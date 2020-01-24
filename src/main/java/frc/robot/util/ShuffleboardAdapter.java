package frc.robot.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
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

public class ShuffleboardAdapter {
    private final ShuffleboardTab subsystemTab;
    private ShuffleboardContainer activeContainer;
    private Dictionary<String, SimpleWidget> widgets;
    private String activeWidget;
    private List<Runnable> updaters;
    private List<Double> prevValues;

    private boolean isUpdating;

    public ShuffleboardAdapter(String subsystem) {
        subsystemTab = Shuffleboard.getTab(subsystem);
        activeContainer = subsystemTab;

        updaters = new ArrayList<Runnable>();
        prevValues = new ArrayList<Double>();
        
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
    public ShuffleboardAdapter inBox(String name) {
        activeContainer = activeContainer.getLayout(name, BuiltInLayouts.kList);
        return this;
    }
    public ShuffleboardAdapter inTab() {
        activeContainer = subsystemTab;
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
    public ShuffleboardAdapter addString(String name, String defaultValue, Consumer<String> receiver) {
        var v = activeContainer.add(name, defaultValue);
        widgets.put(name, v);
        activeWidget = name;
        updaters.add(() -> receiver.accept(v.getEntry().getString(defaultValue)));
        return this;
    }
    public ShuffleboardAdapter addString(String name, String defaultValue, Supplier<String> updater) {
        var v = activeContainer.add(name, defaultValue);
        widgets.put(name, v);
        activeWidget = name;
        updaters.add(() -> v.getEntry().setString(updater.get()));
        return this;
    }
    public ShuffleboardAdapter addDoubleSlider(String name, double defaultValue, DoubleConsumer receiver, double min, double max) {
        addDouble(name, defaultValue, receiver)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", min, "max", max));
        return this;
    }
    public ShuffleboardAdapter addDoubleSlider(String name, double defaultValue, DoubleConsumer receiver, double min, double max, double incrementerBy) {
        addDouble(name, defaultValue, receiver)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", min, "max", max, "block increment", incrementerBy));
        System.out.println(activeWidget);
        return this;
    }
    public ShuffleboardAdapter addDoubleText(String name, double defaultValue, DoubleConsumer receiver) {
        addDouble(name, defaultValue, receiver).withWidget(BuiltInWidgets.kTextView);
        return this;
    }
    public ShuffleboardAdapter addDoubleText(String name, double defaultValue, DoubleConsumer receiver, double min, double max) {
        addDouble(name, defaultValue, receiver, min, max).withWidget(BuiltInWidgets.kTextView);
        return this;
    }
    public ShuffleboardAdapter addDouble(String name, double defaultValue, DoubleConsumer receiver, double min, double max) {
        var v = activeContainer.add(name, defaultValue);
        widgets.put(name, v);
        activeWidget = name;
        int index = prevValues.size();
        prevValues.add(defaultValue);

        updaters.add(() -> {
            double d = v.getEntry().getDouble(Double.POSITIVE_INFINITY);
            if(d != Double.POSITIVE_INFINITY && d >= min && d <= max) {
                receiver.accept(d);
                prevValues.set(index, d);
            }
            else {
                v.getEntry().setDouble(prevValues.get(index));
            }
        });
        return this;
    }
    public ShuffleboardAdapter addDouble(String name, double defaultValue, DoubleConsumer receiver) {
        addDouble(name, defaultValue, receiver, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
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