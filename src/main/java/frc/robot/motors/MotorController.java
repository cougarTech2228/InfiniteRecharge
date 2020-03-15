package frc.robot.motors;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.motors.MotorBase;

public class MotorController {
    private ProfiledPIDController controller;
    private Constraints constraints;
    private DoubleSupplier pos = () -> 0;
    private DoubleSupplier vel = () -> 0;
    private Function<Double, Double> filter = value -> value;
    private DoubleConsumer set;
    private double conversion = 1;
    private Runnable updater = () -> {};

    public MotorController(double kP, double kI, double kD, double maxVel, double maxAccel) {
        constraints = new Constraints(maxVel, maxAccel);
        controller = new ProfiledPIDController(kP, kI, kD, constraints);
    }
    public void setP(double kP) {
        controller.setP(kP);
    }
    public void setI(double kI) {
        controller.setP(kI);
    }
    public void setD(double kD) {
        controller.setP(kD);
    }
    public void setIzone(double izone) {
        controller.setIntegratorRange(-izone, izone);
    }
    public void setCountsPerRevolution(double conversionConstant) {
        conversion = conversionConstant;
    }
    public void setMotor(MotorBase motor) {
        pos = () -> motor.getPosition();
        vel = () -> motor.getVelocity();
        set = value -> motor.set(value);
    }
    public void setMotor(TalonSRX motor) {
        pos = () -> motor.getSelectedSensorPosition();
        vel = () -> motor.getSelectedSensorVelocity();
        set = value -> motor.set(ControlMode.PercentOutput, value);
    }
    public void setMaxVelocity(double vel) {
        constraints = new Constraints(vel, constraints.maxAcceleration);
        controller.setConstraints(constraints);
    }
    public void setMaxAccel(double accel) {
        constraints = new Constraints(constraints.maxVelocity, accel);
        controller.setConstraints(constraints);
    }
    public double getPosition() {
        return pos.getAsDouble() / conversion;
    }
    public double getVelocity() {
        return vel.getAsDouble() / conversion;
    }
    public void setPercentOutput(double percentOutput) {
        set.accept(percentOutput);
        updater = () -> {};
    }
    public void setPositionGoal(double position) {
        controller.setGoal(position * conversion);
        updater = () -> set.accept(filter.apply(controller.calculate(pos.getAsDouble())));
    }
    public void setVelocityGoal(double velocity) {
        controller.setGoal(velocity * conversion);
        updater = () -> set.accept(filter.apply(controller.calculate(vel.getAsDouble())));
    }
    public void run() {
        updater.run();
    }
    public void useFilter(MedianFilter filter) {
        this.filter = value -> filter.calculate(value);
    }
    public void useFilter(LinearFilter filter) {
        this.filter = value -> filter.calculate(value);
    }
    //public Command 
}