package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
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
        //System.out.println("ShootEntireDrumCommand");
        addCommands(
            new PrintCommand("Shoot entire drum0"),
            RobotContainer.getRotateDrumOneSectionCommand(),
            new PrintCommand("Shoot 1"),
            RobotContainer.getTryToShootCommand(),
            new WaitCommand(Constants.timeBetweenShots),
            new PrintCommand("Shoot 2"),
            RobotContainer.getTryToShootCommand(),
            new WaitCommand(Constants.timeBetweenShots),
            new PrintCommand("Shoot 3"),
            RobotContainer.getTryToShootCommand(),
            new WaitCommand(Constants.timeBetweenShots),  
            new PrintCommand("Shoot 4"),
            RobotContainer.getTryToShootCommand(),
            new WaitCommand(Constants.timeBetweenShots),
            new PrintCommand("Shoot 5"),
            RobotContainer.getTryToShootCommand()
            .andThen(() -> shooterSubsystem.setIsShooting(false))
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}