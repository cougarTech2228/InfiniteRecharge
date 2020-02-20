package frc.robot.commands;

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
            RobotContainer.getRotateDrumOneSectionCommand(),
            RobotContainer.getTryToShootCommand(),
            new WaitCommand(Constants.timeBetweenShots),
            RobotContainer.getTryToShootCommand(),
            new WaitCommand(Constants.timeBetweenShots),
            RobotContainer.getTryToShootCommand(),
            new WaitCommand(Constants.timeBetweenShots),  
            RobotContainer.getTryToShootCommand(),
            new WaitCommand(Constants.timeBetweenShots),
            RobotContainer.getTryToShootCommand()
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}