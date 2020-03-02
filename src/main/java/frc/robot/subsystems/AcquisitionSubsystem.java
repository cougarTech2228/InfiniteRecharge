package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
//import frc.robot.commands.StartAcquirerCommand;

public class AcquisitionSubsystem extends SubsystemBase {
    //private TalonSRXMotor m_acquisitionMotor;
    private WPI_TalonSRX m_acquisitionMotor;
    private Solenoid m_acquirerExtender;
    //private StartAcquirerCommand m_startAcquirerCommandInstance;

    public AcquisitionSubsystem() {
        register();

        //m_acquisitionMotor = new TalonSRXMotor(Constants.ACQUISITION_MOTOR_CAN_ID);
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
     * Sets the acquirer speed, multiplies the -paramter by the max speed
     * 
     * @param speed The speed to which the acquirer will be run
     */
    // public void setAcquirerSpeed(double speed) {
    //     m_acquisitionMotor.set(speed);
    // }

    /**
     * Creates the instance of the startAcquirerCommand so it can be canceled later
     * 
     * @param startAcquirerCommand the instance to be set
     */
    // public void createStartAcquireCommandInstance(StartAcquirerCommand startAcquirerCommand) {
    //     m_startAcquirerCommandInstance = startAcquirerCommand;
    // }

    /**
     * Gets the acquisition motor 
     * 
     * @return the acquisitionMotor instance
     */
    // public WPI_TalonSRX getAcquisitionMotor() {
    //     return m_acquisitionMotor;
    // }

    /**
     * Stops the acquirer by canceling the startAcquirerCommand instance
     * Checks if the instance if null before ending it
     */
    // public void stopAcquirer() {
    //     if(m_startAcquirerCommandInstance != null) {
    //         m_startAcquirerCommandInstance.end(true);
    //     }
    // }
    
    /**
     * Deploys the acquirer by setting the solenoid to true
     */
    public void deployAcquirer() {
        m_acquirerExtender.set(true);
    }

    /**
     * Retracts the acquirer by setting the solenoid to false
     */
    public void retractAcquirer() {
        m_acquirerExtender.set(false);
    }

    public void startAcquirerMotor() {
        //m_acquisitionMotor.set(Constants.ACQUIRER_MOTOR_SPEED);
    }

    public void stopAcquirerMotor() {
        //m_acquisitionMotor.set(0);
    }
}