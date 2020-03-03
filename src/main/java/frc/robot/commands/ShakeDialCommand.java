package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.StorageSubsystem;

/**
 * ShakeDialCommand
 * 
 * Jiggles the dial to get balls unstuck
 */
public class ShakeDialCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public ShakeDialCommand(StorageSubsystem storageSubsystem) {
        
        addCommands (
            new PrintCommand("Shake Dial")
            .andThen(() -> storageSubsystem.startDrumMotor()),
            new WaitCommand(0.2)
            .andThen(() -> storageSubsystem.stopDrumMotor())
            .andThen(() -> storageSubsystem.startDrumMotorBackwards()),
            new WaitCommand(0.2)
            .andThen(() -> storageSubsystem.stopDrumMotor())
            .andThen(() -> storageSubsystem.startDrumMotor()),
            new WaitCommand(0.2)
            .andThen(() -> storageSubsystem.stopDrumMotor())
            .andThen(() -> storageSubsystem.startDrumMotorBackwards()),
            new WaitCommand(0.2)
            .andThen(() -> storageSubsystem.stopDrumMotor())
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}