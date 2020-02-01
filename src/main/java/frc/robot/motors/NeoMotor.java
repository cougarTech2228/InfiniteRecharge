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

    public NeoMotor(int port) {
        super(port);
        neo = new CANSparkMax(port, MotorType.kBrushless);
        neoEnc = neo.getEncoder();
        neoPID = neo.getPIDController();
        neo.restoreFactoryDefaults();
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
    public void setBrakeMode(BrakeMode brakeMode) {
        switch(brakeMode) {
            case Brake:
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

    @Override
    public void setP(int slot, double kP) {
        neoPID.setP(kP, slot);
    }

    @Override
    public void setI(int slot, double kI) {
        neoPID.setI(kI, slot);
    }

    @Override
    public void setD(int slot, double kD) {
        neoPID.setD(kD, slot);
    }

    @Override
    public void setF(int slot, double kF) {
        neoPID.setFF(kF, slot);
    }

    @Override
    public void setIntegralZone(int slot, double kP) {
        neoPID.setIZone(kP, slot);
    }
    
}