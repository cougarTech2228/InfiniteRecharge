package frc.robot.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.function.Function;

import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Constant {
    private Object previousValue;
    private Config configuration;
    private Object parent;
    private String name;
    private Field field;
    private Function<Object, Object> toFieldType;
    private Function<Object, NetworkTableValue> toNetworkValue;
    private NetworkTableEntry value, defaultValue, configEntry;
    private boolean[] booleanConfiguration;

    private String stackTrace(Exception e) {
        StringWriter strStream = new StringWriter();
        e.printStackTrace(new PrintWriter(strStream));
        return strStream.toString();
    }
    public Constant(Field f, Object s) {
        this.field = f;
        this.parent = s;
        this.name = s.getClass().getSimpleName() + "." + f.getName();
        this.configuration = f.getAnnotation(Config.class);
        var constantTable = NetworkTableInstance.getDefault().getTable("constants");
        this.value = constantTable.getEntry(name + "/Value");
        this.defaultValue = constantTable.getEntry(name + "/Default");
        this.configEntry = constantTable.getEntry(name + "/Config");

        booleanConfiguration = new boolean[4];
        booleanConfiguration[0] = configuration.push();
        booleanConfiguration[1] = configuration.save();
        booleanConfiguration[2] = configuration.load();
        booleanConfiguration[3] = configuration.readonly();

        try {
            previousValue = f.get(s);
        } catch(Exception e) {
            Scribe.printSevere("Enormous error initializing variable " + name + ". Find Justin. Tell him his code sucks.\n" + stackTrace(e));
        }
        setConversion(previousValue);
        try {
            if(configuration.save() && configuration.load()) {
                var configValue = ConfigFile.getInstance().getConfig(name, previousValue);
                field.set(parent, configValue);
            } else {
                field.set(parent, previousValue);
            }
        } catch (Exception e) {
            Scribe.printSevere("Enormous error initializing variable " + name + ". Find Justin. Tell him his code sucks.\n" + stackTrace(e));
        }
        if(configuration.push()) {
            value.setValue(toNetworkValue.apply(previousValue));
            defaultValue.setValue(toNetworkValue.apply(previousValue));
        }
        configEntry.setBooleanArray(booleanConfiguration);
    }
    /**
     * <br>[0] = push</br>
     * <br>[1] = save</br>
     * <br>[2] = load</br>
     * <br>[3] = readonly</br>
     */
    public boolean[] getConfig() {
        return booleanConfiguration;
    }
    public NetworkTableEntry getNetworkValue() {
        return value;
    }
    public NetworkTableEntry getNetworkDefaultValue() {
        return defaultValue;
    }
    public NetworkTableEntry getNetworkConfigValue() {
        return configEntry;
    }
    public String getFullName() {
        return name;
    }
    public String getVarName() {
        return field.getName();
    }
    public void setValue(Object o) {
        try {
            field.set(parent, toFieldType.apply(o));
        } catch (Exception e) {
            Scribe.printSevere("Enormous error setting variable " + name + ". Most likely you're trying to set it to the wrong type\n" + stackTrace(e));
        }
    }
    public void setToConfigValue() {
        value.setValue(
            toNetworkValue.apply(
                ConfigFile.getInstance().getConfig(
                    name,
                    previousValue
                )
            )
        );
    }
    public boolean saveToConfig() {
        if(field.getAnnotation(Config.class).save()) {
            ConfigFile.getInstance().config(name, previousValue);
            return true;
        }
        return false;
    }
    private void setConversion(Object object) {
        var type = object.getClass();
        toFieldType = o -> o;

        if(type == double.class || type == Double.class) {
            toNetworkValue = d -> NetworkTableValue.makeDouble((Double)d);
        } else if(type == int.class || type == Integer.class) {
            toNetworkValue = i -> NetworkTableValue.makeDouble((Integer)i);
            toFieldType = d -> ((Double)d).intValue();
        } else if(type == boolean.class || type == Boolean.class) {
            toNetworkValue = b -> NetworkTableValue.makeBoolean((Boolean)b);
        } else if(type == String.class) {
            toNetworkValue = s -> NetworkTableValue.makeString((String)s);
        } else {
            Scribe.printSevere("Type " + type.getSimpleName() + " cannot be set to a constant!");
        }
    }
    public void update() {
        if(configuration.push()) {
            Object netVal = toFieldType.apply(value.getValue().getValue());
            Object fieldVal = null;
            try {
                fieldVal = field.get(parent);
            } catch (Exception e) {
                Scribe.printSevere("Enormous error updating variable " + name + ". Find Justin. Tell him his code sucks\n" + stackTrace(e));
            }

            if (!fieldVal.equals(netVal)) {
                if (!previousValue.equals(fieldVal)) {
                    //the field value has been updated
                    value.setValue(toNetworkValue.apply(fieldVal));
                    previousValue = fieldVal;
                    System.out.println("set to: " + previousValue);
                } else if(!configuration.readonly()) {
                    //the networkTable value has been updated
                    try {
                        field.set(parent, netVal);
                    } catch (Exception e) {
                        Scribe.printSevere("Enormous error updating variable " + name + ". Find Justin. Tell him his code sucks\n" + stackTrace(e));
                    }
                    previousValue = netVal;
                    System.out.println("set to: " + previousValue);
                }
            }
        }
        
    }
}