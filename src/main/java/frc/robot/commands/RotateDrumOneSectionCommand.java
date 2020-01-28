package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * RotateDrumOneSectionCommand
 * 
 */
public class RotateDrumOneSectionCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private StorageSubsystem m_storageSubsystem;
    private ShooterSubsystem m_shooterSubsystem;
    int count;

    public RotateDrumOneSectionCommand(StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem) {
        m_storageSubsystem = storageSubsystem;
        m_shooterSubsystem = shooterSubsystem;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_storageSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        int count = 0;
        m_storageSubsystem.startDrumMotor();
        System.out.println("Rotating Drum ...");
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        count++;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if(count > 5)
        return m_storageSubsystem.isIndexCheckerBlocked();
        else
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_storageSubsystem.stopDrumMotor();
        System.out.println("Stopping Drum ...");

        m_storageSubsystem.finishIndex();
        m_storageSubsystem.isDrumFull();
        //m_storageSubsystem.setIsShooting(false);
    }
}