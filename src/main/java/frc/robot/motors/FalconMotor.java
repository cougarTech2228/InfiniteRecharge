package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class FalconMotor extends TalonMotorBase {

    public FalconMotor(int port) {
        super(port);
        talon = new TalonFX(port);
        talon.configFactoryDefault();
    }
}