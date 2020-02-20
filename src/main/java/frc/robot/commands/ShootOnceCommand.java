package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * ShootOnceCommand
 * 
 * Shoots one power cell or moves to the next space. Resets back to acquire slot.
 */
public class ShootOnceCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public ShootOnceCommand(ShooterSubsystem shooterSubsystem) {
        System.out.println("ShootOnceCommand");
        addCommands(
            new PrintCommand("shoot once"),
            RobotContainer.getRotateDrumOneSectionCommand()
            .andThen(() -> shooterSubsystem.setIsShooting(false)),
            RobotContainer.getTryToShootCommand()
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}