package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonSRXMotor extends TalonMotorBase {
    
    public TalonSRXMotor(String name, int port) {
        super(name, port);
        talon = new TalonSRX(port);
        talon.configFactoryDefault();
    }
}