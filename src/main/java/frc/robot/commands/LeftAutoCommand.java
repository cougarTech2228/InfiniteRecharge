package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

/**
 * LeftAutoCommand
 * 
 */
public class LeftAutoCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public LeftAutoCommand(StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem,
            DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem) {
        
        addCommands (
            new PrintCommand("LeftAutoCommand"),
            RobotContainer.getTurnRobotCommand(-55)
            .andThen(() -> shooterSubsystem.startShooterMotor()),
            new WaitCommand(2),
            //RobotContainer.getShootEntireDrumCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true)),
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true)),
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            .andThen(() -> shooterSubsystem.stopShooterMotor()),
            RobotContainer.getLeftTrajectoryCommand()
            .andThen(() -> shooterSubsystem.startShooterMotor()),
            new WaitCommand(2),
            RobotContainer.getShootEntireDrumCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            .andThen(() -> shooterSubsystem.stopShooterMotor())
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}