package frc.robot.motors;

import com.playingwithfusion.CANVenom;
import com.playingwithfusion.CANVenom.BrakeCoastMode;
import com.playingwithfusion.CANVenom.ControlMode;

public class VenomMotor extends MotorBase {
    
    private CANVenom venom;

    public VenomMotor(int port) {
        super(port);
        venom = new CANVenom(port);
    }
    /**
     * The Venom motors only support a single slot, so the slot parameter is redundant here
     */
    @Override
    public void setPID(int slot, Gains pidGains) {
        venom.setKP(pidGains.kP);
        venom.setKI(pidGains.kI);
        venom.setKD(pidGains.kD);
        venom.setKF(pidGains.kF);
    }

    @Override
    public void setP(int slot, double kP) {
        venom.setKP(kP);
    }

    @Override
    public void setI(int slot, double kI) {
        venom.setKI(kI);
    }

    @Override
    public void setD(int slot, double kD) {
        venom.setKD(kD);
    }

    @Override
    public void setF(int slot, double kF) {
        venom.setKF(kF);
    }
    /**
     * @deprecated
     * Do not use this method, Venom's do not support integral zone
     */
    @Override
    public void setIntegralZone(int slot, double kIzone) {}

    @Override
    public void setVelocity(double vel) {
        venom.setCommand(ControlMode.SpeedControl, vel);
    }

    @Override
    public void setPosition(double pos) {
        venom.setCommand(ControlMode.PositionControl, pos);
    }

    @Override
    public void setInverted(boolean inverted) {
        venom.setInverted(inverted);
    }

    @Override
    public void set(double percent) {
        venom.setCommand(ControlMode.Proportional, percent);
    }

    @Override
    public void setBrakeMode(BrakeMode brakeMode) {
        switch(brakeMode) {
            case Brake:
                venom.setBrakeCoastMode(BrakeCoastMode.Brake);
                break;
            case Coast:
                venom.setBrakeCoastMode(BrakeCoastMode.Coast);
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
        return venom.getPosition();
    }

    @Override
    public double getVelocity() {
        return venom.getSpeed();
    }

    @Override
    public void resetEncoder() {
        venom.resetPosition();
    }

    @Override
    public void setOutputRange(double min, double max) {
        venom.setMinPILimit(min);
        venom.setMaxPILimit(max);
    }

    @Override
    public void follow(MotorBase m) {
        venom.follow(((VenomMotor)m).venom);

    }

    @Override
    public double getCurrent() {
        return venom.getOutputCurrent();
    }

    @Override
    public double getVoltage() {
        return venom.getBusVoltage();
    }

    @Override
    public double getTemperature() {
        return venom.getTemperature();
    }
}