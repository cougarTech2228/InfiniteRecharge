package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;


public class ClimberSubsystem extends SubsystemBase {

    private DigitalInput m_upperProximitySensor;
    private DigitalInput m_lowerProximitySensor;
    

    public ClimberSubsystem() {
        
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        register();

        m_lowerProximitySensor = new DigitalInput(Constants.CLIMBER_LOWER_PROX_DIO);
        m_upperProximitySensor = new DigitalInput(Constants.CLIMBER_UPPER_PROX_DIO);

    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putNumber("Match Time", Timer.getMatchTime());
        SmartDashboard.putBoolean("Upper prox", !m_upperProximitySensor.get());
        SmartDashboard.putBoolean("Lower prox" , !m_lowerProximitySensor.get());
        
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}