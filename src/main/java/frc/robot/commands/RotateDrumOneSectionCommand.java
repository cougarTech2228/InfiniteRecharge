package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.motors.*;
import frc.robot.Constants;

/**
 * RotateDrumOneSectionCommand
 * 
 */
public class RotateDrumOneSectionCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private TalonSRXMotor m_drumMotor;
    private StorageSubsystem m_storageSubsystem;
    private boolean m_isDoneSpinning;
    
    public RotateDrumOneSectionCommand(StorageSubsystem storageSubsystem) {  
        m_storageSubsystem = storageSubsystem;
        m_drumMotor = new TalonSRXMotor(Constants.DRUM_MOTOR_CAN_ID);

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_storageSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_drumMotor.set(Constants.DRUM_MOTOR_VELOCITY);
        m_isDoneSpinning = false;
        System.out.println("start motor");
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if(!m_storageSubsystem.getIndexCheckerIsNotBlocked())
        {
            m_drumMotor.set(0);
            m_isDoneSpinning = true;
            System.out.println("stop motor");
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_isDoneSpinning;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        // m_storageSubsystem.finishIndex();
        // System.out.println("Waiting 2 seconds for motor to finish moving");
        // CommandScheduler.getInstance().schedule(new WaitCommand(2));
        // System.out.println("finished waiting");
    }
}
