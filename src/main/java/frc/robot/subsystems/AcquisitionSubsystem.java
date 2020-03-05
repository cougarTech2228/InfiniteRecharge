package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class AcquisitionSubsystem extends SubsystemBase {
    private WPI_TalonSRX m_acquisitionMotor;
    private Solenoid m_acquirerExtender;
    private boolean m_isRunningAcquirer;

    public AcquisitionSubsystem() {
        // You need to register the subsystem to get it's periodic
		// method to be called by the Scheduler
        //register();

        m_acquisitionMotor = new WPI_TalonSRX(Constants.ACQUISITION_MOTOR_CAN_ID);
        m_acquirerExtender = new Solenoid(Constants.PCM_CAN_ID, Constants.ACQUIRER_DEPLOY_PCM_PORT);

        m_acquisitionMotor.configPeakCurrentLimit(Constants.ACQUIRE_CURRENT_LIMIT);
		m_acquisitionMotor.configPeakCurrentDuration(Constants.ACQUIRE_CURRENT_DURATION);
		m_acquisitionMotor.configContinuousCurrentLimit(Constants.ACQUIRE_CONTINUOUS_CURRENT_LIMIT);
        m_acquisitionMotor.enableCurrentLimit(true);
        m_isRunningAcquirer = false;
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Is Running Acquirer", m_isRunningAcquirer);
    }
    
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

    /**
     * Starts the acquirer motor
     */
    public void startAcquirerMotor() {
        m_acquisitionMotor.set(Constants.ACQUIRER_MOTOR_SPEED);
        m_isRunningAcquirer = true;
    }

    /**
     * Stops the acquirer motor
     */
    public void stopAcquirerMotor() {
        m_acquisitionMotor.set(0);
        m_isRunningAcquirer = false;
    }

    /**
     * Deploy or/and start the motor based on the passed in value
     * 
     * @param deployAcquirer determines if the acquirer will be deployed
     * @param startMotor determines if the motor will be started
     */
    public void deployAndOrStartMotor(boolean deployAcquirer, boolean startMotor) {
        if(deployAcquirer) {
            deployAcquirer();
        }
        if(startMotor) {
            startAcquirerMotor();
        }
    }

    /**
     * Retract or/and stop the motor based on the passed in value
     * 
     * @param retractAcquirer determines if the acquirer will be retracted
     * @param stopMotor determines if the motor will be stoped
     */
    public void retractAndOrStopMotor(boolean retractAcquirer, boolean stopMotor) {
        if(retractAcquirer) {
            retractAcquirer();
        }
        if(stopMotor) {
            stopAcquirerMotor();
        }
    }

    /**
     * Runs the acquirer motor in reverse
     */
    public void reverseAcquirer() {
        m_acquisitionMotor.set(-Constants.ACQUIRER_MOTOR_SPEED);
    }
}