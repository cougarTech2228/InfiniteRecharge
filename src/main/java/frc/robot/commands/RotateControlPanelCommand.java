package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanelSubsystem;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 * RotateControlPanelCommand
 * 
 */
public class RotateControlPanelCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
 
    private ControlPanelSubsystem m_controlPanelSubsystem;

    private String m_colorStringRotate = "Unknown";
    private int m_rotationsOfStartColor;
    private boolean m_hasChangedColor;
    private boolean m_isFinished = false;
    private boolean m_hasFoundRed = false;
    private final int m_rotations = 3; // Setting this variable to 5 will not work correctly as of now
    private final int m_numOfRotationsToStop = (m_rotations * 2) + 2;
    // The (rotations * 2) because there are two of the same color on the wheel.
    // The + 1 is because that is when we have achieved the minimum number of
    // rotations

    private final String RED_COLOR_STRING = "Red";

    public RotateControlPanelCommand(ControlPanelSubsystem controlPanelSubsystem) {

        m_controlPanelSubsystem = controlPanelSubsystem;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_controlPanelSubsystem);
    }

      // Called when the command is initially scheduled.
      @Override
      public void initialize() {

        m_rotationsOfStartColor = 0;
        m_hasChangedColor = false;

        System.out.println("Start Motor");
        m_controlPanelSubsystem.getWheelMotor().set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_FAST);
      }
  
      // Called every time the scheduler runs while the command is scheduled.
      @Override
      public void execute() {
        m_colorStringRotate = m_controlPanelSubsystem.getCurrentColor();

        // -------------------------------------FindRed-----------------------------------------------
        if (!m_hasFoundRed) 
        {
            if(m_colorStringRotate.equals(RED_COLOR_STRING)) 
            {
                m_hasFoundRed = true;
                m_controlPanelSubsystem.getWheelMotor().set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_FAST);
                System.out.println("red found");
            }
        } 
        else 
        {
            // ----------------------------------IncrementRotations----------------------------------------
            if (!m_colorStringRotate.equals(RED_COLOR_STRING)) 
            {
                m_hasChangedColor = true; // the color is different and sensor has moved
            } 
            else if ((m_colorStringRotate.equals(RED_COLOR_STRING) && (m_hasChangedColor))
                    && m_controlPanelSubsystem.getConfidence() > 0.97) // If the current color is red, has changed color, and has a high confident rating.
            {                                                          
                m_rotationsOfStartColor++; // increment rotationsOfStartColor
                m_hasChangedColor = false;
            }

            if ((m_rotationsOfStartColor) == (m_numOfRotationsToStop - 1)) // slow wheel down on last rotation
            {
                m_controlPanelSubsystem.getWheelMotor().set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_SLOW);
            }

            if (m_rotationsOfStartColor == m_numOfRotationsToStop) // If we have reached the required amount of rotations,
            {
                m_controlPanelSubsystem.getWheelMotor().set(0); // stop the motor and change rotationComplete to true;
                System.out.println("Stop Motor");
                m_isFinished = true;
            } 
        }

        // -------------------------------------SmartDashboardWrites--------------------------------------
        //SmartDashboard.putString("startColor", m_redColor);
        // SmartDashboard.putString("colorString", m_colorStringRotate);
        // SmartDashboard.putBoolean("hasChangedColor", m_hasChangedColor);
        // SmartDashboard.putNumber("rotationsOfStartColor", m_rotationsOfStartColor);
      }
  
      // Returns true when the command should end.
      @Override
      public boolean isFinished() {
          return m_isFinished;
      }
  
      // Called once the command ends or is interrupted.
      @Override
      public void end(boolean interrupted) {
        RobotContainer.getRumbleCommand(1).schedule();
      }
}