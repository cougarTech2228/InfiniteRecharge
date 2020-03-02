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
import frc.robot.RobotContainer;

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
	private RamseteController m_ramseteController;

	private Pose2d m_savedPose;

	private boolean m_isAutonomous = true;

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

		/* Set open and closed loop values */
		m_leftMaster.configOpenloopRamp(0.25);
		m_leftMaster.configClosedloopRamp(0);

		m_rightMaster.configOpenloopRamp(0.25);
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

		m_rightMaster.configPeakCurrentLimit(Constants.DRIVE_CURRENT_LIMIT);
		m_rightFollower.configPeakCurrentLimit(Constants.DRIVE_CURRENT_LIMIT);
		m_rightMaster.configPeakCurrentDuration(Constants.DRIVE_CURRENT_DURATION);
		m_rightFollower.configPeakCurrentDuration(Constants.DRIVE_CURRENT_DURATION);
		m_rightMaster.configContinuousCurrentLimit(Constants.DRIVE_CONTINUOUS_CURRENT_LIMIT);
		m_rightFollower.configContinuousCurrentLimit(Constants.DRIVE_CONTINUOUS_CURRENT_LIMIT);
		m_rightMaster.enableCurrentLimit(true);
		m_rightFollower.enableCurrentLimit(true);
		// TODO can we set these back to true?

		m_leftMaster.configPeakCurrentLimit(Constants.DRIVE_CURRENT_LIMIT);
		m_leftFollower.configPeakCurrentLimit(Constants.DRIVE_CURRENT_LIMIT);
		m_leftMaster.configPeakCurrentDuration(Constants.DRIVE_CURRENT_DURATION);
		m_leftFollower.configPeakCurrentDuration(Constants.DRIVE_CURRENT_DURATION);
		m_leftMaster.configContinuousCurrentLimit(Constants.DRIVE_CONTINUOUS_CURRENT_LIMIT);
		m_leftFollower.configContinuousCurrentLimit(Constants.DRIVE_CONTINUOUS_CURRENT_LIMIT);
		m_leftMaster.enableCurrentLimit(true);
		m_leftFollower.enableCurrentLimit(true);
		// TODO can we set these back to true?

		// m_differentialDrive = new DifferentialDrive(m_leftMaster, m_rightMaster);
		// m_differentialDrive.setRightSideInverted(true);

		// zeroSensors();

		// RobotContainer.getNavigationSubsystem().resetYaw();

		// m_odometry = new DifferentialDriveOdometry(RobotContainer.getNavigationSubsystem().getHeading());

		// resetOdometry();

		// m_ramseteController = new RamseteController(Constants.RAMSETE_B, Constants.RAMSETE_ZETA);
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
			System.out.println("Encoders aren't available");
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

		/* Inside deadband */
		return 0.0;
	}

	private void arcadeDrive() {
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
			//System.out.println("update odometry");
			 //m_odometry.update(RobotContainer.getNavigationSubsystem().getHeading(),
			 		//edgesToMeters(getLeftEncoderPosition()), edgesToMeters(getRightEncoderPosition()));
		} else {
			 arcadeDrive();
		}

		//m_differentialDrive.feed();
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
		leftVelocity *= -1;
		rightVelocity *= -1;

		var leftAccel = (leftVelocity - edgesPerDecisecToMetersPerSec(m_leftMaster.getSelectedSensorVelocity())) / .02;
		var rightAccel = (rightVelocity - edgesPerDecisecToMetersPerSec(m_rightMaster.getSelectedSensorVelocity()))
				/ .02;

		var leftFeedForwardVolts = Constants.FEED_FORWARD.calculate(leftVelocity, leftAccel);
		var rightFeedForwardVolts = Constants.FEED_FORWARD.calculate(rightVelocity, rightAccel);

		m_leftMaster.set(ControlMode.Velocity, metersPerSecToEdgesPerDecisec(leftVelocity),
				DemandType.ArbitraryFeedForward, leftFeedForwardVolts / 12);
		m_rightMaster.set(ControlMode.Velocity, metersPerSecToEdgesPerDecisec(rightVelocity),
				DemandType.ArbitraryFeedForward, rightFeedForwardVolts / 12);
	}

	/**
	 * Sets the drivetrain to zero velocity and rotation.
	 */
	public void stop() {
		tankDriveVelocity(0, 0);
		setArcadeDrive(0, 0);
	}

	/**
	 * Resets the current pose to 0, 0, 0° and resets the saved pose
	 */
	public void resetOdometry() {
		zeroSensors();
		m_savedPose = new Pose2d(0, 0, Rotation2d.fromDegrees(0));
		m_odometry.resetPosition(m_savedPose, RobotContainer.getNavigationSubsystem().getHeading());
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

	public void setArcadeDrive(double forward, double turn) {
		forward = deadband(forward);
		turn = (deadband(turn) * 0.5);
		m_leftMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
		m_rightMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
	}

	public void setAutonomous(boolean isAutonomous) {
		m_isAutonomous = isAutonomous;
	}

	public RamseteController getRamseteController() {
		return m_ramseteController;
	}
}
