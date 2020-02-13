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
 * ShootEntireDrumCommand
 * 
 * This command shoots the entire drum. It calls tryToShootOnce 5 times.
 */
public class AutonomousOneCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public AutonomousOneCommand(StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem,
            DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem) {

        addCommands(
            new PrintCommand("auto 2")
            .andThen(() -> shooterSubsystem.startShooterMotor()),
            new WaitCommand(2),
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true)),
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true)),
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true)),
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true)),
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            .andThen(() -> shooterSubsystem.stopShooterMotor())
            .andThen(() -> drivebaseSubsystem.setArcadeDrive(0.2, 0)),
            new WaitCommand(1)
            .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0))
            // new WaitCommand(1)
            // .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0.8)), // turn 90 degrees
            // new WaitCommand(0.93)
            // .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0))
            // .andThen(() -> drivebaseSubsystem.setArcadeDrive(-0.2, 0)), // drive forwarrd
            // new WaitCommand(3.6)
            // .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0))
            // .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0.8)), // turn 90 degrees
            // new WaitCommand(0.70)
            // .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0))
            // .andThen(() -> drivebaseSubsystem.setArcadeDrive(-0.2, 0)), // drive forwar
            // new WaitCommand(7.5)
            // .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0))
        );

        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}