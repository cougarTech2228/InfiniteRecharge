package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.Constants;

/**
 * TrajectoryCommand
 * 
 *  */
public class TrajectoryCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public TrajectoryCommand(Trajectory trajectory, DrivebaseSubsystem drivebaseSubsystem) {
        addCommands(
            new RamseteCommand(trajectory, drivebaseSubsystem::getCurrentPose,
            new RamseteController(Constants.RAMSETE_B, Constants.RAMSETE_ZETA), Constants.DRIVE_KINEMATICS,
            drivebaseSubsystem::tankDriveVelocity, drivebaseSubsystem).andThen(() -> drivebaseSubsystem.stop())
        );
        
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }
}
