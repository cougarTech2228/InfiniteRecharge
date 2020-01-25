package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanelSubsystem;
import frc.robot.Constants;

/**
 * RotateControlPanelCommand
 * 
 */
public class RotateControlPanelCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private final ControlPanelSubsystem m_controlPanelSubsystem;

    private final int m_rotations = 3; // Setting this variable to 5 will not work correctly as of now
    private final int m_numOfRotationsToStop = (m_rotations * 2) + 2;
    // The (rotations * 2) because there are two of the same color on the wheel.
    // The + 1 is because that is when we have achieved the minimum number of
    // rotations

    private boolean m_rotationComplete;
    private int m_rotationsOfStartColor;
    private boolean m_hasChangedColor;
    private String m_colorString = "Unknown";
    private final String m_redColor = "Red";
    private boolean hasFoundRed = false;

    private WPI_TalonSRX m_wheelTalonSRX;

    public RotateControlPanelCommand(ControlPanelSubsystem controlPanel) {
        m_controlPanelSubsystem = controlPanel;
        m_wheelTalonSRX = m_controlPanelSubsystem.getTalonSRX();

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_controlPanelSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_rotationsOfStartColor = 0;

        // ---------------------------------GetStartColor---------------------------------------

        m_rotationComplete = false;
        m_hasChangedColor = false;

        System.out.println("Start Motor");
        m_wheelTalonSRX.set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_FAST);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        // -------------------------------------GetCurrentColor---------------------------------------
        m_colorString = m_controlPanelSubsystem.getCurrentColor();

        // -------------------------------------FindRed-----------------------------------------------
        if (!hasFoundRed) 
        {
            if(m_colorString.equals(m_redColor)) 
            {
                hasFoundRed = true;
                m_wheelTalonSRX.set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_FAST);
                System.out.println("red found");
            }
        } 
        else 
        {
            // ----------------------------------IncrementRotations----------------------------------------
            if (!m_colorString.equals(m_redColor)) 
            {
                m_hasChangedColor = true; // the color is different and sensor has moved
            } 
            else if ((m_colorString.equals(m_redColor) && (m_hasChangedColor))
                    && m_controlPanelSubsystem.getConfidence() > 0.97) // If the current color is red, has changed color, and has a high confident rating.
            {                                                          
                m_rotationsOfStartColor++; // increment rotationsOfStartColor
                m_hasChangedColor = false;
            }

            if ((m_rotationsOfStartColor) == (m_numOfRotationsToStop - 1)) // slow wheel down on last rotation
            {
                m_wheelTalonSRX.set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_SLOW);
            }

            if (m_rotationsOfStartColor == m_numOfRotationsToStop) // If we have reached the required amount of rotations,
            {
                m_wheelTalonSRX.set(0); // stop the motor and change rotationComplete to true;
                m_rotationComplete = true;
                System.out.println("Stop Motor");
            }
        }

        // -------------------------------------SmartDashboardWrites--------------------------------------
        // SmartDashboard.putString("startColor", m_redColor);
        // SmartDashboard.putString("colorString", m_colorString);
        // SmartDashboard.putBoolean("hasChangedColor", m_hasChangedColor);
        // SmartDashboard.putNumber("rotationsOfStartColor", m_rotationsOfStartColor);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_rotationComplete;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {

    }
}
