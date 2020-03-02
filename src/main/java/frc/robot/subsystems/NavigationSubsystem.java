package frc.robot.subsystems;

import frc.robot.Constants;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU.CalibrationMode;

import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * NavigationSubsystem
 */
public class NavigationSubsystem extends SubsystemBase {
    
    private PigeonIMU m_pigeon; 

    private double[] m_yawPitchRoll = new double[3];

    public NavigationSubsystem() {
        m_pigeon = new PigeonIMU(Constants.PIGEON_IMU_CAN_ID);
    }

    public void calibratePigeon() {
        m_pigeon.enterCalibrationMode(CalibrationMode.BootTareGyroAccel);
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
    public Rotation2d getHeading() {
        // TODO - need to see if this is giving us correct heading values for the Ramsete Controller
        System.out.println("Heading: " + Math.IEEEremainder(getYaw(), 360.0d));
        return Rotation2d.fromDegrees(Math.IEEEremainder(getYaw(), 360.0d));
    }
}

