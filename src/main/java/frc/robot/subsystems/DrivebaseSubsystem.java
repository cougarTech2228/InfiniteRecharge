package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.OI;
import frc.robot.commands.MethodCommand;
import frc.robot.util.ShuffleboardAdapter;
/**
 *
 */
public class DrivebaseSubsystem extends SubsystemBase {

    private TalonSRX m_rightMaster = new TalonSRX(Constants.RIGHT_FRONT_MOTOR_CAN_ID);
    private TalonSRX m_rightFollower = new TalonSRX(Constants.RIGHT_REAR_MOTOR_CAN_ID);
    private TalonSRX m_leftMaster = new TalonSRX(Constants.LEFT_FRONT_MOTOR_CAN_ID);
    private TalonSRX m_leftFollower = new TalonSRX(Constants.LEFT_REAR_MOTOR_CAN_ID);
	private double maxSpeed = 1;
    private DriveState m_driveState = DriveState.Arcade;
    private int m_targetAngle;

    public enum DriveState {
        Arcade, DriveStraight
    }

    public DrivebaseSubsystem() {

        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        register();

        m_leftMaster.configFactoryDefault();
		m_rightMaster.configFactoryDefault();

		/* Disable all motor controllers */
		m_rightMaster.set(ControlMode.PercentOutput, 0);
		m_leftMaster.set(ControlMode.PercentOutput, 0);
		
		/* Set Neutral Mode */
		m_leftMaster.setNeutralMode(NeutralMode.Brake);
		m_rightMaster.setNeutralMode(NeutralMode.Brake);
		
		/** Closed loop configuration */
		
		/* Configure the drivetrain's left side Feedback Sensor as a Quadrature Encoder */
		m_leftMaster.configSelectedFeedbackSensor(	FeedbackDevice.QuadEncoder,			// Local Feedback Source
													Constants.PID_PRIMARY,				// PID Slot for Source [0, 1]
													Constants.kTimeoutMs);				// Configuration Timeout

		/* Configure the left Talon's Selected Sensor to be a remote sensor for the right Talon */
		m_rightMaster.configRemoteFeedbackFilter(m_leftMaster.getDeviceID(),					// Device ID of Source
												RemoteSensorSource.TalonSRX_SelectedSensor,	// Remote Feedback Source
												Constants.REMOTE_0,							// Source number [0, 1]
												Constants.kTimeoutMs);						// Configuration Timeout
		
		/* Setup difference signal to be used for turn when performing Drive Straight with encoders */
		m_rightMaster.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.RemoteSensor0, Constants.kTimeoutMs);	// Feedback Device of Remote Talon
		m_rightMaster.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.QuadEncoder, Constants.kTimeoutMs);		// Quadrature Encoder of current Talon
		
		/* Difference term calculated by right Talon configured to be selected sensor of turn PID */
		m_rightMaster.configSelectedFeedbackSensor(	FeedbackDevice.SensorDifference, 
													Constants.PID_TURN, 
													Constants.kTimeoutMs);
		
		/* Scale the Feedback Sensor using a coefficient */
		/**
		 * Heading units should be scaled to ~4000 per 360 deg, due to the following limitations...
		 * - Target param for aux PID1 is 18bits with a range of [-131072,+131072] units.
		 * - Target for aux PID1 in motion profile is 14bits with a range of [-8192,+8192] units.
		 *  ... so at 3600 units per 360', that ensures 0.1 degree precision in firmware closed-loop
		 *  and motion profile trajectory points can range +-2 rotations.
		 */
		m_rightMaster.configSelectedFeedbackCoefficient(	Constants.kTurnTravelUnitsPerRotation / Constants.kEncoderUnitsPerRotation,	// Coefficient
														Constants.PID_TURN, 														// PID Slot of Source
														Constants.kTimeoutMs);														// Configuration Timeout
		
		/* Configure output and sensor direction */
		m_leftMaster.setInverted(true);
		m_leftMaster.setSensorPhase(true);
		m_rightMaster.setInverted(false);
		m_rightMaster.setSensorPhase(true);
		
		m_leftFollower.setInverted(true);
		m_leftFollower.follow(m_leftMaster);
		m_rightFollower.follow(m_rightMaster);
		
		/* Set status frame periods */
		m_rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, Constants.kTimeoutMs);
		m_rightMaster.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, Constants.kTimeoutMs);
		m_leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.kTimeoutMs);		//Used remotely by right Talon, speed up

		/* Configure neutral deadband */
		m_rightMaster.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);
		m_leftMaster.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);

		/* max out the peak output (for all modes).  However you can
		* limit the output of a given PID object with configClosedLoopPeakOutput().
		*/
		m_leftMaster.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
		m_leftMaster.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
		m_rightMaster.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
		m_rightMaster.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);

		/* FPID Gains for turn servo */
		m_rightMaster.config_kP(Constants.kSlot_Turning, Constants.kGains_Turning.kP, Constants.kTimeoutMs);
		m_rightMaster.config_kI(Constants.kSlot_Turning, Constants.kGains_Turning.kI, Constants.kTimeoutMs);
		m_rightMaster.config_kD(Constants.kSlot_Turning, Constants.kGains_Turning.kD, Constants.kTimeoutMs);
		m_rightMaster.config_kF(Constants.kSlot_Turning, Constants.kGains_Turning.kF, Constants.kTimeoutMs);
		m_rightMaster.config_IntegralZone(Constants.kSlot_Turning, Constants.kGains_Turning.iZone, Constants.kTimeoutMs);
		m_rightMaster.configClosedLoopPeakOutput(Constants.kSlot_Turning, Constants.kGains_Turning.peakOutput, Constants.kTimeoutMs);
		m_rightMaster.configAllowableClosedloopError(Constants.kSlot_Turning, 0, Constants.kTimeoutMs);
			
		/* 1ms per loop.  PID loop can be slowed down if need be.
		* For example,
		* - if sensor updates are too slow
		* - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
		* - sensor movement is very slow causing the derivative error to be near zero.
		*/
		int closedLoopTimeMs = 1;
		m_rightMaster.configClosedLoopPeriod(0, closedLoopTimeMs, Constants.kTimeoutMs);
		m_rightMaster.configClosedLoopPeriod(1, closedLoopTimeMs, Constants.kTimeoutMs);

		/* configAuxPIDPolarity(boolean invert, int timeoutMs)
		* false means talon's local output is PID0 + PID1, and other side Talon is PID0 - PID1
		* true means talon's local output is PID0 - PID1, and other side Talon is PID0 + PID1
		*/
		m_rightMaster.configAuxPIDPolarity(false, Constants.kTimeoutMs);

		new ShuffleboardAdapter("Drive Base")
			.addDoubleText("Right kP", 0, value -> {m_rightMaster.config_kP(0, value); })
			.addDoubleText("Right kI", 0, value -> {m_rightMaster.config_kF(0, value); })
			.addDoubleText("Right kD", 0, value -> {m_rightMaster.config_kI(0, value); })
			.addDoubleText("Right kF", 0, value -> {m_rightMaster.config_kF(0, value); })
			.addDoubleText("Right kIZone", 0, value -> {m_rightMaster.config_IntegralZone(0, (int)value); })
			.addDoubleText("RightTargetVel", 0, value -> rightTargetVelocity = value)
			.addDouble("Right Velocity Error", 0, () -> m_rightMaster.getClosedLoopError())
			.addDouble("Right Velocity", 0, () -> m_rightMaster.getSelectedSensorVelocity())
			.addDouble("Right Current", 0, () -> m_rightMaster.getSupplyCurrent())
			.addDoubleText("Right Percent Power", 0, value -> rightPercentOutput = value)

			.addDoubleText("Left kP", 0, value -> {m_leftMaster.config_kP(0, value); })
			.addDoubleText("Left kI", 0, value -> {m_leftMaster.config_kF(0, value); })
			.addDoubleText("Left kD", 0, value -> {m_leftMaster.config_kI(0, value); })
			.addDoubleText("Left kF", 0, value -> {m_leftMaster.config_kF(0, value); })
			.addDoubleText("Left kIZone", 0, value -> {m_leftMaster.config_IntegralZone(0, (int)value); })
			.addDoubleText("LeftTargetVel", 0, value -> leftTargetVelocity = value)
			.addDouble("Left Velocity Error", 0, () -> m_leftMaster.getClosedLoopError())
			.addDouble("Left Velocity", 0, () -> m_leftMaster.getSelectedSensorVelocity())
			.addDouble("Left Current", 0, () -> m_leftMaster.getSupplyCurrent())
			.addDoubleText("Left Percent Power", 0, value -> leftPercentOutput = value);
	}
	private double rightTargetVelocity = 0;
	private double leftTargetVelocity = 0;
	private double rightPercentOutput = 0;
	private double leftPercentOutput = 0;

    @Override
    public void periodic() {
		/*
		if(rightPercentOutput != 0) {
			m_rightMaster.set(ControlMode.PercentOutput, rightPercentOutput);
			System.out.println("Set speed to: " + rightPercentOutput);
		}
		else if(rightTargetVelocity != 0) {
			m_rightMaster.set(ControlMode.Velocity, rightTargetVelocity);
			System.out.println("Set velocity to: " + rightTargetVelocity);
		}
		else {
			m_rightMaster.set(ControlMode.PercentOutput, 0);
		}

		if(leftPercentOutput != 0) {
			m_leftMaster.set(ControlMode.PercentOutput, leftPercentOutput);
		}
		else if(leftTargetVelocity != 0) {
			m_leftMaster.set(ControlMode.Velocity, leftTargetVelocity);
		}
		else {
			m_leftMaster.set(ControlMode.PercentOutput, 0);
		}*/
    }
	public Command cmdSetMaxSpeed(double percentOfMax) {
		return new MethodCommand(() -> maxSpeed = percentOfMax);
	}
    public Command cmdUseArcadeDrive() {
        return new SequentialCommandGroup(
            new MethodCommand(this::arcadeInit),
            new MethodCommand(this::arcadeDrive, true)
        );
    }

    public Command cmdUseStraightDrive() {
        return new SequentialCommandGroup(
            new MethodCommand(this::straightInit),
            new MethodCommand(this::straightDrive, true)
        );
    }

    private void arcadeInit() {
		System.out.println("This is arcade drive");
	}
	private void arcadeDrive() {
		double forward = -OI.getXboxLeftJoystickY();
		double turn = OI.getXboxRightJoystickX();
		forward = Deadband(forward) * maxSpeed;
		turn = Deadband(turn) * 0.5 * maxSpeed;
		m_leftMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
		m_rightMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
	}
	private void straightInit() {
		System.out.println("This is straight drive with encoders");
		m_rightMaster.selectProfileSlot(Constants.kSlot_Turning, Constants.PID_TURN);
		m_targetAngle = m_rightMaster.getSelectedSensorPosition(1);
	}
	private void straightDrive() {
		double forward = OI.getXboxLeftJoystickY();
		double turn = OI.getXboxRightJoystickX();
		forward = Deadband(forward) * maxSpeed;
		turn = Deadband(turn) * maxSpeed;
		m_rightMaster.set(ControlMode.PercentOutput, forward, DemandType.AuxPID, m_targetAngle);
		m_leftMaster.follow(m_rightMaster, FollowerType.AuxOutput1);
	}
	
	/* Zero all sensors used */
	private void zeroSensors() {
		m_leftMaster.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
		m_rightMaster.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
		System.out.println("[Quadrature Encoders] All sensors are zeroed.\n");
	}
	
	/** Deadband 5 percent, used on the gamepad */
	double Deadband(double value) {
		/* Upper deadband */
		if (value >= 0.1) 
			return value;
		
		/* Lower deadband */
		if (value <= -0.1)
			return value;
		
		/* Outside deadband */
		return 0;
	}

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}
