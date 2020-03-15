package frc.robot.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.UnstoppableCommand;

public class ConfigFile {
    private static ConfigFile instance = new ConfigFile();

    private Map<String, Object> loadedConfigurations;
    private File configFile = new File("/home/lvuser/config.txt");

    public static ConfigFile getInstance() {
        return instance;
    }
    public ConfigFile() {
        loadedConfigurations = new HashMap<String, Object>();
        try {
            if(!configFile.exists()) {
                configFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Trigger(RobotState::isEnabled).whenActive(() -> {
            loadConfigFile();
        });
        new Trigger(RobotState::isDisabled).whenActive(
            new UnstoppableCommand(
                new WaitCommand(2).andThen(() -> {
                    saveConfigFile();
                })
            )
        );
    }
    public void fullReset() {
        loadedConfigurations = new HashMap<String, Object>();
        saveConfigFile();
    }
    public void loadConfigFile() {
        try {
            Scribe.printInfo("loading...");
            var f = new FileReader(configFile);
            f.close();
            var configReader = new BufferedReader(new FileReader(configFile));
            String declaration = configReader.readLine();
            while(declaration != null) {
                int valueIndex = declaration.indexOf(" = ");
                switch(declaration.charAt(0)) {
                    case 'D': {
                        String name = declaration.substring("Double ".length(), valueIndex);
                        double value = Double.parseDouble(declaration.substring(valueIndex + 3, declaration.length()));
                        Scribe.printInfo("Loaded constant [" + name + "] with value [" + value + "]");
                        loadedConfigurations.put(name, value);
                        break;
                    }
                    case 'I': {
                        String name = declaration.substring("Integer ".length(), valueIndex);
                        int value = Integer.parseInt(declaration.substring(valueIndex + 3, declaration.length()));
                        Scribe.printInfo("Loaded constant [" + name + "] with value [" + value + "]");
                        loadedConfigurations.put(name, value);
                        break;
                    }
                    case 'B': {
                        String name = declaration.substring("Boolean ".length(), valueIndex);
                        boolean value = Boolean.parseBoolean(declaration.substring(valueIndex + 3, declaration.length()));
                        Scribe.printInfo("Loaded constant [" + name + "] with value [" + value + "]");
                        loadedConfigurations.put(name, value);
                        break;
                    }
                    case 'S': {
                        String name = declaration.substring("String ".length(), valueIndex);
                        String value = declaration.substring(valueIndex + 3, declaration.length());
                        Scribe.printInfo("Loaded constant [" + name + "] with value [" + value + "]");
                        loadedConfigurations.put(name, value);
                        break;
                    }
                }
                declaration = configReader.readLine();
            }
            configReader.close();
            Scribe.printInfo("Loaded.");
        } catch(Exception e) {
            Scribe.printInfo("ConfigFile had an EPIC fail while loading");
        }
    }
    public void config(String name, Object value) {
        loadedConfigurations.put(name, value);
    }
    public Object getConfig(String name) {
        return loadedConfigurations.get(name);
    }
    public Object getConfig(String name, Object defaultValue) {
        var ret = loadedConfigurations.get(name);
        if(ret == null) {
            config(name, defaultValue);
            return defaultValue;
        } else {
            return ret;
        }
    }
    public void saveConfigFile() {
        Scribe.printInfo("Saving...");
        try {
            if(configFile.delete() || !configFile.exists()) {
                configFile.createNewFile();
            }
            var fileWriter = new FileWriter(configFile);
            for(var entry : loadedConfigurations.entrySet()) {
                fileWriter.write(
                    entry.getValue().getClass().getSimpleName() + " " +
                    entry.getKey() + " = " +
                    entry.getValue().toString() + "\n"
                );
            }
            fileWriter.close();
            //printInfo(this, "saved file");
            Scribe.printInfo("Saved.");
        } catch(Exception e) {
            Scribe.printInfo("ConfigFile failed to save.");
        }
    }
}