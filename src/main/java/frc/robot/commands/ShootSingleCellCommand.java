package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.OI;
import frc.robot.RobotContainer;

/**
 * Rumble Controller
 * Needs a .withTimeout(time) when scheduled or it will vibrate infinitely
 */
public class ShootSingleCellCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private ShooterSubsystem m_shooterSubsystem;

    public ShootSingleCellCommand(ShooterSubsystem shooterSubsystem) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooterSubsystem = shooterSubsystem;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        while(!m_shooterSubsystem.isShooterSlotOccupied())
        {
            CommandScheduler.getInstance()
                        .schedule(RobotContainer.getRotateDrumOneSectionCommand()
                        .andThen(new WaitCommand(1))); // move drum until there is a ball there to shoot;
        }
        m_shooterSubsystem.startFlywheel();

        // raise lifter
        // wait a second
        // lower lifter
        CommandScheduler.getInstance()
            .schedule(new WaitCommand(0.1)
            .andThen(() -> m_shooterSubsystem.raiseLifter())
            .andThen(new WaitCommand(1))
            .andThen(() -> m_shooterSubsystem.lowerLifter())
            .andThen(new WaitCommand(1)));

        m_shooterSubsystem.stopFlywheel();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        
    }
}
