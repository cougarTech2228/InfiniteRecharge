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
 * MinAutoCommand
 * 
 */
public class MinAutoCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public MinAutoCommand(StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem,
            DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem) {
        
        addCommands (
            new PrintCommand("MinAutoCommand")
            /*.andThen(() -> shooterSubsystem.startShooterMotor()),
            new WaitCommand(2),
            RobotContainer.getShootEntireDrumCommand().beforeStarting(() -> shooterSubsystem.setIsShooting(true))
            .andThen(() -> shooterSubsystem.stopShooterMotor())*/
            .andThen(() -> drivebaseSubsystem.setArcadeDrive(.5, 0)),
            new WaitCommand(2)
            .andThen(() -> drivebaseSubsystem.stop())
        );
    }
}