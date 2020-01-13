package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class FalconMotor extends TalonMotorBase {

    public FalconMotor(String name, int port) {
        super(name, port);
        talon = new TalonFX(port);
        talon.configFactoryDefault();
    }
}