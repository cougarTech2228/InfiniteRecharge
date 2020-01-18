// /*----------------------------------------------------------------------------*/
// /* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
// /* Open Source Software - may be modified and shared by FRC teams. The code   */
// /* must be accompanied by the FIRST BSD license file in the root directory of */
// /* the project.                                                               */
// /*----------------------------------------------------------------------------*/

// package frc.robot;

// import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.CommandScheduler;

// /**
//  * The VM is configured to automatically run this class, and to call the functions corresponding to
//  * each mode, as described in the TimedRobot documentation. If you change the name of this class or
//  * the package after creating this project, you must also update the build.gradle file in the
//  * project.
//  */

// public class Robot extends TimedRobot {
//   private Command m_autonomousCommand;

//   private RobotContainer m_robotContainer;

//   /**
//    * This function is run when the robot is first started up and should be used for any
//    * initialization code.
//    */
//   @Override
//   public void robotInit() {
//     // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
//     // autonomous chooser on the dashboard.
//     m_robotContainer = new RobotContainer();
//   }

//   /**
//    * This function is called every robot packet, no matter the mode. Use this for items like
//    * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
//    *
//    * <p>This runs after the mode specific periodic functions, but before
//    * LiveWindow and SmartDashboard integrated updating.
//    */
//   @Override
//   public void robotPeriodic() {
//     // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
//     // commands, running already-scheduled commands, removing finished or interrupted commands,
//     // and running subsystem periodic() methods.  This must be called from the robot's periodic
//     // block in order for anything in the Command-based framework to work.
//     CommandScheduler.getInstance().run();
//   }

//   /**
//    * This function is called once each time the robot enters Disabled mode.
//    */
//   @Override
//   public void disabledInit() {
//   }

//   @Override
//   public void disabledPeriodic() {
//   }

//   /**
//    * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
//    */
//   @Override
//   public void autonomousInit() {
//     m_autonomousCommand = m_robotContainer.getAutonomousCommand();

//     // schedule the autonomous command (example)
//     if (m_autonomousCommand != null) {
//       m_autonomousCommand.schedule();
//     }
//   }

//   /**
//    * This function is called periodically during autonomous.
//    */
//   @Override
//   public void autonomousPeriodic() {
//   }

//   @Override
//   public void teleopInit() {
//     // This makes sure that the autonomous stops running when
//     // teleop starts running. If you want the autonomous to
//     // continue until interrupted by another command, remove
//     // this line or comment it out.
//     if (m_autonomousCommand != null) {
//       m_autonomousCommand.cancel();
//     }
//   }

//   /**
//    * This function is called periodically during operator control.
//    */
//   @Override
//   public void teleopPeriodic() {
//   }

//   @Override
//   public void testInit() {
//     // Cancels all running commands at the start of test mode.
//     CommandScheduler.getInstance().cancelAll();
//   }

//   /**
//    * This function is called periodically during test mode.
//    */
//   @Override
//   public void testPeriodic() {
//   }
// }
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.util.ToggleButton;
import frc.robot.util.ToggleButton.CommandState;
import frc.robot.commands.MethodCommand;
import frc.robot.motors.Gains;
import frc.robot.motors.TalonSRXMotor;
import frc.robot.shuffleboard.GainsBinder;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.BallDumpSubsystem;
import frc.robot.subsystems.BallDumpSubsystem.DumperState;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Robot extends TimedRobot {
/** Hardware */
	TalonSRX _rightMaster = new TalonSRX(11);
	TalonSRX _rightFollower = new TalonSRX(12);
	TalonSRX _leftMaster = new TalonSRX(13);
	TalonSRX _leftFollower = new TalonSRX(14);
	
	TalonSRXMotor _acquisitionMotor = new TalonSRXMotor(15);
	
	OI oi = new OI();
	BallDumpSubsystem dumper = new BallDumpSubsystem();
	AcquisitionSubsystem acquirer = new AcquisitionSubsystem();
	
	/** Latched values to detect on-press events for buttons */
	boolean[] _previousBtns = new boolean[Constants.kNumButtonsPlusOne];
	boolean[] _currentBtns = new boolean[Constants.kNumButtonsPlusOne];
	
	/** Tracking variables */
	boolean _firstCall = false;
	boolean _state = false;
	double _targetAngle = 0;

	@Override
	public void robotInit() {

		new ToggleButton()
		.setStartCommand(new InstantCommand(() -> System.out.println("Enabled!")))
		.addCommand(
			new SequentialCommandGroup(
				new MethodCommand(this::arcadeInit),
				new MethodCommand(this::arcadeDrive, true)
			),
			CommandState.Interruptible
		)
		.addCommand(
			new SequentialCommandGroup(
				new MethodCommand(this::straightInit),
				new MethodCommand(this::straightDrive, true)
			),
			CommandState.Interruptible
		)
		.setToggleButton(OI::getXboxLeftBumper)
		.setCycle(true);

		new ToggleButton(
			dumper.cmdSetPosition(DumperState.raised),
			dumper.cmdSetPosition(DumperState.lowered)
		)
		.setStartCommand(dumper.cmdSetPosition(DumperState.lowered))
		.setToggleButton(OI::getXboxRightBumper)
		.setCycle(true);

		new ToggleButton(
			acquirer.cmdSetClosedLoop(),
			null
		)
		.setDefaultState(CommandState.Interruptible)
		.setToggleButton(OI::getXboxRightTriggerPressed)
		.setCycle(true);
		
		OI.assignButton(OI::getXboxBButton)
		.whenPressed(this::zeroSensors);
}
@Override
public void robotPeriodic() {
	CommandScheduler.getInstance().run();
}
@Override
public void testPeriodic() {
	//System.out.println(OI.getXboxBButton() || OI.getXboxRightBumper());

}
	@Override
	public void testInit(){
		GainsBinder b = new GainsBinder("Attempt 2", _acquisitionMotor, new Gains(0,0,0,0,0,0));
		/* Factory Default all hardware to prevent unexpected behaviour */
		_leftMaster.configFactoryDefault();
		_rightMaster.configFactoryDefault();

		/* Disable all motor controllers */
		_rightMaster.set(ControlMode.PercentOutput, 0);
		_leftMaster.set(ControlMode.PercentOutput, 0);
		
		/* Set Neutral Mode */
		_leftMaster.setNeutralMode(NeutralMode.Brake);
		_rightMaster.setNeutralMode(NeutralMode.Brake);
		
		/** Closed loop configuration */
		
		/* Configure the drivetrain's left side Feedback Sensor as a Quadrature Encoder */
		_leftMaster.configSelectedFeedbackSensor(	FeedbackDevice.QuadEncoder,			// Local Feedback Source
													Constants.PID_PRIMARY,				// PID Slot for Source [0, 1]
													Constants.kTimeoutMs);				// Configuration Timeout

		/* Configure the left Talon's Selected Sensor to be a remote sensor for the right Talon */
		_rightMaster.configRemoteFeedbackFilter(_leftMaster.getDeviceID(),					// Device ID of Source
												RemoteSensorSource.TalonSRX_SelectedSensor,	// Remote Feedback Source
												Constants.REMOTE_0,							// Source number [0, 1]
												Constants.kTimeoutMs);						// Configuration Timeout
		
		/* Setup difference signal to be used for turn when performing Drive Straight with encoders */
		_rightMaster.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.RemoteSensor0, Constants.kTimeoutMs);	// Feedback Device of Remote Talon
		_rightMaster.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.QuadEncoder, Constants.kTimeoutMs);		// Quadrature Encoder of current Talon
		
		/* Difference term calculated by right Talon configured to be selected sensor of turn PID */
		_rightMaster.configSelectedFeedbackSensor(	FeedbackDevice.SensorDifference, 
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
		_rightMaster.configSelectedFeedbackCoefficient(	Constants.kTurnTravelUnitsPerRotation / Constants.kEncoderUnitsPerRotation,	// Coefficient
														Constants.PID_TURN, 														// PID Slot of Source
														Constants.kTimeoutMs);														// Configuration Timeout
		
		/* Configure output and sensor direction */
		_leftMaster.setInverted(false);
		_leftMaster.setSensorPhase(true);
		_rightMaster.setInverted(true);
		_rightMaster.setSensorPhase(true);
		
		_rightFollower.setInverted(true);
		_leftFollower.follow(_leftMaster);
		_rightFollower.follow(_rightMaster);
		
		/* Set status frame periods */
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, Constants.kTimeoutMs);
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, Constants.kTimeoutMs);
		_leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.kTimeoutMs);		//Used remotely by right Talon, speed up

		/* Configure neutral deadband */
		_rightMaster.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);
		_leftMaster.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);

		/* max out the peak output (for all modes).  However you can
		* limit the output of a given PID object with configClosedLoopPeakOutput().
		*/
		_leftMaster.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
		_leftMaster.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
		_rightMaster.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
		_rightMaster.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);

		/* FPID Gains for turn servo */
		_rightMaster.config_kP(Constants.kSlot_Turning, Constants.kGains_Turning.kP, Constants.kTimeoutMs);
		_rightMaster.config_kI(Constants.kSlot_Turning, Constants.kGains_Turning.kI, Constants.kTimeoutMs);
		_rightMaster.config_kD(Constants.kSlot_Turning, Constants.kGains_Turning.kD, Constants.kTimeoutMs);
		_rightMaster.config_kF(Constants.kSlot_Turning, Constants.kGains_Turning.kF, Constants.kTimeoutMs);
		_rightMaster.config_IntegralZone(Constants.kSlot_Turning, Constants.kGains_Turning.iZone, Constants.kTimeoutMs);
		_rightMaster.configClosedLoopPeakOutput(Constants.kSlot_Turning, Constants.kGains_Turning.peakOutput, Constants.kTimeoutMs);
		_rightMaster.configAllowableClosedloopError(Constants.kSlot_Turning, 0, Constants.kTimeoutMs);
			
		/* 1ms per loop.  PID loop can be slowed down if need be.
		* For example,
		* - if sensor updates are too slow
		* - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
		* - sensor movement is very slow causing the derivative error to be near zero.
		*/
		int closedLoopTimeMs = 1;
		_rightMaster.configClosedLoopPeriod(0, closedLoopTimeMs, Constants.kTimeoutMs);
		_rightMaster.configClosedLoopPeriod(1, closedLoopTimeMs, Constants.kTimeoutMs);

		/* configAuxPIDPolarity(boolean invert, int timeoutMs)
		* false means talon's local output is PID0 + PID1, and other side Talon is PID0 - PID1
		* true means talon's local output is PID0 - PID1, and other side Talon is PID0 + PID1
		*/
		_rightMaster.configAuxPIDPolarity(false, Constants.kTimeoutMs);

		/* Initialize */
		_firstCall = true;
		_state = false;

		zeroSensors();
	}
	public void arcadeInit() {
		System.out.println("This is arcade drive");
	}
	public void arcadeDrive() {
		double forward = OI.getXboxLeftJoystickY();
		double turn = OI.getXboxRightJoystickX();
		forward = Deadband(forward);
		turn = Deadband(turn) * 0.5;
		_leftMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
		_rightMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
	}
	public void straightInit() {
		System.out.println("This is straight drive with encoders");
		_rightMaster.selectProfileSlot(Constants.kSlot_Turning, Constants.PID_TURN);
		_targetAngle = _rightMaster.getSelectedSensorPosition(1);
	}
	public void straightDrive() {
		double forward = OI.getXboxLeftJoystickY();
		double turn = OI.getXboxRightJoystickX();
		forward = Deadband(forward);
		turn = Deadband(turn);
		_rightMaster.set(ControlMode.PercentOutput, forward, DemandType.AuxPID, _targetAngle);
		_leftMaster.follow(_rightMaster, FollowerType.AuxOutput1);
	}
	
	/* Zero all sensors used */
	void zeroSensors() {
		_leftMaster.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
		_rightMaster.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
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
}