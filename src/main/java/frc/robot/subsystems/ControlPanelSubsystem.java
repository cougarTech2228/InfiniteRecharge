package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.RobotContainer;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;

/**
 *
 */
public class ControlPanelSubsystem extends SubsystemBase {

    private WPI_TalonSRX m_wheelTalonSRX;
    private final static DigitalInput m_digitalInterrupt = new DigitalInput(Constants.DIGITAL_IO_0);
    private boolean m_hasFired;

    public ControlPanelSubsystem() {
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        CommandScheduler.getInstance().registerSubsystem(this);
        m_hasFired = false;
        
        // ----------------------------------RotateControlPanelInterrupt----------------------------------
        m_digitalInterrupt.requestInterrupts(new InterruptHandlerFunction<Object>() {

            @Override
            public void interruptFired(int interruptAssertedMask, Object param) {
                if(!m_hasFired)
                {
                    m_hasFired = true;
                    m_digitalInterrupt.disableInterrupts();
                    System.out.println("interruptFired");
                    CommandScheduler.getInstance().schedule(RobotContainer.getRotateControlPanelCommand());
                }
            }
          });

        m_digitalInterrupt.setUpSourceEdge(false, true);

        // Enable digital interrupt pin
        m_digitalInterrupt.enableInterrupts();
        // -----------------------------------------------------------------------------------------------
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        // Put methods for controlling this subsystem
        // here. Call these from Commands.
        relatchInterrupt();
    }

    public WPI_TalonSRX getTalonSRX()
    {
        return m_wheelTalonSRX;
    }

    public void relatchInterrupt()
    {
        if(OI.getXboxAButton())
        {
            m_hasFired = false;
            System.out.println("Resetting interrupt");
            m_digitalInterrupt.enableInterrupts();
        }
    }
}

