package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class DriverMappings {
    private String driver;
    private Runnable setting;
    private static SendableChooser<DriverMappings> activeDriver = new SendableChooser<DriverMappings>();
    public DriverMappings(String driver, Runnable setting) {
        this.setting = setting;
        this.driver = driver;
        activeDriver.addOption(driver, this);
        activeDriver.setDefaultOption(driver, this);
    }
    public static void apply() {
        CommandScheduler.getInstance().clearButtons();
        activeDriver.getSelected().setting.run();
    }
    public static String getActiveDriver() {
        return activeDriver.getSelected().driver;
    }
    public static SendableChooser<DriverMappings> getChooser() {
        return activeDriver;
    }
}