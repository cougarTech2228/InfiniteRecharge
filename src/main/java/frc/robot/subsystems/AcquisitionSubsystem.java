package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.Compressor;

public class AcquisitionSubsystem extends SubsystemBase {
    private WPI_TalonSRX m_acquisitionMotor;
    private Solenoid m_acquirerExtender;
    private boolean m_isRunningAcquirer;
    private boolean m_stoppedAcquirer;
    //private Compressor m_compressor;

    public AcquisitionSubsystem() {
        // You need to register the subsystem to get it's periodic
		// method to be called by the Scheduler
        //register();

        //m_compressor = new Compressor();

        m_acquisitionMotor = new WPI_TalonSRX(Constants.ACQUISITION_MOTOR_CAN_ID);
        m_acquirerExtender = new Solenoid(Constants.PCM_CAN_ID, Constants.ACQUIRER_DEPLOY_PCM_PORT);

        m_acquisitionMotor.configPeakCurrentLimit(Constants.ACQUIRE_CURRENT_LIMIT);
		m_acquisitionMotor.configPeakCurrentDuration(Constants.ACQUIRE_CURRENT_DURATION);
		m_acquisitionMotor.configContinuousCurrentLimit(Constants.ACQUIRE_CONTINUOUS_CURRENT_LIMIT);
        m_acquisitionMotor.enableCurrentLimit(true);
        m_isRunningAcquirer = false;
        m_stoppedAcquirer = false;
    }

    @Override
    public void periodic() {
        // if(RobotContainer.getDrivebaseSubsystem().getCurrentMoveSpeedAverage() != 0 && m_isRunningAcquirer) {
        //     m_compressor.stop();
        // } else if(!m_compressor.enabled() && !m_isRunningAcquirer) {
        //     m_compressor.start();
        // }

        // if(RobotContainer.getStorageSubsystem().isAcquireBallOccupied() && m_isRunningAcquirer) {
        //     m_stoppedAcquirer = true;
        //     stopAcquirerMotor();
        // } else if(!RobotContainer.getStorageSubsystem().isAcquireBallOccupied() && m_stoppedAcquirer) {
        //     startAcquirerMotor(true);
        //     m_stoppedAcquirer = false;
        // }

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
        // new SequentialCommandGroup(
        //     new InstantCommand(this::startAcquirerMotorReverse),
        //     new WaitCommand(1),
        //     new InstantCommand(this::stopAcquirerMotor)
        // ).schedule();
    }

    /**
     * Starts the acquirer motor
     * 
     * @param shouldWait determines if we should wait before starting the motor,
     * this should be only be true when the method is used for restarting the motor in the periodic of
     * the acquistion subsystem.
     */
    public void startAcquirerMotor(boolean shouldWait) {
        if(!RobotContainer.getClimberSubsystem().isClimbing()) {
            if(shouldWait) {
                new SequentialCommandGroup(
                    new WaitCommand(0.5)
                    .andThen(() -> m_acquisitionMotor.set(Constants.ACQUIRER_MOTOR_SPEED))
                ).schedule();
            } else {
                m_acquisitionMotor.set(Constants.ACQUIRER_MOTOR_SPEED);
            }
            
        }
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
     * Runs the acquirer motor in reverse
     */
    public void startAcquirerMotorReverse() {
        if(!RobotContainer.getClimberSubsystem().isClimbing()) {
            m_acquisitionMotor.set(-Constants.ACQUIRER_MOTOR_SPEED);
        }
        m_isRunningAcquirer = true;
    }

    /**
     * Gets if the acquirer is running
     * 
     * @return if this acquirer is running
     */
    public boolean m_isRunningAcquirer() {
        return m_isRunningAcquirer;
    }
}