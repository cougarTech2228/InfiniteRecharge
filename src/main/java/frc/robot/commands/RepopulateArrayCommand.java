package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.StorageSubsystem;

/**
 * ShootEntireDrumCommand
 * 
 * This command shoots the entire drum. It calls tryToShootOnce 5 times.
 */
public class RepopulateArrayCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private StorageSubsystem m_storageSubsystem;
    public RepopulateArrayCommand(StorageSubsystem storageSubsystem) {
        m_storageSubsystem = storageSubsystem;

        addCommands(
            new PrintCommand("Repopulate Array Command")
            .andThen(() -> m_storageSubsystem.setIsRepopulating(true)) // so the drum doesn't start indexing and stuff
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

    public void populateIndex() {
        System.out.println("populateIndex");
        if(m_storageSubsystem.isAcquireSlotOccupied()) {
            int currentDrumIndex = m_storageSubsystem.getDrumArrayIndex();
            m_storageSubsystem.setDrumArray(currentDrumIndex, true);
        }
    }
}