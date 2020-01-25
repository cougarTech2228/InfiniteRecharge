package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanelSubsystem;
import frc.robot.OI;
import frc.robot.Constants;

/**
 * RumbleCommand
 * 
 */
public class RumbleCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public RumbleCommand(ControlPanelSubsystem controlPanel) {

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(controlPanel);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        OI.setXboxRumbleSpeed(Constants.XBOX_RUMBLE_TIME);
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
