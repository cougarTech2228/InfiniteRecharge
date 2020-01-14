package frc.robot.motors;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class NeoMotor extends MotorBase {

    private CANSparkMax neo;
    private CANEncoder neoEnc;
    private CANPIDController neoPID;

    public NeoMotor(String name, int port) {
        super(name, port);
        neo = new CANSparkMax(port, MotorType.kBrushless);
        neoEnc = neo.getEncoder();
        neoPID = neo.getPIDController();
        neo.restoreFactoryDefaults();
        // Test comment here
    }

    @Override
    public void setPID(int slot, Gains pidGains) {
        neoPID.setP(pidGains.kP, slot);
        neoPID.setI(pidGains.kI, slot);
        neoPID.setD(pidGains.kD, slot);
        neoPID.setFF(pidGains.kF, slot);
        neoPID.setIZone(pidGains.iZone, slot);
    }

    @Override
    public void setVelocity(double vel) {
        neoPID.setReference(vel, ControlType.kVelocity);
    }

    @Override
    public void setPosition(double pos) {
        neoPID.setReference(pos, ControlType.kPosition);
    }

    @Override
    public void setInverted(boolean inverted) {
        neo.setInverted(inverted);

    }

    @Override
    public void set(double percent) {
        neo.set(percent);
    }

    @Override
    public void resetEncoder() {
        neoEnc.setPosition(0);
    }

    @Override
    public void follow(MotorBase m) {
        neo.follow(((NeoMotor)m).neo);

    }

    @Override
    public void setBreakMode(BrakeMode brakeMode) {
        switch(brakeMode) {
            case Break:
                neo.setIdleMode(IdleMode.kBrake);
                break;
            case Coast:
                neo.setIdleMode(IdleMode.kCoast);
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
        return neoEnc.getPosition();
    }

    @Override
    public double getVelocity() {
        return neoEnc.getVelocity();
    }

    @Override
    public void setOutputRange(double min, double max) {
        neoPID.setOutputRange(min, max);
    }

    @Override
    public double getCurrent() {
        return neo.getOutputCurrent();
    }

    @Override
    public double getVoltage() {
        return neo.getBusVoltage();
    }

    @Override
    public double getTemperature() {
        return neo.getMotorTemperature();
    }
    
}