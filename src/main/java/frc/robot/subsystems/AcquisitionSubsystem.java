package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class AcquisitionSubsystem extends SubsystemBase {
    private WPI_TalonSRX m_acquisitionMotor;
    private Solenoid m_acquirerExtender;

    public AcquisitionSubsystem() {
        register();

        m_acquisitionMotor = new WPI_TalonSRX(Constants.ACQUISITION_MOTOR_CAN_ID);
        m_acquirerExtender = new Solenoid(Constants.PCM_CAN_ID, Constants.ACQUIRER_DEPLOY_PCM_PORT);

        m_acquisitionMotor.configPeakCurrentLimit(Constants.ACQUIRE_CURRENT_LIMIT);
		m_acquisitionMotor.configPeakCurrentDuration(Constants.ACQUIRE_CURRENT_DURATION);
		m_acquisitionMotor.configContinuousCurrentLimit(Constants.ACQUIRE_CONTINUOUS_CURRENT_LIMIT);
		m_acquisitionMotor.enableCurrentLimit(true);
    }

    @Override
    public void periodic() {}
    
    /**
     * Deploys the acquirer by setting the solenoid to true
     */
    public void deployAcquirer() {
        //m_acquirerExtender.set(true);
    }

    /**
     * Retracts the acquirer by setting the solenoid to false
     */
    public void retractAcquirer() {
        //m_acquirerExtender.set(false);
    }

    /**
     * Starts the acquirer motor
     */
    public void startAcquirerMotor() {
        m_acquisitionMotor.set(Constants.ACQUIRER_MOTOR_SPEED);
    }

    /**
     * Stops the acquirer motor
     */
    public void stopAcquirerMotor() {
        m_acquisitionMotor.set(0);
    }
}