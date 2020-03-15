package frc.robot.motors;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.Constants;
import frc.robot.util.ShuffleboardAdapter;

public class TalonController {
    private TalonSRX talon;
    private double targetPos;
    private double targetVel;
    private double targetOutput;
    private double conversion;

    public TalonController(double kP, double kI, double kD, int port) {
        talon = new TalonSRX(port);
        talon.config_kP(0, kP);
        talon.config_kI(0, kI);
        talon.config_kD(0, kD);
        talon.configClosedLoopPeriod(0, 1);
        talon.configClosedLoopPeriod(1, 1);
        talon.configVoltageCompSaturation(11);
        talon.enableVoltageCompensation(true);
        /*
        new ShuffleboardAdapter("Talon thing")
            .addDoubleText("target pos", 0, value -> {targetPos = value; update();})
            .addDoubleText("P F", 0, value -> setPF(value))
            .addDoubleText("P P", 0, value -> setPP(value))
            .addDoubleText("P I", 0, value -> setPI(value))
            .addDoubleText("P D", 0, value -> setPD(value))
            .addDoubleText("P Izone", 0, value -> setPIzone((int)value))

            .addDoubleText("target vel", 0, value -> {targetVel = value; update();})
            .addDoubleText("V F", 0, value -> setVF(value))
            .addDoubleText("V P", 0, value -> setVP(value))
            .addDoubleText("V I", 0, value -> setVI(value))
            .addDoubleText("V D", 0, value -> setVD(value))
            .addDoubleText("V Izone", 0, value -> setVIzone((int)value))

            .addDoubleText("target output", 0, value -> {targetOutput = value; update();})

            .addDouble("Pos", 0, () -> getPosition())
            .addDouble("Vel", 0, () -> getVelocity())
            .addDouble("Pos Error", 0, () -> getPositionError())
            .addDouble("Vel Error", 0, () -> getVelocityError());
        */
        talon.configFactoryDefault();
        talon.configSelectedFeedbackSensor(
            FeedbackDevice.CTRE_MagEncoder_Relative,
            0,
            Constants.kTimeoutMs
        );
        talon.configSelectedFeedbackSensor(
            FeedbackDevice.CTRE_MagEncoder_Relative,
            1,
            Constants.kTimeoutMs
        );
    }
    private void update() {
        if(targetPos != 0) {
            this.setPositionGoal(targetPos);
        }
        else if(targetVel != 0) {
            this.setVelocityGoal(targetVel);
        }
        else {
            this.setPercentOutput(targetOutput);
        }
    }
    public void setPF(double kF) {
        talon.config_kF(0, kF);
    }
    /**
     * Make sure you have very large PP
     */
    public void setPP(double kP) {
        talon.config_kP(0, kP);
    }
    public void setVP(double kP) {
        talon.config_kP(1, kP);
    }
    public void setPI(double kI) {
        talon.config_kI(0, kI);
    }
    public void setVF(double kF) {
        talon.config_kF(1, kF);
    }
    public void setVI(double kI) {
        talon.config_kI(1, kI);
    }
    public void setPD(double kD) {
        talon.config_kD(0, kD);
    }
    public void setVD(double kD) {
        talon.config_kD(1, kD);
    }
    public void setPIzone(int izone) {
        talon.config_IntegralZone(0, izone);
    }
    public void setVIzone(int izone) {
        talon.config_IntegralZone(1, izone);
    }
    public void setCountsPerRevolution(double conversionConstant) {
        conversion = conversionConstant;
    }
    public double getPosition() {
        return talon.getSelectedSensorPosition();
    }
    public double getVelocity() {
        return talon.getSelectedSensorVelocity();
    }
    public double getPositionError() {
        return talon.getClosedLoopError(0);
    }
    public double getVelocityError() {
        return talon.getClosedLoopError(1);
    }
    public void setPercentOutput(double percentOutput) {
        talon.set(ControlMode.PercentOutput, percentOutput);
    }
    public void setPositionGoal(double position) {
        talon.selectProfileSlot(0, 0);
        talon.set(ControlMode.Position, position);
    }
    public void setVelocityGoal(double velocity) {
        talon.selectProfileSlot(1, 0);
        talon.set(ControlMode.Velocity, velocity);
    }
}