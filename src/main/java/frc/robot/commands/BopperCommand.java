package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * BopperCommand
 * 
 */
public class BopperCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public BopperCommand(ShooterSubsystem shooterSubsystem) {
        addCommands(
            new PrintCommand("Bopping ..."),
            new InstantCommand(shooterSubsystem::raiseBopper, shooterSubsystem),
            new WaitCommand(0.1),
            new InstantCommand(shooterSubsystem::lowerBopper, shooterSubsystem)
        );

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(shooterSubsystem);
    }
}
