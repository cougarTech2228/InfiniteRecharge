package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.StartAcquirerCommand;
import frc.robot.motors.TalonSRXMotor;

public class AcquisitionSubsystem extends SubsystemBase {
    private TalonSRXMotor m_acquisitionMotor;
    private double acquisitionMaxSpeed = 0.5;
    private Solenoid m_acquirerExtender;
    private StartAcquirerCommand m_startAcquirerCommandInstance;

    public AcquisitionSubsystem() {
        register();

        m_acquisitionMotor = new TalonSRXMotor(Constants.ACQUISITION_MOTOR_CAN_ID);
        m_acquirerExtender = new Solenoid(Constants.PCM_CAN_ID, Constants.ACQUIRER_DEPLOY_PCM_PORT);
    }
    @Override
    public void periodic() {}

    public void setAcquirerSpeed(double speed) {
        m_acquisitionMotor.set(acquisitionMaxSpeed * -speed);
    }

    public void createStartAcquireCommandInstance(StartAcquirerCommand startAcquirerCommand) {
        m_startAcquirerCommandInstance = startAcquirerCommand;
    }

    public TalonSRXMotor getAcquisitionMotor() {
        return m_acquisitionMotor;
    }

    public void stopAcquirer() {
        if(m_startAcquirerCommandInstance != null) {
            m_startAcquirerCommandInstance.end(true);
        }
    }

    public void deployAcquirer() {
        m_acquirerExtender.set(true);
    }

    public void retractAcquirer() {
        m_acquirerExtender.set(false);
    }
}