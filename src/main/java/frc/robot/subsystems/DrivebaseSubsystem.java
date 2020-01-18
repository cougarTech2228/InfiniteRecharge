package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.OI;

/**
 *
 */
public class DrivebaseSubsystem extends SubsystemBase {

    private WPI_TalonSRX m_frontRightTalonSRX;
    private WPI_TalonSRX m_frontLeftTalonSRX;
    private WPI_TalonSRX m_rearRightTalonSRX;
    private WPI_TalonSRX m_rearLeftTalonSRX;

    private SpeedControllerGroup m_leftSpeedControllerGroup;
    private SpeedControllerGroup m_rightSpeedControllerGroup;

    private DifferentialDrive m_differentialDrive;

    public DrivebaseSubsystem() {

        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        CommandScheduler.getInstance().registerSubsystem(this);

        m_frontRightTalonSRX = new WPI_TalonSRX(Constants.RIGHT_FRONT_MOTOR_CAN_ID);
        m_frontLeftTalonSRX = new WPI_TalonSRX(Constants.LEFT_FRONT_MOTOR_CAN_ID);
        m_rearRightTalonSRX = new WPI_TalonSRX(Constants.RIGHT_REAR_MOTOR_CAN_ID);
        m_rearLeftTalonSRX = new WPI_TalonSRX(Constants.LEFT_REAR_MOTOR_CAN_ID);

        m_leftSpeedControllerGroup = new SpeedControllerGroup(m_frontLeftTalonSRX, m_rearLeftTalonSRX);
        m_rightSpeedControllerGroup = new SpeedControllerGroup(m_frontRightTalonSRX, m_rearRightTalonSRX);

        m_differentialDrive = new DifferentialDrive(m_leftSpeedControllerGroup, m_rightSpeedControllerGroup);

        m_differentialDrive.setSafetyEnabled(true);
        m_differentialDrive.setExpiration(0.1);
        m_differentialDrive.setMaxOutput(1.0);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        double fwdRevValue = OI.getXboxLeftJoystickY(); // -1.0 full forward, 1.0 full reverse
        double turnValue = OI.getXboxRightJoystickX();  // -1.0 counter clockwise, 1.0 clockwise

        // Was seeing a value of -0.023375 on the Right Joystick X value coming
        // from one of the Xbox Controllers when it was at it's 'zero' position.
        if ((turnValue < Constants.ARCADE_DRIVE_TURN_DEADBAND) && (turnValue > -Constants.ARCADE_DRIVE_TURN_DEADBAND)) {
            turnValue = 0.0;
        }

        //System.out.println("fwdRevValue = " + fwdRevValue + " turnValue = " + turnValue);

        m_differentialDrive.arcadeDrive(fwdRevValue, -turnValue); 
    }
}