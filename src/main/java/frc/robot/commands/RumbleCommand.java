package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.OI;

/**
 * RumbleCommand
 * 
 * Rumbles the controller based on the amount of time passed in
 */
public class RumbleCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public RumbleCommand(double time) {
        
        addCommands (
            new PrintCommand("Rumble")
            .andThen(() -> OI.setXboxRumbleSpeed(Constants.XBOX_RUMBLE_SPEED)),
            new WaitCommand(time)
            .andThen(() -> OI.setXboxRumbleSpeed(0))
        );
    }
}