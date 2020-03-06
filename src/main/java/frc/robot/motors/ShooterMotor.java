package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import java.util.HashMap;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.Constants;
import frc.robot.util.Configuration;

public class ShooterMotor {//extends TalonSRXMotor {

    private HashMap<Integer, Integer> m_shooterMap;
    private boolean m_encodersAreAvailable;
    private WPI_TalonSRX m_talon;

    public double m_shooterSpeed = 120000;

    public ShooterMotor() {
        //super(Constants.SHOOTER_CAN_ID);
        
        m_talon = new WPI_TalonSRX(Constants.SHOOTER_CAN_ID);
        Configuration.create(this); // Creates a configuration for the shooter speed on shuffleboard

        m_shooterMap = new HashMap<Integer, Integer>();

        // shooterMap.put(Constants.MIN_SHOOTING_DISTANCE, 100000); // distance (in),
        // Velocity
        m_shooterMap.put(139, 85000);
        m_shooterMap.put(244, 93500);

        m_talon.configFactoryDefault();
        m_encodersAreAvailable =  m_talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.PID_PRIMARY, Constants.kTimeoutMs) == ErrorCode.OK;
        m_talon.config_kP(0, 0.0075, Constants.kTimeoutMs);
        m_talon.config_kI(0, 0, Constants.kTimeoutMs);
        m_talon.config_kD(0, 0, Constants.kTimeoutMs);
        m_talon.config_kF(0, 0.00854, Constants.kTimeoutMs);
        m_talon.config_IntegralZone(0, 0, Constants.kTimeoutMs);
        m_talon.configClosedLoopPeakOutput(0, 1.0, Constants.kTimeoutMs);
        m_talon.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);
        m_talon.setInverted(true);
        m_talon.setSensorPhase(true);
        m_talon.configVoltageCompSaturation(11);

        m_talon.configClosedLoopPeriod(0, 1, Constants.kTimeoutMs);

        m_talon.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
        m_talon.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);

        m_talon.configPeakCurrentLimit(Constants.SHOOTER_CURRENT_LIMIT);
		m_talon.configPeakCurrentDuration(Constants.SHOOTER_CURRENT_DURATION);
		m_talon.configContinuousCurrentLimit(Constants.SHOOTER_CONTINUOUS_CURRENT_LIMIT);
		m_talon.enableCurrentLimit(true);

        // new ShuffleboardAdapter("Shooter Motor")
        // .addDoubleText("Motor Speed", Constants.SHOOTER_MOTOR_SPEED, value ->
        // Constants.SHOOTER_MOTOR_SPEED = value);
        // GainsBinder g = new GainsBinder("Shooter Motor", m_talon, new Gains(0.01, 0,
        // 0, 0, 0, 1.0));
        /*
         * m_talon.setPID(0, new Gains(0.01, 0, 0, 0, 0, 1.0));
         */

        // new ShuffleboardAdapter("shooter")
        // .addDoubleText("kP", 0, value -> {m_talon.config_kP(0, value); })
        // // .addDoubleText("kI", 0, value -> {m_talon.config_kF(0, value); })
        // // .addDoubleText("kD", 0, value -> {m_talon.config_kI(0, value); })
        // .addDoubleText("kF", 0, value -> {m_talon.config_kF(0, value); })
        // // .addDoubleText("kIZone", 0, value -> {m_talon.config_IntegralZone(0,
        // (int)value); })
        // .addDoubleText("TargetVel", 0, value -> m_talon.set(ControlMode.Velocity,
        // value))
        // .addDouble("Velocity Error", 0, () -> m_talon.getClosedLoopError())
        // .addDouble("Velocity", 0, () -> m_talon.getSelectedSensorVelocity())
        // .addDouble("Current", 0, () -> m_talon.getSupplyCurrent());

        System.out.println(m_encodersAreAvailable);
    }

    public void start(int distance) {
        //m_talon.set(ControlMode.Velocity, m_shooterMap.get(closestDistance(distance)));
        m_talon.set(ControlMode.Velocity, m_shooterSpeed);
    }

    public void stop() {
        m_talon.set(ControlMode.Velocity, 0);
    }

    public WPI_TalonSRX getTalon() {
        return m_talon;
    }

    public int closestDistance(int distance) {
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
        return currentClosest;
    }

}