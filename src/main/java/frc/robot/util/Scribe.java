package frc.robot.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import edu.wpi.first.wpilibj.DriverStation;

public class Scribe {
    private static Scribe logger = new Scribe();

    java.util.logging.Logger baseLogger;
    String[] loggingClasses;
    String[] printingClasses;

    private int printSeverity = 0;

    public enum Severity {
        INFO(0, Level.INFO, "INFO"),
        WARNING(1, Level.WARNING, "WARNING"),
        SEVERE(2, Level.SEVERE, "SEVERE");
        private int intlevel;
        private Level logLevel;
        private String stringLevel;
        private Severity(int i, Level l, String s) {
            this.intlevel = i;
            this.logLevel = l;
            this.stringLevel = s;
        }
        @Override
        public String toString() {
            return stringLevel;
        }
    }
    public Scribe() {
        
        /*FileHandler logFile;

        baseLogger = java.util.logging.Logger.getLogger("name");
        
        try {
            Date time = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
            String path = "C:/Users/Public/Documents/FRC/Robot Logs/" + dateFormat.format(time) + ".log";
            File f = new File(path);
            f.createNewFile();

            logFile = new FileHandler(path);  

            baseLogger.addHandler(logFile);
            SimpleFormatter formatter = new SimpleFormatter();  
            logFile.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    public static void printInfo(String msg) {
        logger.print(Severity.INFO, msg);
    }
    public static void printWarning(String msg) {
        logger.print(Severity.WARNING, msg);
    }
    public static void printSevere(String msg) {
        logger.print(Severity.SEVERE, msg);
    }
    public static void printFromOnly(Class<?>... classes) {
        List<String> printingClasses = new ArrayList<String>();
        for(Class<?> c : classes) {
            printingClasses.add(c.getName());
        }
        logger.printingClasses = printingClasses.toArray(new String[0]);
    }
    public static void printFromAll() {
        logger.printingClasses = null;
    }
    private void print(Severity severity, String msg) {
        if(printingClasses == null && severity.intlevel >= printSeverity) {
            System.out.println(fromClass() + " (" + severity.stringLevel + "): " + msg);
        } else if(severity.intlevel >= printSeverity) {
            for(String s : printingClasses) {
                if(fromClass().equals(s)) {
                    System.out.println(fromClass() + " (" + severity.stringLevel + "): " + msg);
                    break;
                }
            }
        }
    }
    public static void saveLog() {
        //logger.baseLogger.
    }
    public static void setPrintSeverity(Severity severity) {
        logger.printSeverity = severity.intlevel;
    }
    /*
    public static void logFromOnly(Object... classInstances) {
        List<String> loggingClasses = new ArrayList<String>();
        for(Object o : classInstances) {
            loggingClasses.add(o.getClass().getSimpleName());
        }
        logger.loggingClasses = loggingClasses.toArray(new String[0]);
    }
    public static void logFromAll() {
        logger.loggingClasses = null;
    }
    public static void setLogSeverity(Severity severity) {
        logger.baseLogger.setLevel(severity.logLevel);
    }
    public static void logSevere(String msg) {
        logger.log(Severity.SEVERE, msg);
    }
    public static void logWarning(String msg) {
        logger.log(Severity.WARNING, msg);
    }
    public static void logInfo(String msg) {
        logger.log(Severity.INFO, msg);
    }
    private void log(Severity l, String msg) {
        if(loggingClasses == null) {
            baseLogger.log(l.logLevel, msg);
        } else {
            for(String s : loggingClasses) {
                if(fromClass().equals(s)) {
                    baseLogger.log(l.logLevel, msg);
                    break;
                }
            }
        }
    }*/
    private String fromClass() {
        return new Exception().getStackTrace()[3].getClassName();
    }
}