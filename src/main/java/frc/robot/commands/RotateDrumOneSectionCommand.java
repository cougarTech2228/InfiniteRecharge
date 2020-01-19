package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.OI;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.motors.*;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;

/**
 * Rumble Controller
 * Needs a .withTimeout(time) when scheduled or it will vibrate infinitely
 */
public class RotateDrumOneSectionCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private TalonSRXMotor m_drumMotor;
    private StorageSubsystem m_storageSubsystem;
    private boolean isDoneSpinning;
    

    public RotateDrumOneSectionCommand(StorageSubsystem storageSubsystem) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_storageSubsystem = storageSubsystem;
        m_drumMotor = new TalonSRXMotor("drumMotor", Constants.DRUM_MOTOR_CAN_ID);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_drumMotor.set(0.2);
        isDoneSpinning = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if(!m_storageSubsystem.getIndexCheckerIsNotBlocked())
        {
            m_drumMotor.set(0);
            isDoneSpinning = true;
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isDoneSpinning;
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
