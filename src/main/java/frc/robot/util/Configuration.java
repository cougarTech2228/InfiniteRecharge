package frc.robot.util;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.MethodCommand;
import frc.robot.commands.UnstoppableCommand;

public final class Configuration implements TypedSendable {
    public List<Constant> variables = new ArrayList<Constant>();
    public Runnable updater = () -> {};
    
    public static Configuration create(Object parent) {
        return new Configuration(parent);
    }
    public static Configuration create(Object parent, Runnable updaterMethod) {
        var configInstance = new Configuration(parent);
        configInstance.updater = updaterMethod;
        return configInstance;
    }
    private Configuration() {

    }
    private Configuration(Object parent) {
        register(parent);
    }
    private void register(Object parent) {
        for (var v : parent.getClass().getFields()) {
            if (v.isAnnotationPresent(Config.class)) {
                try {
                    String fieldTypeName = v.get(parent).getClass().getSimpleName();

                    String parentClassName = parent.getClass().getSimpleName();

                    if (v.get(parent) == null)
                        throw new Exception();
                    switch (fieldTypeName) {
                    case "Double":
                    case "Integer":
                    case "String":
                    case "Boolean":
                        variables.add(new Constant(v, parent));
                        Scribe.printInfo("Successfully registered " + fieldTypeName + v.getName() + " of " + parentClassName);
                        break;
                    default:
                        Scribe.printWarning(
                            "Constant " + fieldTypeName + v.getName() + " of " + parentClassName + " not initialized\n" +
                            "Only Doubles, Integers, Strings, and Booleans, can be configured"
                        );
                        break;
                    }
                } catch (Exception e) {
                    Scribe.printWarning(
                        "Constant " + v.getName() + " not initialized\n" +
                        "Configurations must be public and initialized"
                    );
                    e.printStackTrace();
                }
            }
        }
        // create a background updater
        CommandScheduler.getInstance().addButton(() -> {
            for (var constant : variables) {
                constant.update();
            }
        });
        // save config values to file when robot is disabled
        new Trigger(RobotState::isDisabled).whenActive(new UnstoppableCommand(new MethodCommand(() -> {
            for (var config : variables) {
                config.saveToConfig();
            }
            Scribe.printInfo("Configs for " + parent.getClass().getSimpleName() + " saved");
        })));
        // load config values from file when robot is enabled
        new Trigger(RobotState::isEnabled).whenActive(() -> {
            for (var config : variables) {
                config.setToConfigValue();
            }
            Scribe.printInfo("Configs for " + parent.getClass().getSimpleName() + " reinitialized");
        });
    }
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("ConfigWidget");
        for (var constant : variables) {
            builder.addValueProperty(
                constant.getVarName() + "/Value",
                () -> constant.getNetworkValue().getValue(),
                networkValue -> constant.setValue(networkValue.getValue())
            );
            builder.addValueProperty(
                constant.getVarName() + "/Default",
                () -> constant.getNetworkDefaultValue().getValue(),
                null
            );
            builder.addBooleanArrayProperty(
                constant.getVarName() + "/Config",
                () -> constant.getConfig(),
                null
            );
        }
    }
    @Override
    public String widget() {
        return "ConfigWidget";
    }
}