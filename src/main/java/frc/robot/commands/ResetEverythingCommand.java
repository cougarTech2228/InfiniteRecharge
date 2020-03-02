package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.*;
import java.util.logging.Logger;

/**
 * ResetEverythingCommand
 * 
 * This command will reset everything incase of an emergency which can not be resolved 
 * normally. Everything that will be reset:
 * -Drum moved back to acquire position, and in the process resetting variables
 * -Drum array is emptied
 * -Reset control panel interrupt and set the boolean variable has fired rotate to false
 * -to be continued
 */
public class ResetEverythingCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private final Logger m_logger = Logger.getLogger(this.getClass().getName());

    public ResetEverythingCommand(StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem,
                                  GarminLidarSubsystem garminLidarSubsystem, DrivebaseSubsystem driveBaseSubsystem,
                                  AcquisitionSubsystem acquistionSubsystem, ClimberSubsystem climberSubsystem,
                                  ControlPanelSubsystem controlPanelSubsystem) 
    {
        addCommands(
            new PrintCommand("ALERT! ALERT! ROBOT MACHINE BROKE, RESETTING EVERYTHING!")
            .andThen(() -> m_logger.severe("Reset everything"))
            //-----------Shooting/Storage------
            .andThen(() -> shooterSubsystem.setIsShooting(false)),
            RobotContainer.getRepopulateArrayCommand()
            //----------Control Panel----------
            .andThen(() -> controlPanelSubsystem.relatchInterrupts())
            .andThen(() -> controlPanelSubsystem.setHasFiredRotate(false))
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}
