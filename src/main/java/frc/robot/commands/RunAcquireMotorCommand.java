package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.Constants;

/**
 * RunAcquireMotorCommand
 * 
 * 
 */
public class RunAcquireMotorCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private AcquisitionSubsystem m_acquisitionSubsystem;
    private Timer m_timer;
    private boolean m_isTeleOp;

    public RunAcquireMotorCommand(AcquisitionSubsystem acquisitionSubsystem, boolean isTeleOp) {

        m_acquisitionSubsystem = acquisitionSubsystem;
        m_timer = new Timer();
        m_isTeleOp = isTeleOp;
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
       //m_acquisitionSubsystem.setAcquirerSpeed(-0.5);
       System.out.println("Start acquirer"); 
    }

     // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // if(m_acquisitionSubsystem.getAcquisitionMotor().getCurrent() > 2 && m_timer.get() == 0) {
        //     m_timer.start();
        //     m_acquisitionSubsystem.setAcquirerSpeed(-0.6); 
        // }
        // else if(m_timer.get() > 0.2) {
        //     m_timer.stop();
        //     m_timer.reset();
        //     m_acquisitionSubsystem.setAcquirerSpeed(-0.5); 
        // }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if(m_isTeleOp) { 
            return false; 
        } else {
            return m_acquisitionSubsystem.getStopAcquirer();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        System.out.println("stop acquirer");
        //m_acquisitionSubsystem.setAcquirerSpeed(0);
        m_acquisitionSubsystem.setStopAcquirer(false);
    }
}
