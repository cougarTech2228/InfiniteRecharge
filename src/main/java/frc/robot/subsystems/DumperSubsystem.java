package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.MethodCommand;

public class DumperSubsystem extends SubsystemBase {
    private Solenoid dumper;
    private Compressor compressor;

    public enum DumperState {
        raised(false), lowered(true);
        boolean value;
        DumperState(boolean value) {
            this.value = value;
        }
    }

    public DumperSubsystem() {
        register();
        //dumper = new Solenoid(0);
        compressor = new Compressor();
        compressor.setClosedLoopControl(true);
    }
    @Override
    public void periodic() {
        if(!compressor.enabled()) {
            compressor.start();
        }
    }
    
    public Command cmdSetPosition(DumperState state) {
        return new MethodCommand(() -> {
            dumper.set(state.value);
            System.out.println(state);
        });
    }
}