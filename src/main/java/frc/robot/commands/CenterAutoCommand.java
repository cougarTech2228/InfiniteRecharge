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
        System.out.println("CenterAutoCommand");
        // addCommands (
        //     new PrintCommand("CenterAutoCommand")
        //     // .andThen(() -> shooterSubsystem.startShooterMotor()),
        //     // new WaitCommand(2),
        //     // RobotContainer.getShootEntireDrumCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
        //     // .andThen(() -> shooterSubsystem.stopShooterMotor())
        //     // .andThen(RobotContainer.getTurnRobotCommand(90))
        //     // .andThen(() -> drivebaseSubsystem.setArcadeDrive(-0.6, 0)), // -0.2, 4
        //     // new WaitCommand(.9)
        //     // .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0))
        //     // .andThen(RobotContainer.getTurnRobotCommand(75))
            
        //     .alongWith(RobotContainer.getRunAcquireMotorCommand(false), (
        //         new SequentialCommandGroup(
        //             new PrintCommand("started acquirer")
        //             .andThen(() -> drivebaseSubsystem.setArcadeDrive(-0.6, 0)), /// -0.2, 9
        //             new WaitCommand(1) // 2.1
        //             .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0))
        //             .andThen(() -> acquisitionSubsystem.setStopAcquirer(true))
        //     )))
        //     //.andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0))

        //     // start acquirer motor
        //     // .andThen(() -> drivebaseSubsystem.setArcadeDrive(-0.6, 0)), /// -0.2, 9
        //     // new WaitCommand(2.1)
        //     // .andThen(() -> drivebaseSubsystem.setArcadeDrive(0, 0))
        //     // stop acquirer motor
        // );



        addCommands(
            new PrintCommand("CenterAutoCommand")
            .andThen(() -> shooterSubsystem.startShooterMotor()),
            new WaitCommand(2),
            //RobotContainer.getShootEntireDrumCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true)),
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true)),
            RobotContainer.getShootOnceCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            .andThen(() -> shooterSubsystem.stopShooterMotor()),
            RobotContainer.getCenterTrajectoryCommand()
            .andThen(() -> shooterSubsystem.startShooterMotor()),
            new WaitCommand(2),
            RobotContainer.getShootEntireDrumCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            .andThen(() -> shooterSubsystem.stopShooterMotor())
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}