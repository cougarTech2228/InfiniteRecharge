/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.motors.Gains;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

	public static final int DIGITAL_IO_0 = 0;
	public static final int DIGITAL_IO_1 = 1;
	public static final int DIGITAL_IO_2 = 2;
	public static final int DIGITAL_IO_3 = 3;
	public static final int DIGITAL_IO_4 = 4;
	public static final int DIGITAL_IO_5 = 5;
	public static final int DIGITAL_IO_6 = 6;
	public static final int DIGITAL_IO_7 = 7;
	public static final int DIGITAL_IO_8 = 8;

	public static final int ANALOG_INPUT_0 = 0;
	public static final int ANALOG_INPUT_1 = 1;
	public static final int ANALOG_INPUT_2 = 2;
	public static final int ANALOG_INPUT_3 = 3;

	public static final int PCM_PORT_0 = 0;
	public static final int PCM_PORT_1 = 1;
	public static final int PCM_PORT_2 = 2;
	public static final int PCM_PORT_3 = 3;
	public static final int PCM_PORT_4 = 4;
	public static final int PCM_PORT_5 = 5;
	public static final int PCM_PORT_6 = 6;
	public static final int PCM_PORT_7 = 7;

	public static final int PWM_PIN_0 = 0;
	public static final int PWM_PIN_1 = 1;
	public static final int PWM_PIN_2 = 2;
	public static final int PWM_PIN_3 = 3;
	public static final int PWM_PIN_4 = 4;
	public static final int PWM_PIN_5 = 5;
	public static final int PWM_PIN_6 = 6;
	public static final int PWM_PIN_7 = 7;
	public static final int PWM_PIN_8 = 8;
	public static final int PWM_PIN_9 = 9;

	public static final int CONTROL_PANEL_MOTOR_CAN_ID = 4;
	public static final int RIGHT_FRONT_MOTOR_CAN_ID = 11;
	public static final int RIGHT_REAR_MOTOR_CAN_ID = 12;
	public static final int LEFT_FRONT_MOTOR_CAN_ID = 13;
	public static final int LEFT_REAR_MOTOR_CAN_ID = 14;
	public static final int ACQUISITION_MOTOR_CAN_ID = 3;

	public static final int NEO_SPARK_MAX_CAN_ID = 6;

	public static final int PIGEON_IMU_CAN_ID = 61;
	public static final int CANIFIER_CAN_ID = 62;

	public static final int LEFT_DISTANCE_SENSOR_ID = 1;
	public static final int RIGHT_DISTANCE_SENSOR_ID = 2;

	public static final int RELAY_PIN_0 = 0;
	public static final int RELAY_PIN_1 = 1;
	public static final int RELAY_PIN_2 = 2;
	public static final int RELAY_PIN_3 = 3;    

	public static final double VISION_TARGET_TOLERANCE_IN_INCHES = 1.0;

	public static final double ARCADE_DRIVE_TURN_DEADBAND = .03;

	public static final double XBOX_RUMBLE_TIME = 1.0;

	public static final double WHEEL_MOTOR_VELOCITY = 0.1;
	/**
	 * Number of joystick buttons to poll.
	 * 10 means buttons[1,9] are polled, which is actually 9 buttons.
	 */
	public final static int kNumButtonsPlusOne = 10;
	
	/**
	 * How many sensor units per rotation.
	 * Using CTRE Magnetic Encoder.
	 * @link https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
	 */
	public final static double kEncoderUnitsPerRevolution = 379.16;
	public final static double kWheelDiameterIN = 6.25;
	public final static double kMaxFTPerSecond = 12;
	public final static double kMaxUnitsPer_100ms = 8735.8; //kEncoderUnitsPerRevolution / kWheelDiameterIN / Math.PI * 12 * kMaxFTPerSecond / 10;

	public final static int kSensorUnitsPerRotation = 4096;
	
	/**
	 * Using the configSelectedFeedbackCoefficient() function, scale units to 3600 per rotation.
	 * This is nice as it keeps 0.1 degrees of resolution, and is fairly intuitive.
	 */
	public final static double kTurnTravelUnitsPerRotation = 3600;
	
	/**
	 * Empirically measure what the difference between encoders per 360'
	 * Drive the robot in clockwise rotations and measure the units per rotation.
	 * Drive the robot in counter clockwise rotations and measure the units per rotation.
	 * Take the average of the two.
	 */
	public final static int kEncoderUnitsPerRotation = 51711;
	/**
	 * Number of rotations to drive when performing Distance Closed Loop
	 */
	public final static double kRotationsToTravel = 6;
	
	/**
	 * This is a property of the Pigeon IMU, and should not be changed.
	 */
	public final static int kPigeonUnitsPerRotation = 8192;

	/**
	 * Set to zero to skip waiting for confirmation.
	 * Set to nonzero to wait and report to DS if action fails.
	 */
	public final static int kTimeoutMs = 30;

	/**
	 * Motor neutral dead-band, set to the minimum 0.1%.
	 */
	public final static double kNeutralDeadband = 0.001;
	
	/**
	 * PID Gains may have to be adjusted based on the responsiveness of control loop.
     * kF: 1023 represents output value to Talon at 100%, 6800 represents Velocity units at 100% output
     * Not all set of Gains are used in this project and may be removed as desired.
     * 
	 * 	                                    			  kP   kI   kD   kF               Iz    PeakOut */
	public final static Gains kGains_Distanc = new Gains( 0.1, 0.0,  0.0, 0.0,            100,  0.50 );
	public final static Gains kGains_Turning = new Gains( 2.0, 0.0,  4.0, 0.0,            200,  1.00 );
	public final static Gains kGains_Velocit = new Gains( 0.1, 0.0, 20.0, 1023.0/6800.0,  300,  0.50 );
	public final static Gains kGains_MotProf = new Gains( 1.0, 0.0,  0.0, 1023.0/6800.0,  400,  1.00 );
	
	/** ---- Flat constants, you should not need to change these ---- */
	/* We allow either a 0 or 1 when selecting an ordinal for remote devices [You can have up to 2 devices assigned remotely to a talon/victor] */
	public final static int REMOTE_0 = 0;
	public final static int REMOTE_1 = 1;
	/* We allow either a 0 or 1 when selecting a PID Index, where 0 is primary and 1 is auxiliary */
	public final static int PID_PRIMARY = 0;
	public final static int PID_TURN = 1;
	/* Firmware currently supports slots [0, 3] and can be used for either PID Set */
	public final static int SLOT_0 = 0;
	public final static int SLOT_1 = 1;
	public final static int SLOT_2 = 2;
	public final static int SLOT_3 = 3;
	/* ---- Named slots, used to clarify code ---- */
	public final static int kSlot_Distanc = SLOT_0;
	public final static int kSlot_Turning = SLOT_1;
	public final static int kSlot_Velocit = SLOT_2;
	public final static int kSlot_MotProf = SLOT_3;

	public static final int SHOOTER_CAN_ID = 15;
	public static final int DRUM_SPARK_PWM_ID = 1;
}
