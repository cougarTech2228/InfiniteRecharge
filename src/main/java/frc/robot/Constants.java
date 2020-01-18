/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
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
}
