package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.Constants;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * RotateDrumOneSectionCommand
 * 
 * Rotates the drum until a flag is triggered depending on if the robot is shooting or acquiring/storing
 */
public class RotateDrumOneSectionCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private StorageSubsystem m_storageSubsystem;
    private ShooterSubsystem m_shooterSubsystem;
    private int m_commandExecutionCount;

    public RotateDrumOneSectionCommand(StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem) {
        System.out.println("Rotate drum one section");
        m_storageSubsystem = storageSubsystem;
        m_shooterSubsystem = shooterSubsystem;

        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements(m_storageSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_storageSubsystem.getBallArray().rotate();
        m_commandExecutionCount = 0;
        m_storageSubsystem.startDrumMotor();

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_commandExecutionCount++; // Count is necessary because the drum starts on the flag being blocked,
                                   // so the drum needs to rotate for a second or two before it checks for flags
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if(m_commandExecutionCount > Constants.LOOPS_TO_WAIT) {

            if(m_shooterSubsystem.getIsShooting()) {
                return m_shooterSubsystem.isShooterPositionTripped();
            } else {
                return m_storageSubsystem.isAcquirePositionTripped();
            }

        } else {
            return false;
        }

        // Command will end when the AcquireFlag or ShooterFlag interrupt is fired
        // depending on the shooting/acquiring mode of the robot.

        //return false; 
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_storageSubsystem.stopDrumMotor();
        m_storageSubsystem.resetHasBeenTripped();
        m_storageSubsystem.checkIfDrumFull();
    }
}