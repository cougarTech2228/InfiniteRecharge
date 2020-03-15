package frc.robot.motors;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.playingwithfusion.CANVenom;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;

import frc.robot.util.Config;
import frc.robot.util.Configuration;

public class Motor {
    @Config(readonly = true)
    private final int port;
    
    @Config
    double kF = 0, kP = 0, kI = 0, kD = 0, targetPos = 0, targetVel = 0, targetOutputs = 0;

    @Config
    int kIzone = 0;

    private TalonSRX talonController;

    private TalonFX falconController;

    private CANVenom venomController;

    private CANSparkMax neoController;
    private CANEncoder neoEncoder;
    private CANPIDController neoPID;

    private final Runnable zeroEncoders;
    private final Consumer<Boolean> setMotorInverted, setEncoderInverted, setBrake;
    private final BiConsumer<Integer, Double> setF, setP, setI, setD, setIzone;
    private final Consumer<Double> setPos, setVel, setOutput, limitVoltage;
    private final Supplier<Double> getPos, getVel, getCurrent, getTemp, getVolt;
    private final Consumer<Motor> follow;

    public Motor(TalonSRX motor) {
        Configuration.create(this, this::updateValues);
        talonController = motor;
        talonController.configFactoryDefault();
        port = talonController.getDeviceID();

        setF = (slot, k) -> {talonController.config_kF(slot, k);};
        setP = (slot, k) -> {talonController.config_kP(slot, k);};
        setI = (slot, k) -> {talonController.config_kI(slot, k);};
        setD = (slot, k) -> {talonController.config_kD(slot, k);};
        setIzone = (slot, k) -> {talonController.config_IntegralZone(slot, k.intValue());};
        
        getPos = () -> 1.0 * talonController.getSelectedSensorPosition();
        getVel = () -> 1.0 * talonController.getSelectedSensorVelocity();
        getCurrent = () -> talonController.getSupplyCurrent();
        getVolt = () -> talonController.getBusVoltage();
        getTemp = () -> talonController.getTemperature();

        setPos = value -> talonController.set(ControlMode.Position, value);
        setVel = value -> talonController.set(ControlMode.Velocity, value);
        setOutput = value -> talonController.set(ControlMode.PercentOutput, value);

        zeroEncoders = () -> talonController.setSelectedSensorPosition(0);

        setMotorInverted = invert -> talonController.setInverted(invert);
        setEncoderInverted = invert -> talonController.setSensorPhase(invert);

        setBrake = brake -> talonController.setNeutralMode(brake ? NeutralMode.Brake : NeutralMode.Coast);
        limitVoltage = value -> {talonController.configVoltageCompSaturation(value); talonController.enableVoltageCompensation(true); };

        follow = follower -> {
            if(talonController == null) {

            } else {
                follower.talonController.follow(talonController);
            }
        };

    }
    enum BrakeMode {
        Brake(true), Coast(false);
        boolean value;
        BrakeMode(boolean value) {
            this.value = value;
        }
    };
    public void setPID(int slot, Gains pidGains) {
        setP(slot, pidGains.kP);
        setI(slot, pidGains.kI);
        setD(slot, pidGains.kD);
        setF(slot, pidGains.kF);
        setIntegralZone(slot, pidGains.iZone);
    }
    public void setP(int slot, double kP) {
        setP.accept(slot, kP);
    }
    public void setI(int slot, double kI) {
        setI.accept(slot, kI);
    }
    public void setD(int slot, double kD) {
        setD.accept(slot, kD);
    }
    public void setF(int slot, double kF) {
        setF.accept(slot, kF);
    }
    public void setIntegralZone(int slot, double kIzone) {
        setIzone.accept(slot, kIzone);
    }
    public void setVelocity(double vel) {
        setVel.accept(vel);
    }
    public void setPosition(double pos) {
        setPos.accept(pos);
    }
    public void setInverted(boolean inverted) {
        setMotorInverted.accept(inverted);
    }
    public void setEncoderInverted(boolean inverted) {
        setEncoderInverted.accept(inverted);
    }
    public void set(double percent) {
        setOutput.accept(percent);
    }
    public void zeroEncoder() {
        zeroEncoders.run();
    }
    public void limitVoltageTo(double volts) {
        limitVoltage.accept(volts);
    }
    public void follow(Motor leader) {
        follow.accept(leader);
    }
    public void setBrakeMode(BrakeMode brakeMode) {
        setBrake.accept(brakeMode.value);
    }
    public int getPort() {
        return port;
    }
    public double getPosition() {
        return getPos.get();
    }
    public double getVelocity() {
        return getVel.get();
    }
    public double getCurrent() {
        return getCurrent.get();
    }
    public double getVoltage() {
        return getVolt.get();
    }
    public double getTemperature() {
        return getTemp.get();
    }
    public void updateValues() {
        setP(0, kP);
        setI(0, kI);
        setD(0, kD);
        setIntegralZone(0, kIzone);
    }
}