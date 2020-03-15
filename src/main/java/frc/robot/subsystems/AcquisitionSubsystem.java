package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.MethodCommand;
import frc.robot.util.Concensus;
import frc.robot.util.Concensus.ConcensusMode;

public class AcquisitionSubsystem extends SubsystemBase {
    private WPI_TalonSRX m_acquisitionMotor;
    private Solenoid m_acquirerExtender;
    private double acquirerSpeed = 0;

    /**
     * If any class votes it should not acquire, it will not
     */
    public final static Concensus shouldAcquire = new Concensus(ConcensusMode.All);
    /**
     * If any class votes it should extend, it will
     */
    public final static Concensus shouldAcquirerExtend = new Concensus(ConcensusMode.Any);

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
    public void periodic() {
        //if other classes want it to run, it will use its speed, otherwise it'll stop
        m_acquisitionMotor.set(shouldAcquire.getConcensus() ? acquirerSpeed : 0);

        //if other classes want it to extend it will
        m_acquirerExtender.set(shouldAcquirerExtend.getConcensus());
    }
    /**
     * Deploys the acquirer
     */
    public Command cmdDeployAcquirer() {
        return new MethodCommand(() -> shouldAcquirerExtend.vote(true));
    }
    /**
     * Retracts the acquirer unless other classes have told it to stay extended
     */
    public Command cmdRetractAcquirer() {
        return new MethodCommand(() -> shouldAcquirerExtend.vote(false));
    }
    /**
     * turns on the acquirer, pulling balls in, unless disabled
     */
    public Command cmdAcquireForwards() {
        return new MethodCommand(() -> acquirerSpeed = Constants.ACQUIRER_MOTOR_SPEED)
        .runOnEnd(() -> acquirerSpeed = 0)
        .perpetually();
    }
    /**
     * turns on the acquirer, pushing balls out, unless disabled
     */
    public Command cmdAcquireBackwards() {
        return new MethodCommand(() -> acquirerSpeed = -Constants.ACQUIRER_MOTOR_SPEED)
        .runOnEnd(() -> acquirerSpeed = 0)
        .perpetually();
    }
    /**
     * Gets if the acquirer is running
     * 
     * @return if this acquirer is running
     */
    public boolean m_isRunningAcquirer() {
        return shouldAcquire.getConcensus() && acquirerSpeed != 0;
    }
}