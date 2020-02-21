package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.BaseTalon;

import edu.wpi.first.wpilibj.DriverStation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.Constants;
import frc.robot.util.ShuffleboardAdapter;

public class ShooterMotor extends TalonSRXMotor {

    private HashMap<Integer, Integer> shooterMap;
    private boolean m_encodersAreAvailable;

    public ShooterMotor() {
        super(Constants.SHOOTER_CAN_ID);

        shooterMap = new HashMap<Integer, Integer>();

        // shooterMap.put(Constants.MIN_SHOOTING_DISTANCE, 100000); // distance (in),
        // Velocity
        shooterMap.put(139, 85000);
        shooterMap.put(244, 93500);

        talon.configFactoryDefault();
        m_encodersAreAvailable =  talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.PID_PRIMARY, Constants.kTimeoutMs) == ErrorCode.OK;
        talon.config_kP(0, 0.0075, Constants.kTimeoutMs);
        talon.config_kI(0, 0, Constants.kTimeoutMs);
        talon.config_kD(0, 0, Constants.kTimeoutMs);
        talon.config_kF(0, 0.00854, Constants.kTimeoutMs);
        talon.config_IntegralZone(0, 0, Constants.kTimeoutMs);
        talon.configClosedLoopPeakOutput(0, 1.0, Constants.kTimeoutMs);
        talon.configAllowableClosedloopError(0, 0, Constants.kTimeoutMs);
        talon.setInverted(true);
        talon.setSensorPhase(true);
        talon.configVoltageCompSaturation(11);

        talon.configClosedLoopPeriod(0, 1, Constants.kTimeoutMs);

        talon.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
        talon.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
        // new ShuffleboardAdapter("Shooter Motor")
        // .addDoubleText("Motor Speed", Constants.SHOOTER_MOTOR_SPEED, value ->
        // Constants.SHOOTER_MOTOR_SPEED = value);
        // GainsBinder g = new GainsBinder("Shooter Motor", m_talon, new Gains(0.01, 0,
        // 0, 0, 0, 1.0));
        /*
         * talon.setPID(0, new Gains(0.01, 0, 0, 0, 0, 1.0));
         */

        // new ShuffleboardAdapter("shooter")
        // .addDoubleText("kP", 0, value -> {talon.config_kP(0, value); })
        // // .addDoubleText("kI", 0, value -> {talon.config_kF(0, value); })
        // // .addDoubleText("kD", 0, value -> {talon.config_kI(0, value); })
        // .addDoubleText("kF", 0, value -> {talon.config_kF(0, value); })
        // // .addDoubleText("kIZone", 0, value -> {talon.config_IntegralZone(0,
        // (int)value); })
        // .addDoubleText("TargetVel", 0, value -> talon.set(ControlMode.Velocity,
        // value))
        // .addDouble("Velocity Error", 0, () -> talon.getClosedLoopError())
        // .addDouble("Velocity", 0, () -> talon.getSelectedSensorVelocity())
        // .addDouble("Current", 0, () -> talon.getSupplyCurrent());

        System.out.println(m_encodersAreAvailable);
    }

    public void start(int distance) {
        talon.set(ControlMode.Velocity, shooterMap.get(closestDistance(distance)));
    }

    public void stop() {
        talon.set(ControlMode.Velocity, 0);
    }

    public BaseTalon getTalon() {
        return talon;
    }

    public int closestDistance(int distance) {
        int currentClosest = -1;
        boolean firstTime = true;
        for (int curDistance : shooterMap.keySet()) {
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