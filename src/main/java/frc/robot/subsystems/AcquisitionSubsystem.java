package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class AcquisitionSubsystem extends SubsystemBase {

    public AcquisitionSubsystem() {
        
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        CommandScheduler.getInstance().registerSubsystem(this);

    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        
    }

}