package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

/**
 * Right Auto Command
 * 
 */
public class RightAutoCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public RightAutoCommand(StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem,
            DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem) {
        
        addCommands (
            
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}