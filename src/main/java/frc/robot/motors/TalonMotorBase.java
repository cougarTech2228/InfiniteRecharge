package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseTalon;

public abstract class TalonMotorBase extends MotorBase {
    
    public TalonMotorBase(int port) {
        super(port);
        // TODO Auto-generated constructor stub
    }

    protected BaseTalon talon;

    @Override
    public void setPID(int slot, Gains pidGains) {
        talon.config_kP(slot, pidGains.kP);
        talon.config_kI(slot, pidGains.kI);
        talon.config_kD(slot, pidGains.kD);
        talon.config_kF(slot, pidGains.kF);
        talon.config_IntegralZone(slot, pidGains.iZone);
        talon.configClosedLoopPeakOutput(slot, pidGains.peakOutput);
    }

    @Override
    public void setVelocity(double vel) {
        talon.set(ControlMode.Velocity, vel);
    }

    @Override
    public void setPosition(double pos) {
        talon.set(ControlMode.Position, pos);
    }

    @Override
    public void setInverted(boolean inverted) {
        talon.setInverted(inverted);
    }

    @Override
    public void set(double percent) {
        talon.set(ControlMode.PercentOutput, percent);
    }

    @Override
    public void follow(MotorBase m) {
        talon.set(ControlMode.Follower, m.port);
    }

    @Override
    public void setBrakeMode(BrakeMode brakeMode) {
        switch(brakeMode) {
            case Brake:
                talon.setNeutralMode(NeutralMode.Brake);
                break;
            case Coast:
                talon.setNeutralMode(NeutralMode.Coast);
                break;
        }
    }

    @Override
    public void addMotionProfilingPoint(double pos) {
        // TODO Auto-generated method stub

    }

    @Override
    public void executeProfile() {
        // TODO Auto-generated method stub

    }

    @Override
    public void clearProfile() {
        // TODO Auto-generated method stub

    }

    @Override
    public double getPosition() {
        return talon.getSelectedSensorPosition();
    }

    @Override
    public double getVelocity() {
        return talon.getSelectedSensorVelocity();
    }

    @Override
    public void resetEncoder() {
        talon.setSelectedSensorPosition(0);
    }

    @Override
    public void setOutputRange(double min, double max) {
        talon.configPeakOutputForward(max);
        talon.configPeakOutputReverse(min);
    }

    @Override
    public double getCurrent() {
        return talon.getSupplyCurrent();
    }

    @Override
    public double getVoltage() {
        return talon.getBusVoltage();
    }

    @Override
    public double getTemperature() {
        return talon.getTemperature();
    }
}