package frc.robot.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Scribe {
    private static Scribe scribe;

    String[] printingClasses;

    StringBuffer printStream;
    StringBuffer logStream;

    String allPrintsSinceLastCheck = "";
    String allLogsSinceLastCheck = "";

    Severity printSeverity = Severity.INFO;
    Severity logSeverity = Severity.INFO;

    private SendableScribe console;
    private String[] consoleProperties = new String[2];

    private int totalClasses = 0;

    private long lastUpdate = 0;

    @SuppressWarnings("all")
    public enum Severity {
        INFO(0, Level.INFO, "INFO"), WARNING(1, Level.WARNING, "WARNING"), SEVERE(2, Level.SEVERE, "SEVERE");

        private int intlevel;
        private Level logLevel;
        private String stringLevel;

        private Severity(int i, Level l, String s) {
            this.intlevel = i;
            this.logLevel = l;
            this.stringLevel = s;
        }

        public static Severity parse(String s) {
            if (s.equals("INFO"))
                return INFO;
            if (s.equals("WARNING"))
                return WARNING;
            if (s.equals("SEVERE"))
                return SEVERE;
            return INFO;
        }

        @Override
        public String toString() {
            return stringLevel;
        }
    }

    private class SendableScribe implements Sendable {
        @Override
        public void initSendable(SendableBuilder builder) {
            builder.setSmartDashboardType("ConsoleWidget");
            builder.addStringArrayProperty("Printing Classes", () -> printingClasses, s -> printingClasses = s);
            builder.addStringProperty("Print Stream", () -> allPrintsSinceLastCheck, s -> allPrintsSinceLastCheck = s);
            builder.addStringProperty("Log Stream", () -> allLogsSinceLastCheck, s -> {});
            builder.addStringProperty("Print Severity", () -> printSeverity.stringLevel, s -> printSeverity = Severity.parse(s));
            builder.addStringProperty("All Classes", () -> consoleProperties[0], s -> {});
            builder.addStringProperty("Startup Msg", () -> consoleProperties[1], s -> {});
        }
    }

    private Scribe() {
        printingClasses = new String[0];
        printStream = new StringBuffer();
        logStream = new StringBuffer();
        StringBuffer allClassesProperty = new StringBuffer();

        try {
            ZipFile z = new ZipFile(new File("/home/lvuser/InfiniteRecharge.jar"));
            var entries = z.entries();
            while (entries.hasMoreElements()) {
                var path = entries.nextElement().getName();
                if (path.charAt(0) == 'f' && path.indexOf(".class") > 0 && path.indexOf("$") == -1) {
                    String classPath = path.substring(path.lastIndexOf("frc/robot/"), path.length() - 6).replace('/', '.');
                    allClassesProperty.append(classPath);
                    totalClasses++;
                    if (entries.hasMoreElements()) {
                        allClassesProperty.append(", ");
                    }
                }
            }
            z.close();
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream i = getClass().getClassLoader().getResourceAsStream("StartupMsg.txt");
        String msg = "";
        try {
            msg = new String(i.readAllBytes(), StandardCharsets.UTF_8);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        console = new SendableScribe();
        SendableRegistry.add(console, "Console");
        //SmartDashboard.putData(value);
        consoleProperties[0] = allClassesProperty.toString();
        consoleProperties[1] = msg;

        Shuffleboard.getTab("Test")
            .add(console)
            .withWidget("ConsoleWidget")
            .withProperties(Map.of(
                "All Classes", allClassesProperty.toString(),
                "Startup Message", msg
            ));

        CommandScheduler.getInstance().addButton(() -> {
            long now = System.currentTimeMillis();
            if(now > lastUpdate + 100) {
                if(printStream.length() > 0) {
                    allPrintsSinceLastCheck = printStream.toString();
                    printStream = new StringBuffer();
                }
                if(logStream.length() > 0) {
                    allLogsSinceLastCheck = logStream.toString();
                    logStream = new StringBuffer();
                }
                lastUpdate = now;
            }
        });
    }
    public static Sendable getConsole() {
        return scribe.console;
    }
    public static void initialize() {
        scribe = new Scribe();
    }
    public static void printInfo(String msg, String color) {
        scribe.print(Severity.INFO, msg, color);
    }
    public static void printWarning(String msg, String color) {
        scribe.print(Severity.WARNING, msg, color);
    }
    public static void printSevere(String msg, String color) {
        scribe.print(Severity.SEVERE, msg, color);
    }
    public static void printInfo(String msg) {
        scribe.print(Severity.INFO, msg, "WHITE");
    }

    public static void printWarning(String msg) {
        scribe.print(Severity.WARNING, msg, "WHITE");
    }

    public static void printSevere(String msg) {
        scribe.print(Severity.SEVERE, msg, "WHITE");
    }

    public static void printFromOnly(Class<?>... classes) {
        List<String> printingClasses = new ArrayList<String>();
        for (Class<?> c : classes) {
            printingClasses.add(c.getName());
        }
        scribe.printingClasses = printingClasses.toArray(new String[0]);
    }

    public static void printFromAll() {
        scribe.printingClasses = null;
    }
    private void print(Severity severity, String msg, String color) {
        if (printingClasses.length == totalClasses + 1 && severity.intlevel >= printSeverity.intlevel) {
            printNoCheck(severity, fromClass(), msg, color);
        } else if (severity.intlevel >= printSeverity.intlevel) {
            for (String s : printingClasses) {
                if (fromClass().equals(s)) {
                    printNoCheck(severity, fromClass(), msg, color);
                    break;
                }
            }
        }
    }
    private void printNoCheck(Severity severity, String from, String msg, String color) {
        if(msg.indexOf("#") < 0) {
            printStream.append("#class=" + from + ",severity=" + severity.stringLevel + ",time=" + System.currentTimeMillis() + ",color=" + color + "#" + msg);
        }
    }

    public static void setPrintSeverity(Severity severity) {
        scribe.printSeverity = severity;
    }

    private void log(Severity severity, String msg) {
        if (severity.intlevel >= logSeverity.intlevel) {
            for (String s : printingClasses) {
                if (fromClass().equals(s)) {
                    logNoCheck(severity, fromClass(), msg);
                    break;
                }
            }
        }
    }
    private void logNoCheck(Severity severity, String from, String msg) {
        double timeLeft = Timer.getMatchTime();
        if(timeLeft != -1) {
            msg = "[" + DriverStation.getInstance().getMatchType().name() + " time left: " + String.format("%.2f", timeLeft) + "]";
        }
        if(msg.indexOf("#") < 0) {
            logStream.append("#class=" + from + ",severity=" + severity.stringLevel + ",time=" + System.currentTimeMillis() + "#" + msg);
        }
    }
    public static void logSevere(String msg) {
        scribe.log(Severity.SEVERE, msg);
    }
    public static void logWarning(String msg) {
        scribe.log(Severity.WARNING, msg);
    }
    public static void logInfo(String msg) {
        scribe.log(Severity.INFO, msg);
    }
    private String fromClass() {
        return new Exception().getStackTrace()[3].getClassName();
    }
}