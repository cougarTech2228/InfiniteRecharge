package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.DrumSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * MinAutoCommand
 * 
 */
public class MinAutoCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public MinAutoCommand(DrumSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem,
            DrivebaseSubsystem drivebaseSubsystem) {
        
        addCommands (
            new PrintCommand("MinAutoCommand"),
            shooterSubsystem.cmdStartShooter(),
            new WaitCommand(2),
            storageSubsystem.cmdShootAll(),
            shooterSubsystem.cmdStopShooter(),
            drivebaseSubsystem.cmdSetArcadeDrive(0.5, 0, 1)
        );
    }
}