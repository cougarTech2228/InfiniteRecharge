package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

/**
 * CenterAutoCommand
 * 
 * Center Auto Description:
 * -Shoots 3 balls
 * -Turns 90 degrees to the right
 * -Drives forward
 * -Turns 90 degrees to the right
 * -Turn on acquirer
 * -Drive forward and acquire three balls in trench
 */
public class CenterAutoCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public CenterAutoCommand(StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem,
            DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem) {
        addCommands(
            new PrintCommand("CenterAutoCommand")
            // .andThen(() -> shooterSubsystem.startShooterMotor()),
            // new WaitCommand(2),
            // RobotContainer.getShootEntireDrumCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            // .andThen(() -> shooterSubsystem.stopShooterMotor()),
            //.andThen(() -> RobotContainer.getNavigationSubsystem().resetYaw()),
            //RobotContainer.getCenterTrajectoryCommand()
            // .andThen(() -> shooterSubsystem.startShooterMotor()),
            // new WaitCommand(2),
            // RobotContainer.getShootEntireDrumCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            // .andThen(() -> shooterSubsystem.stopShooterMotor())
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}