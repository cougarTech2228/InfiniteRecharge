package frc.robot.subsystems;

import java.util.HashMap;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.commands.MethodCommand;
import frc.robot.sensors.GarminLidar;
import frc.robot.util.Concensus;
import frc.robot.util.ConfigurableSubsystem;
import frc.robot.util.Configuration;
import frc.robot.util.Concensus.ConcensusMode;

public class ShooterSubsystem extends ConfigurableSubsystem {
    private boolean isRunning;
    private HashMap<Integer, Double> m_shooterMap;
    private boolean m_encodersAreAvailable;
    private WPI_TalonSRX m_talon;
    private GarminLidar lidarSensor;

    public static final Concensus shouldShoot = new Concensus(ConcensusMode.All);

    public ShooterSubsystem() {
        m_talon = new WPI_TalonSRX(Constants.SHOOTER_CAN_ID);
        Configuration.create(this); // Creates a configuration for the shooter speed on shuffleboard

        m_shooterMap = new HashMap<Integer, Double>();
        lidarSensor = new GarminLidar();

        // shooterMap.put(Constants.MIN_SHOOTING_DISTANCE, 100000); // distance (in),
        // Velocity
        // m_shooterMap.put(80, 10500);
        // m_shooterMap.put(97, 87500);
        // m_shooterMap.put(122, 75000);
        // m_shooterMap.put(227, 77000);
        // m_shooterMap.put(304, 82000);

        
        m_shooterMap.put(80, 110512.0);
        m_shooterMap.put(81, 106598.0);
        m_shooterMap.put(83, 100577.0);
        m_shooterMap.put(84, 98206.9);
        m_shooterMap.put(87, 92757.3);
        m_shooterMap.put(90, 88939.9);
        m_shooterMap.put(95, 84649.9);
        m_shooterMap.put(100, 81867.3);
        m_shooterMap.put(105, 79972.3);
        m_shooterMap.put(110, 78646.6);
        m_shooterMap.put(120, 77046.0);
        m_shooterMap.put(130, 76280.6);
        m_shooterMap.put(140, 75999.4);
        m_shooterMap.put(150, 76021.1);
        m_shooterMap.put(160, 76242.2);
        m_shooterMap.put(170, 76599.9);
        m_shooterMap.put(180, 77053.6);
        m_shooterMap.put(190, 77576.1);
        m_shooterMap.put(200, 78148.6);
        m_shooterMap.put(210, 78757.7);
        m_shooterMap.put(220, 79393.6);
        m_shooterMap.put(230, 80049.2);
        m_shooterMap.put(240, 80719.0);
        m_shooterMap.put(250, 81399.0);
        m_shooterMap.put(260, 82085.8);
        m_shooterMap.put(270, 82777.1);
        m_shooterMap.put(280, 83470.9);
        m_shooterMap.put(290, 84165.7);
        m_shooterMap.put(300, 84860.4);
        m_shooterMap.put(310, 85553.8);
        m_shooterMap.put(320, 86245.3);
        m_shooterMap.put(330, 86934.3);
        m_shooterMap.put(340, 87620.3);
        m_shooterMap.put(350, 88302.9);
        m_shooterMap.put(360, 88981.8);

        m_talon.configFactoryDefault();
        m_encodersAreAvailable =  m_talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.PID_PRIMARY, Constants.kTimeoutMs) == ErrorCode.OK;
        m_talon.config_kP(0, 0.01764, Constants.kTimeoutMs); //.0465
        m_talon.config_kI(0, 0, Constants.kTimeoutMs);
        m_talon.config_kD(0, 0, Constants.kTimeoutMs);
        m_talon.config_kF(0, 0.00851, Constants.kTimeoutMs);
        m_talon.config_IntegralZone(0, 0, Constants.kTimeoutMs);
        m_talon.configClosedLoopPeakOutput(0, 1.0, Constants.kTimeoutMs);
        m_talon.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);
        m_talon.setInverted(true);
        m_talon.setSensorPhase(false);
        m_talon.configVoltageCompSaturation(11);

        m_talon.configClosedLoopPeriod(0, 1, Constants.kTimeoutMs);

        m_talon.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
        m_talon.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);

        m_talon.configPeakCurrentLimit(Constants.SHOOTER_CURRENT_LIMIT);
		m_talon.configPeakCurrentDuration(Constants.SHOOTER_CURRENT_DURATION);
		m_talon.configContinuousCurrentLimit(Constants.SHOOTER_CONTINUOUS_CURRENT_LIMIT);
		m_talon.enableCurrentLimit(true);
    }
    public double correspondingSpeed(int distance) {
        int currentClosest = -1;
        boolean firstTime = true;
        for (int curDistance : m_shooterMap.keySet()) {
            if (firstTime) {
                currentClosest = curDistance;
                firstTime = false;
            } else {
                if (distance == curDistance) {
                    return curDistance;
                } else if ((Math.abs(curDistance - distance)) < (Math.abs(currentClosest - distance))) {
                    currentClosest = curDistance;
                }
            }
        }
        return m_shooterMap.get(currentClosest) - 1500;
    }
    @Override
    public void periodic() {
        if(isRunning && shouldShoot.getConcensus()) {
            m_talon.set(ControlMode.Velocity, correspondingSpeed(lidarSensor.getAverage()));
        } else {
            m_talon.set(ControlMode.PercentOutput, 0);
        }
    }
    public Command cmdStartShooter() {
        return new MethodCommand(() -> {
            isRunning = true;
            AcquisitionSubsystem.shouldAcquirerExtend.vote(true);
            AcquisitionSubsystem.shouldAcquire.vote(false);
        });
    }
    public boolean isShooterMotorRunning() {
        return isRunning && shouldShoot.getConcensus();
    }
    public boolean isShooterMotorActivated() {
        return isRunning;
    }
    public Command cmdStopShooter() {
        return new MethodCommand(() -> {
            isRunning = false;
            AcquisitionSubsystem.shouldAcquirerExtend.vote(false);
            AcquisitionSubsystem.shouldAcquire.vote(true);
        });
    }
}