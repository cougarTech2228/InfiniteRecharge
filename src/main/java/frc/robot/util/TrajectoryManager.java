// Ryan - You can move all the trajectory stuff from DrivebaseSubystem to this class.
//        In the constructor (or an init() method called from the constructor), you
//        can build all the trajectories. You can make public getters to retrieve
//        the trajectories when the commands are created. Instantiate this class from
//        RobotContainer somewhere before you instantiate anything that would use the
//        trajectories.

package frc.robot.util;

import java.util.List;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.Constants;


public class TrajectoryManager {

    private TrajectoryConfig m_config;
    private Trajectory m_centerTrajectory;
    private Trajectory m_leftTrajectory;
    private Trajectory m_rightTrajectory;

    public TrajectoryManager() {

        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(Constants.FEED_FORWARD,
                Constants.DRIVE_KINEMATICS, Constants.DIFFERENTIAL_DRIVE_CONSTRAINT_MAX_VOLTAGE);

        // Create m_config for all trajectories
        m_config = new TrajectoryConfig(Constants.kMaxSpeedMetersPerSecond,
                Constants.kMaxAccelerationMetersPerSecondSquared)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(Constants.DRIVE_KINEMATICS)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint).setEndVelocity(0.0);

        m_centerTrajectory = makeTrajectory("Center", 
        List.of( 
        //     new Translation2d(0.348, -1.692),
        //     new Translation2d(-0.217, -1.692),
        //     new Translation2d(-2.472, -1.692),
        //     new Translation2d(-3.281, -1.692),
        //     new Translation2d(-4.209, -1.692),
        //     new Translation2d(-5.303, -1.69),
        //     new Translation2d(-4.851, -1.155),
        //     new Translation2d(-4.149, -1.048),
        //     new Translation2d(-3.316, -1.036)
        // ),
        // new Pose2d(-2.234, -0.976, new Rotation2d(.25)));
            new Translation2d(0.86, -1.583), 
            new Translation2d(-5.184, -1.881)),
            // new Translation2d(-4.768, -0.726)),
            // new Translation2d(-1.424, -1.785)),
            // new Translation2d(-4.149, -0.429),
            // new Translation2d(-1.424, -0.512)),
        new Pose2d(-2.23, -0.9, new Rotation2d(.1))); /* 6.02 radians = 345 degrees */ 

        m_leftTrajectory = makeTrajectory("Left", 
        List.of(new Translation2d(-0.9, -1.3)),
        new Pose2d(-5.0, -1.6, new Rotation2d(3.14))); 

        m_rightTrajectory = makeTrajectory("Right", 
        List.of(new Translation2d(-0.9, -1.3)),
        new Pose2d(-5.0, -1.6, new Rotation2d(3.14))); 
    }

    public Trajectory makeTrajectory(String shuffleBoardName, List<Translation2d> pathList, Pose2d endPose) {
        // Right Trajectory Autonomous Command
        return TrajectoryGenerator.generateTrajectory(
                new Pose2d(0, 0, new Rotation2d(0)), // Assuming start pose is 0,0
                pathList, 
                endPose, 
                m_config);

    }

    public Trajectory getCenterTrajectory() {
        return m_centerTrajectory;
    }

    public Trajectory getLeftTrajectory() {
        return m_leftTrajectory;
    }

    public Trajectory getRightTrajectory() {
        return m_rightTrajectory;
    }

}