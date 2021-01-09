package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

/**
 * BasicAutoCommand
 * 
 */
public class BasicAutoCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public BasicAutoCommand(StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem,
            DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem) {
        
        addCommands (
            new PrintCommand("BasicAutoCommand")
            // .andThen(() -> shooterSubsystem.startShooterMotor()),
            // new WaitCommand(2),
            // RobotContainer.getShootEntireDrumCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            // .andThen(() -> shooterSubsystem.stopShooterMotor())
            // .andThen(() -> RobotContainer.getNavigationSubsystem().resetYaw()),
            // RobotContainer.getBasicTrajectoryCommand()
        );
    }
}