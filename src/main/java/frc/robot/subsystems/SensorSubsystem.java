package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//import frc.robot.Constants;

public class SensorSubsystem extends SubsystemBase {

    private DigitalInput m_input;

    public SensorSubsystem() {
        
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        CommandScheduler.getInstance().registerSubsystem(this);
        m_input = new DigitalInput(0);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        if (m_input.get()) {
            System.out.println("ON!");
        }
        else {
            System.out.println("off...");
        }
    }

}