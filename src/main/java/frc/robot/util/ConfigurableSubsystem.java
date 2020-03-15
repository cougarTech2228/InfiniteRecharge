package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ConfigurableSubsystem extends SubsystemBase {
    private Configuration configuration;

    public ConfigurableSubsystem() {

    }

    @Override
    public final void register() {
        String name = this.getClass().getSimpleName();
        name = name.substring(name.lastIndexOf('.') + 1);
        SendableRegistry.addLW(this, name, name);
        super.register();
        configuration = Configuration.create(this);
    }

    public TypedSendable getConfigSendable() {
        return configuration;
    }
    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
    }
}