package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;


public class ClimberSubsystem extends SubsystemBase {

    public ClimberSubsystem() {
        
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        CommandScheduler.getInstance().registerSubsystem(this);

    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putNumber("Match Time", Timer.getMatchTime());
        
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}