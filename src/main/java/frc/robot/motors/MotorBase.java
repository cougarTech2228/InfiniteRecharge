package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.controller.PIDController;

public abstract class MotorBase {
    protected int port;
    protected double maxRPM;

    public MotorBase(int port) {
        this.port = port;
    }

    enum BrakeMode {
        Brake(1), Coast(2);
        int value;
        BrakeMode(int value) {
            this.value = value;
        }
    };

    public void setMaxRPM(double rpm) {
        maxRPM = rpm;
    }

    public abstract void setOutputRange(double min, double max);
    public void setPID(int slot, Gains pidGains) {
        setP(slot, pidGains.kP);
        setI(slot, pidGains.kI);
        setD(slot, pidGains.kD);
        setF(slot, pidGains.kF);
        setIntegralZone(slot, pidGains.iZone);
    }
    public abstract void setP(int slot, double kP);
    public abstract void setI(int slot, double kI);
    public abstract void setD(int slot, double kD);
    public abstract void setF(int slot, double kF);
    public abstract void setIntegralZone(int slot, double kP);
    public abstract void setVelocity(double vel);
    public abstract void setPosition(double pos);
    public abstract void setInverted(boolean inverted);
    public abstract void set(double percent);
    public abstract void resetEncoder();

    public abstract void follow(MotorBase m);
    public abstract void setBrakeMode(BrakeMode brakeMode);
    public abstract void addMotionProfilingPoint(double pos);
    public abstract void executeProfile();
    public abstract void clearProfile();

    public int getPort() {
        return port;
    }

    public abstract double getPosition();
    public abstract double getVelocity();
    public abstract double getCurrent();
    public abstract double getVoltage();
    public abstract double getTemperature();
}