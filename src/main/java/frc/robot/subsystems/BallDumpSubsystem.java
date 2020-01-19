package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.MethodCommand;

public class BallDumpSubsystem extends SubsystemBase {
    private Solenoid dumper;
    private Compressor compressor;

    public enum DumperState {
        raised(false), lowered(true);
        boolean value;
        DumperState(boolean value) {
            this.value = value;
        }
    }

    public BallDumpSubsystem() {
        System.out.println("dumper init");
        dumper = new Solenoid(0);
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