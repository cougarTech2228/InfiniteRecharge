package frc.robot.motors;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

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
    public abstract void setPID(int slot, Gains pidGains);
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