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
           new PrintCommand("Bopping...?")
           .andThen(() -> {
               if(shooterSubsystem.getIsRunningShooterMotor()) {
                    new SequentialCommandGroup(
                        new PrintCommand("Bopping ...")
                        .andThen(() -> shooterSubsystem.raiseBopper()),
                        new WaitCommand(Constants.BOPPER_WAIT_TIME)
                        .andThen(() -> shooterSubsystem.lowerBopper())
                    ).schedule();
               } else {
                   System.out.println("Can't bop, shooter motor isn't on");
               }
           })
        );
        // Use addRequirements() here to declare subsystem dependencies.
        // addRequirements(shooterSubsystem);
    }
}
