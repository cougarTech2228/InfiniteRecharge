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
        // addCommands(
        //     new PrintCommand("TryToShootOnce"),
        //     new SelectCommand(
        //         Map.of(
        //             true, (
        //                 RobotContainer.getBopperCommand()
        //                 .andThen(() -> storageSubsystem.getBallArray().shoot())
        //                 .andThen(RobotContainer.getRotateDrumOneSectionCommand())
        //             ),
        //             false, (
        //                 new PrintCommand("Tried to shoot but no ball was there, drum might be empty")
        //                 .andThen(RobotContainer.getRotateDrumOneSectionCommand())
        //             )
        //         ), () -> shooterSubsystem.isShooterBallOccupied()
        //     )
        // );

        addCommands(
            new PrintCommand("TryToShootOnce")
            .andThen(() -> {
                double curSpeed = shooterSubsystem.getShooterMotor().getSpeed();
                int curDistance = RobotContainer.getGarminLidarSubsystem().getAverage();
                double newSpeed = shooterSubsystem.getShooterMotor().closestDistance(curDistance);
                if(curSpeed > 0 && Math.abs(newSpeed - curSpeed) > 10000) {
                    shooterSubsystem.getShooterMotor().start(curDistance);
                }
            }),
            RobotContainer.getBopperCommand()
            .andThen(() -> storageSubsystem.getBallArray().shoot()),
            RobotContainer.getRotateDrumOneSectionCommand()
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}
