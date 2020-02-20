package frc.robot.subsystems;

import frc.robot.Constants;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU.CalibrationMode;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * NavigationSubsystem
 */
public class NavigationSubsystem extends SubsystemBase {

    private ADXRS450_Gyro m_gyro;
    
    private PigeonIMU m_pigeon; 

    private double[] m_yawPitchRoll = new double[3];

    public NavigationSubsystem() {

        m_gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
        //m_pigeon = new PigeonIMU(Constants.PIGEON_IMU_CAN_ID);
    }

    public void calibrateADXRS450() {
        m_gyro.calibrate();
    }

    public void calibratePigeon() {
        m_pigeon.enterCalibrationMode(CalibrationMode.BootTareGyroAccel);
    }

    public void zeroHeading() {
        m_gyro.reset();
    }

    public void resetYaw() {
        m_pigeon.setYaw(0);
    }
    
    /** 
     * @return double
     */
    public double getYaw() {
        m_pigeon.getYawPitchRoll(m_yawPitchRoll);
        return m_yawPitchRoll[0];
    }

    /** 
     * @return double
     */
    public double getPitch() {
        m_pigeon.getYawPitchRoll(m_yawPitchRoll);
        return m_yawPitchRoll[1];
    }

    /** 
     * @return double
     */
    public double getRoll() {
        m_pigeon.getYawPitchRoll(m_yawPitchRoll);
        return m_yawPitchRoll[2];
    }

    /**
     * Returns the heading of the robot.
     *
     * @return the robot's heading in degrees, from -180 to 180
     */
    public double getHeading() {
        // System.out.println("Angle: " + m_gyro.getAngle() + "Heading: " +
        // Math.IEEEremainder(m_gyro.getAngle(), 360) * (Constants.kGyroReversed ? -1.0
        // : 1.0));
        return Math.IEEEremainder(m_gyro.getAngle(), 360) * (Constants.kGyroReversed ? -1.0 : 1.0);
    }

    /**
     * Returns the turn rate of the robot.
     *
     * @return The turn rate of the robot, in degrees per second
     */
    public double getTurnRate() {
        return m_gyro.getRate() * (Constants.kGyroReversed ? -1.0 : 1.0);
    }
}

