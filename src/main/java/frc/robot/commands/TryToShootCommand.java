package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import java.util.Map;

/**
 * TryToShootCommand
 * 
 * Tries to shoot the ball at the current position. Either shoots the ball or rotates one section
 */
public class TryToShootCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public TryToShootCommand(ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem) {
        addCommands(
            new PrintCommand("TryToShootOnce"),
            new SelectCommand(
                Map.of(
                    true, (
                        RobotContainer.getBopperCommand()
                        .andThen(() -> storageSubsystem.getBallArray().shoot())
                        .andThen(RobotContainer.getRotateDrumOneSectionCommand())
                    ),
                    false, (
                        new PrintCommand("Tried to shoot but no ball was there, drum might be empty")
                        .andThen(RobotContainer.getRotateDrumOneSectionCommand())
                    )
                ), () -> shooterSubsystem.isShooterBallOccupied()
            )
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}
