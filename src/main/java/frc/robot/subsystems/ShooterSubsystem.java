package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.commands.MethodCommand;
import frc.robot.motors.Gains;
import frc.robot.motors.TalonSRXMotor;
import frc.robot.shuffleboard.GainsBinder;
import frc.robot.util.ShuffleboardAdapter;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.Solenoid;

public class ShooterSubsystem extends SubsystemBase {
    TalonSRX shooterMotor;
    private DigitalInput m_inputBallShooterChecker;
    private StorageSubsystem m_storageSubsystem;
    private boolean[] m_drumArray;
    private Solenoid m_lifterSolenoid;
    public ShooterSubsystem() {
        register();
        shooterMotor = new TalonSRX(Constants.SHOOTER_CAN_ID);

        shooterMotor.configFactoryDefault();
        shooterMotor.configSelectedFeedbackSensor(
            FeedbackDevice.QuadEncoder,
            Constants.PID_PRIMARY,
            Constants.kTimeoutMs
        );
    


        shooterMotor.config_kP(0, 0.01, Constants.kTimeoutMs);
		shooterMotor.config_kI(0, 0, Constants.kTimeoutMs);
		shooterMotor.config_kD(0, 0, Constants.kTimeoutMs);
		shooterMotor.config_kF(0, 0, Constants.kTimeoutMs);
		shooterMotor.config_IntegralZone(0, 0, Constants.kTimeoutMs);
		shooterMotor.configClosedLoopPeakOutput(0, 1.0, Constants.kTimeoutMs);
        shooterMotor.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);
        
        shooterMotor.configClosedLoopPeriod(0, 1, Constants.kTimeoutMs);

        shooterMotor.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
		shooterMotor.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
        //GainsBinder g = new GainsBinder("Shooter Motor", shooterMotor, new Gains(0.01, 0, 0, 0, 0, 1.0));
        /*shooterMotor.setPID(0, new Gains(0.01, 0, 0, 0, 0, 1.0));
        */
        new ShuffleboardAdapter("Shooter")
            .addDoubleText("target velocity", 0, value -> {
                shooterMotor.set(ControlMode.Velocity, value);
                System.out.println("Set it to: " + value);
            });
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler

        m_inputBallShooterChecker = new DigitalInput(Constants.DIGITAL_IO_3);
        //m_storageSubsystem = storageSubsystem;
        m_drumArray = m_storageSubsystem.getDrumArray();
        //m_lifterSolenoid = new Solenoid(Constants.PCM_CAN_ID, Constants.PCM_PORT_1);
    }

    }
    @Override
    public void periodic() {
        System.out.println(shooterMotor.getSelectedSensorPosition());
        /*
        if(OI.getXboxBButton()) {
            shooterMotor.set(-1.0);
        }
        else {
            shooterMotor.set(0);
        }*/
    }

    public Command cmdEnableShooter() {
        return new MethodCommand(() -> shooterMotor.set(ControlMode.PercentOutput, -1.0), true).runOnEnd(() -> shooterMotor.set(ControlMode.PercentOutput, 0));
    }
    public boolean[] getDrumArray()
    {
        return m_drumArray;
    }
    
    // public boolean[] getDrumArray()
    // {
    //     return drumArray;
    // }

    // public boolean isShooterSlotOccupied()
    // {
    //     return !m_inputBallShooterChecker.get();
    // }

    public void raiseLifter()
    {
        System.out.println("raise lifter");
        m_lifterSolenoid.set(true);
    }

    public void lowerLifter()
    {
        System.out.println("lower lifter");
        m_lifterSolenoid.set(false);
    }
    
    public void startFlywheel()
    {
        System.out.println("start flywheel");
    }

    public void stopFlywheel()
    {
        System.out.println("stop flywheel");
    }
}