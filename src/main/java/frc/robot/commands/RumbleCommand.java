package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanelSubsystem;
import frc.robot.OI;

/**
 * Rumble Controller
 * Needs a .withTimeout(time) when scheduled or it will vibrate infinitely
 */
public class RumbleCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public RumbleCommand(ControlPanelSubsystem controlPanel) {
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        OI.setXboxRumbleSpeed(1.0);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        OI.setXboxRumbleStop();
    }
}
