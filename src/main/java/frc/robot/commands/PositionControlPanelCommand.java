package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.ControlPanelSubsystem;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 * PositionControlPanelCommand
 * 
 */
public class PositionControlPanelCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
 
    private ControlPanelSubsystem m_controlPanelSubsystem;

    private String m_startColor;
    private String m_colorStringPosition;
    private boolean m_isFinished = false;

    public PositionControlPanelCommand(ControlPanelSubsystem controlPanelSubsystem) {

        m_controlPanelSubsystem = controlPanelSubsystem;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_controlPanelSubsystem);
    }

      // Called when the command is initially scheduled.
      @Override
      public void initialize() {
          m_startColor = m_controlPanelSubsystem.parseGameData(); // get the game data color
          System.out.println("Start Motor, p");
          m_colorStringPosition = m_controlPanelSubsystem.getCurrentColor();
          m_controlPanelSubsystem.getWheelMotor().set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_SLOW);
      }
  
      // Called every time the scheduler runs while the command is scheduled.
      @Override
      public void execute() {

        m_colorStringPosition = m_controlPanelSubsystem.matchColor(m_controlPanelSubsystem.getCurrentColor());

        if (m_colorStringPosition.equals(m_startColor) 
                && m_controlPanelSubsystem.getConfidence() > 0.97) { 
                // check if the current color equals the data color in position
                System.out.println("Stop Motor");
                m_controlPanelSubsystem.getWheelMotor().set(-0.2);
                m_isFinished = true; // end method command and stop here
            }
            
            // SmartDashboard.putString("startColor", m_controlPanelSubsystem.matchColor(m_startColor));
            // SmartDashboard.putString("colorString", m_colorStringPosition);
            // SmartDashboard.putNumber("onColorIncrementer", m_isOnColorIncrementer);
      }
  
      // Returns true when the command should end.
      @Override
      public boolean isFinished() {
          return m_isFinished;
      }
  
      // Called once the command ends or is interrupted.
      @Override
      public void end(boolean interrupted) {
        new WaitCommand(0.3)
        .andThen(() -> m_controlPanelSubsystem.getWheelMotor().set(0))
        .andThen(RobotContainer.getRumbleCommand(1)).schedule();
      }
}
