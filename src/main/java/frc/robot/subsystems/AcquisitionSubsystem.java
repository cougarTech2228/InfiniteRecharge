package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.OI;

public class AcquisitionSubsystem extends SubsystemBase {
    private WPI_TalonSRX m_acquisitionMotor;
    private boolean m_isAcquiring;
    public AcquisitionSubsystem() {
        m_acquisitionMotor = new WPI_TalonSRX(Constants.ACQUISITION_MOTOR_CAN_ID);
        m_isAcquiring = false;
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        CommandScheduler.getInstance().registerSubsystem(this);

    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        if(OI.getXboxYButton())
        {
            
        }
    }
        
}