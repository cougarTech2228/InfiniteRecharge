package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * ShootEntireDrumCommand
 * 
 * This command shoots the entire drum. It calls tryToShootOnce 5 times.
 */
public class ShootEntireDrumCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })


    public ShootEntireDrumCommand(ShooterSubsystem shooterSubsystem) {

        addCommands(
            // new PrintCommand("shoot all")
            // .andThen(() -> shooterSubsystem.setShootAllCommand(this)),
            RobotContainer.getRotateDrumOneSectionCommand(),
            RobotContainer.getTryToShootCommand(),
            RobotContainer.getTryToShootCommand(),
            RobotContainer.getTryToShootCommand(),
            RobotContainer.getTryToShootCommand(),
            RobotContainer.getTryToShootCommand()
            );

        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}