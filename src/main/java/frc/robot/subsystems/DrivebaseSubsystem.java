package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.OI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

/**
 * DrivebaseSubsystem
 */
public class DrivebaseSubsystem extends SubsystemBase {

	private WPI_TalonSRX m_rightMaster = new WPI_TalonSRX(Constants.RIGHT_FRONT_MOTOR_CAN_ID);
	private WPI_TalonSRX m_rightFollower = new WPI_TalonSRX(Constants.RIGHT_REAR_MOTOR_CAN_ID);
	private WPI_TalonSRX m_leftMaster = new WPI_TalonSRX(Constants.LEFT_FRONT_MOTOR_CAN_ID);
	private WPI_TalonSRX m_leftFollower = new WPI_TalonSRX(Constants.LEFT_REAR_MOTOR_CAN_ID);

	private DifferentialDrive m_differentialDrive;
	private boolean m_encodersAreAvailable;
	private DifferentialDriveOdometry m_odometry;

	private Pose2d m_savedPose;

	private boolean m_isAutonomous = false;

	public DrivebaseSubsystem() {

		// You need to register the subsystem to get it's periodic
		// method to be called by the Scheduler
		register();

		m_leftMaster.configFactoryDefault();
		m_rightMaster.configFactoryDefault();

		/* Disable all motor controllers */
		m_rightMaster.set(ControlMode.Velocity, 0);
		m_leftMaster.set(ControlMode.Velocity, 0);

		/* Set Neutral Mode */
		m_leftMaster.setNeutralMode(NeutralMode.Brake);
		m_rightMaster.setNeutralMode(NeutralMode.Brake);

		enableEncoders();

		/*
		 * Setup difference signal to be used for turn when performing Drive Straight
		 * with encoders
		 */
		// Feedback Device of Remote Talon
		m_rightMaster.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.RemoteSensor0, Constants.kTimeoutMs);

		// Quadrature Encoder of current Talon
		m_rightMaster.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.QuadEncoder, Constants.kTimeoutMs);

		/*
		 * Difference term calculated by right Talon configured to be selected sensor of
		 * turn PID
		 */
		m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorDifference, Constants.PID_TURN,
				Constants.kTimeoutMs);

		/* Scale the Feedback Sensor using a coefficient */
		/**
		 * Heading units should be scaled to ~4000 per 360 deg, due to the following
		 * limitations... - Target param for aux PID1 is 18bits with a range of
		 * [-131072,+131072] units. - Target for aux PID1 in motion profile is 14bits
		 * with a range of [-8192,+8192] units. ... so at 3600 units per 360', that
		 * ensures 0.1 degree precision in firmware closed-loop and motion profile
		 * trajectory points can range +-2 rotations.
		 */
		m_rightMaster.configSelectedFeedbackCoefficient(
				Constants.kTurnTravelUnitsPerRotation / Constants.kEncoderUnitsPerRotation, // Coefficient
				Constants.PID_TURN, // PID Slot of Source
				Constants.kTimeoutMs); // Configuration Timeout

		/* Set open and closed loop values */
		m_leftMaster.configOpenloopRamp(0.5);
		m_leftMaster.configClosedloopRamp(0);

		m_rightMaster.configOpenloopRamp(0.5);
		m_rightMaster.configClosedloopRamp(0);

		/* Configure output and sensor direction */
		m_leftMaster.setSensorPhase(false);
		m_leftMaster.setInverted(true);

		m_leftFollower.setInverted(true);
		m_leftFollower.follow(m_leftMaster);

		m_rightMaster.setSensorPhase(false);
		m_rightMaster.setInverted(false);

		m_rightFollower.setInverted(false);
		m_rightFollower.follow(m_rightMaster);

		m_differentialDrive = new DifferentialDrive(m_leftMaster, m_rightMaster);
		m_differentialDrive.setRightSideInverted(true);

		zeroSensors();

		RobotContainer.getNavigationSubsystem().calibrateADXRS450();
		RobotContainer.getNavigationSubsystem().zeroHeading();

		m_odometry = new DifferentialDriveOdometry(
				Rotation2d.fromDegrees(RobotContainer.getNavigationSubsystem().getHeading()));

		resetOdometry();
	}

	/* Zero all sensors used */
	private void zeroSensors() {
		m_leftMaster.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
		m_rightMaster.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
		System.out.println("[Quadrature Encoders] All sensors are zeroed.\n");
	}

	private void enableEncoders() {
		m_encodersAreAvailable = m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
				Constants.PID_PRIMARY, Constants.kTimeoutMs) == ErrorCode.OK
				& m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.REMOTE_0,
						Constants.kTimeoutMs) == ErrorCode.OK;
		if (!m_encodersAreAvailable) {
			DriverStation.reportError("Failed to configure Drivetrain encoders!!", false);
		}
	}
	
	/** Deadband 5 percent, used on the gamepad */
	private double deadband(final double value) {
		/* Upper deadband */
		if (value >= 0.1)
			return value;

		/* Lower deadband */
		if (value <= -0.1)
			return value;

		/* Outside deadband */
		return 0;
	}

	private void arcadeDrive() {
		m_differentialDrive.feed();

		double forward = OI.getXboxLeftJoystickY();
		double turn = OI.getXboxRightJoystickX();
		forward = deadband(forward);
		turn = deadband(turn) * 0.5;
		m_leftMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
		m_rightMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
	}

	@Override
	public void periodic() {
		if (m_isAutonomous) {
			m_odometry.update(Rotation2d.fromDegrees(RobotContainer.getNavigationSubsystem().getHeading()),
					edgesToMeters(getLeftEncoderPosition()), edgesToMeters(getRightEncoderPosition()));
		} else {
			arcadeDrive();
		}
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	/**
	 * Controls the left and right side of the drive using Talon SRX closed-loop
	 * velocity.
	 * 
	 * @param leftVelocity  left velocity in meters per second
	 * @param rightVelocity right velocity in meters per second
	 */
	public void tankDriveVelocity(double leftVelocity, double rightVelocity) {

		var leftAccel = (leftVelocity - edgesPerDecisecToMetersPerSec(m_leftMaster.getSelectedSensorVelocity())) / .02;
		var rightAccel = (rightVelocity - edgesPerDecisecToMetersPerSec(m_rightMaster.getSelectedSensorVelocity()))
				/ .02;
		var leftFeedForwardVolts = Constants.FEED_FORWARD.calculate(leftVelocity, leftAccel);
		var rightFeedForwardVolts = Constants.FEED_FORWARD.calculate(rightVelocity, rightAccel);

		m_leftMaster.set(ControlMode.Velocity, metersPerSecToEdgesPerDecisec(leftVelocity),
				DemandType.ArbitraryFeedForward, leftFeedForwardVolts / 12);
		m_rightMaster.set(ControlMode.Velocity, metersPerSecToEdgesPerDecisec(rightVelocity),
				DemandType.ArbitraryFeedForward, rightFeedForwardVolts / 12);
		m_differentialDrive.feed();
	}

	/**
	 * Sets the drivetrain to zero velocity and rotation.
	 */
	public void stop() {
		tankDriveVelocity(0, 0);
	}

	/**
	 * Resets the current pose to 0, 0, 0° and resets the saved pose
	 */
	public void resetOdometry() {
		zeroSensors();
		m_savedPose = new Pose2d(0, 0, Rotation2d.fromDegrees(0));
		m_odometry.resetPosition(m_savedPose,
				Rotation2d.fromDegrees(RobotContainer.getNavigationSubsystem().getHeading()));
	}

	/**
	 * returns left encoder position
	 * 
	 * @return left encoder position
	 */
	public int getLeftEncoderPosition() {
		return m_leftMaster.getSelectedSensorPosition(0);
	}

	/**
	 * returns right encoder position
	 * 
	 * @return right encoder position
	 */
	public int getRightEncoderPosition() {
		return m_rightMaster.getSelectedSensorPosition(0);
	}

	/**
	 * @return boolean
	 */
	public boolean areEncodersAvailable() {
		return m_encodersAreAvailable;
	}

	/**
	 * Converts from encoder edges to meters.
	 * 
	 * @param steps encoder edges to convert
	 * @return meters
	 */
	public static double edgesToMeters(int steps) {
		return (Constants.WHEEL_CIRCUMFERENCE_METERS / Constants.EDGES_PER_ROTATION) * steps;
	}

	/**
	 * Converts from encoder edges per 100 milliseconds to meters per second.
	 * 
	 * @param stepsPerDecisec edges per decisecond
	 * @return meters per second
	 */
	public static double edgesPerDecisecToMetersPerSec(int stepsPerDecisec) {
		return edgesToMeters(stepsPerDecisec * 10);
	}

	/**
	 * Converts from meters to encoder edges.
	 * 
	 * @param meters meters
	 * @return encoder edges
	 */
	public static double metersToEdges(double meters) {
		return (meters / Constants.WHEEL_CIRCUMFERENCE_METERS) * Constants.EDGES_PER_ROTATION;
	}

	/**
	 * Converts from meters per second to encoder edges per 100 milliseconds.
	 * 
	 * @param metersPerSec meters per second
	 * @return encoder edges per decisecond
	 */
	public static double metersPerSecToEdgesPerDecisec(double metersPerSec) {
		return metersToEdges(metersPerSec) * .1d;
	}

	/**
	 * @return Pose2d
	 */
	public Pose2d getCurrentPose() {
		return m_odometry.getPoseMeters();
	}

	/**
	 * Creates a command to follow a Trajectory on the drivetrain.
	 * 
	 * @param trajectory trajectory to follow
	 * @return command that will run the trajectory
	 */
	public Command createCommandForTrajectory(Trajectory trajectory) {
		return new ConditionalCommand(
				new RamseteCommand(trajectory, this::getCurrentPose,
						new RamseteController(Constants.RAMSETE_B, Constants.RAMSETE_ZETA), Constants.DRIVE_KINEMATICS,
						this::tankDriveVelocity, this).andThen(this::stop, this),
				new PrintCommand("Cannot run trajectory because encoders are unavailable!!"),
				this::areEncodersAvailable);
	}

	public void setArcadeDrive(double forward, double turn) {
		forward = deadband(forward) * -1;
		turn = (deadband(turn) * 0.5) * -1;
		m_leftMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
		m_rightMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
	}

	public void setAutonomous(boolean isAutonomous) {
		m_isAutonomous = isAutonomous;
	}
}
