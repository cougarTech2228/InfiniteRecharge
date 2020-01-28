package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.motors.Gains;
import frc.robot.shuffleboard.GainsBinder;
import frc.robot.util.ShuffleboardAdapter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSubsystem extends SubsystemBase {

    TalonSRX m_shooterMotor;
    private DigitalInput m_inputBallShooterChecker;
    private Solenoid m_bopper;
    private StorageSubsystem m_storageSubsystem;
    private boolean m_isShooting;
    //private ShootOnceCommand m_shootOnceCommandReference;

    public ShooterSubsystem(StorageSubsystem storageSubsystem) {
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        register();

        m_storageSubsystem = storageSubsystem;

        m_shooterMotor = new TalonSRX(Constants.SHOOTER_CAN_ID);
        m_bopper = new Solenoid(Constants.PCM_PORT_0);
        m_inputBallShooterChecker = new DigitalInput(Constants.DIGITAL_IO_3);
        m_isShooting = false;

        new ShuffleboardAdapter("Shooter")
            .addDoubleText("target velocity", 0, value -> {
                //m_shooterMotor.set(ControlMode.Velocity, value);
                //System.out.println("Set it to: " + value);
            });
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("shooterChecker", m_inputBallShooterChecker.get());
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void startShooterMotor() {
        m_storageSubsystem.setIsShooting(true);
        m_shooterMotor.set(ControlMode.PercentOutput, -1.0);
    }

    public void stopShooterMotor() {
        m_storageSubsystem.setIsShooting(false);
        m_shooterMotor.set(ControlMode.PercentOutput, 0);
    }

    public boolean isShooterSlotOccupied()
    {
        return !m_inputBallShooterChecker.get();
    }

    public void raiseBopper()
    {
        m_bopper.set(true);
    }

    public void lowerBopper()
    {
        m_bopper.set(false);
    }

    public void initShooterMotor()
    {
        m_shooterMotor.configFactoryDefault();
        m_shooterMotor.configSelectedFeedbackSensor(
            FeedbackDevice.QuadEncoder,
            Constants.PID_PRIMARY,
            Constants.kTimeoutMs
        );
        m_shooterMotor.config_kP(0, 0.01, Constants.kTimeoutMs);
		m_shooterMotor.config_kI(0, 0, Constants.kTimeoutMs);
		m_shooterMotor.config_kD(0, 0, Constants.kTimeoutMs);
		m_shooterMotor.config_kF(0, 0, Constants.kTimeoutMs);
		m_shooterMotor.config_IntegralZone(0, 0, Constants.kTimeoutMs);
		m_shooterMotor.configClosedLoopPeakOutput(0, 1.0, Constants.kTimeoutMs);
        m_shooterMotor.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);
        
        m_shooterMotor.configClosedLoopPeriod(0, 1, Constants.kTimeoutMs);

        m_shooterMotor.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
		m_shooterMotor.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
        //GainsBinder g = new GainsBinder("Shooter Motor", m_shooterMotor, new Gains(0.01, 0, 0, 0, 0, 1.0));
        /*shooterMotor.setPID(0, new Gains(0.01, 0, 0, 0, 0, 1.0));
        */
    }

    public void tryToShoot()
    {
        if (isShooterSlotOccupied())
        {
            //startShooterMotor();

            System.out.println("Bop and Rotate Drum One Section");

            RobotContainer.getBopperCommand()
            .andThen(() -> m_storageSubsystem.setDrumArray(m_storageSubsystem.getDrumArrayIndex(), false))
            .andThen(() -> m_storageSubsystem.isDrumFull())
            .andThen(RobotContainer.getRotateDrumOneSectionCommand()).schedule();
            
        }
        else
        {
            System.out.println("Rotate Drum One Section");

            RobotContainer.getRotateDrumOneSectionCommand().schedule();
        }
    }
}