package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.StorageSubsystem;

/**
 * RepopulateArrayCommand
 * 
 * This command repopulates the drum widget with the correct values
 * Stops in acquire position
 */
public class RepopulateArrayCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private StorageSubsystem m_storageSubsystem;

    public RepopulateArrayCommand(StorageSubsystem storageSubsystem) {
        m_storageSubsystem = storageSubsystem;
        addCommands(
            new PrintCommand("Repopulate Array Command")
            .andThen(() -> m_storageSubsystem.setIsRepopulating(true))
            .andThen(() -> m_storageSubsystem.resetDrum())
            .andThen(() -> populateIndex())
            .andThen(RobotContainer.getRotateDrumOneSectionCommand())
            .andThen(() -> populateIndex())
            .andThen(RobotContainer.getRotateDrumOneSectionCommand())
            .andThen(() -> populateIndex())
            .andThen(RobotContainer.getRotateDrumOneSectionCommand())
            .andThen(() -> populateIndex())
            .andThen(RobotContainer.getRotateDrumOneSectionCommand())
            .andThen(() -> populateIndex())
            .andThen(RobotContainer.getRotateDrumOneSectionCommand())
            .andThen(() -> m_storageSubsystem.setIsRepopulating(false))
        );
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
    /**
     * Checks the current slot if there is a ball in there, and sets the drum array accordingly.
     */
    public void populateIndex() {
        System.out.println("populateIndex");
        if(m_storageSubsystem.isAcquireBallOccupied()) {
            m_storageSubsystem.getBallArray().acquire();
        }
    }
}