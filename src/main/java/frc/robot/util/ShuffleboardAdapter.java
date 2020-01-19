package frc.robot.util;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder.BooleanConsumer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.commands.MethodCommand;

public class ShuffleboardAdapter {
    private ShuffleboardTab subsystemTab;
    private ShuffleboardContainer activeContainer;
    private Dictionary<String, SimpleWidget> widgets;
    private String activeWidget;
    private List<Runnable> updaters;

    private boolean isUpdating;

    public ShuffleboardAdapter(String subsystem) {
        subsystemTab = Shuffleboard.getTab(subsystem);
        activeContainer = subsystemTab;
        updaters = new ArrayList<Runnable>();
        
        widgets = new Hashtable<String, SimpleWidget>();
        isUpdating = true;
        CommandScheduler.getInstance().addButton(() -> {
            for(var update : updaters) {
                update.run();
            }
        });
    }
    public ShuffleboardAdapter update(boolean update) {
        isUpdating = update;
        return this;
    }
    public ShuffleboardAdapter addBox(String name) {
        activeContainer = subsystemTab.getLayout(name, BuiltInLayouts.kList);
        return this;
    }
    public ShuffleboardAdapter addBoolean(String name, boolean defaultValue, BooleanConsumer receiver) {
        var v = widgets.put(name, activeContainer.add(name, defaultValue)).getEntry();
        updaters.add(() -> receiver.accept(v.getBoolean(false)));
        return this;
    }
    public ShuffleboardAdapter addBoolean(String name, boolean defaultValue, BooleanSupplier updater) {
        var v = widgets.put(name, activeContainer.add(name, defaultValue)).getEntry();
        updaters.add(() -> v.setBoolean(updater.getAsBoolean()));
        return this;
    }
    public ShuffleboardAdapter addDouble(String name, double defaultValue, DoubleConsumer receiver) {
        var v = activeContainer.add(name, defaultValue);//.withWidget(BuiltInWidgets.kTextView);
        widgets.put(name, v);
        activeWidget = name;
        updaters.add(() -> {
            double d = v.getEntry().getDouble(Double.POSITIVE_INFINITY);
            if(d != Double.POSITIVE_INFINITY) {
                receiver.accept(v.getEntry().getDouble(defaultValue));
            }
        });
        return this;
    }
    public ShuffleboardAdapter addDouble(String name, double defaultValue, DoubleSupplier updater) {
        var v = activeContainer.add(name, defaultValue);
        widgets.put(name, v);
        activeWidget = name;
        updaters.add(() -> v.getEntry().setDouble(updater.getAsDouble()));
        return this;
    }
    public ShuffleboardAdapter addColorBox(String name, Color8BitSupplier updater) {
        var v = activeContainer.add(name, true).withWidget(BuiltInWidgets.kBooleanBox);
        widgets.put(name, v);
        activeWidget = name;
        updaters.add(() -> {
            String color = String.format("#%02X%02X%02X", updater.get().red, updater.get().green, updater.get().blue);
            v.withProperties(Map.of("Color when true", color));
        });
        return this;
    }
    public ShuffleboardAdapter addColorBox(String name, ColorSupplier updater) {
        addColorBox(name, () -> new Color8Bit(updater.get()));
        return this;
    }
    public ShuffleboardAdapter withWidget(WidgetType widget) {
        getCurrentWidget().withWidget(widget);
        return this;
    }
    public ShuffleboardAdapter withProperties(Map<String, Object> properties) {
        getCurrentWidget().withProperties(properties);
        return this;
    }
    public ShuffleboardAdapter withSize(int width, int height) {
        getCurrentWidget().withPosition(width, height);
        return this;
    }
    public SimpleWidget getCurrentWidget() {
        return getWidget(activeWidget);
    }
    public SimpleWidget getWidget(String varName) {
        return widgets.get(varName);
    }
    public abstract interface ColorSupplier {
        public abstract Color get();
    }
    public abstract interface Color8BitSupplier {
        public abstract Color8Bit get();
    }
}