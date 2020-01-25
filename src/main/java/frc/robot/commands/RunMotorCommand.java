package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.OI;

import frc.robot.motors.*;

/**
 * RunMotorCommand
 * 
 */
public class RunMotorCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private boolean isFinished = false;
    
    private NeoMotor m_neoMotor;

    public RunMotorCommand() {
        // Use addRequirements() here to declare subsystem dependencies.
        m_neoMotor = new NeoMotor(Constants.CONTROL_PANEL_MOTOR_CAN_ID);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_neoMotor.set(-1);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if(OI.getXboxDpadLeft())
        {
            isFinished = true;
        }
    }

    
    /** 
     * @return boolean
     */
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }

    
    /** 
     * @param interrupted
     */
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_neoMotor.set(0);
    }
}
