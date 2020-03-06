package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * BopperCommand
 * 
 * Raises and lowers the bopper with a delay of Constants.BOPPER_WAIT_TIME
 */
public class BopperCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public BopperCommand(ShooterSubsystem shooterSubsystem) {
        addCommands(
            new PrintCommand("Bopping ...")
            .andThen(() -> shooterSubsystem.raiseBopper()),
            new WaitCommand(Constants.BOPPER_WAIT_TIME)
            .andThen(() -> shooterSubsystem.lowerBopper())
        );
        // Use addRequirements() here to declare subsystem dependencies.
        // addRequirements(shooterSubsystem);
    }
}
