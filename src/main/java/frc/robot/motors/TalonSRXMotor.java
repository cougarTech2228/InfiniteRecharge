package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonSRXMotor extends TalonMotorBase {
    
    public TalonSRXMotor(int port) {
        super(port);
        talon = new TalonSRX(port);
        talon.configFactoryDefault();
    }
}