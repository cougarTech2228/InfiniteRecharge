package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.Constants;
import frc.robot.subsystems.ControlPanelSubsystem;
import java.util.logging.Logger;

import java.util.Arrays;

/**
 *
 */
public class PositionControlPanelCommand extends CommandBase {

    ControlPanelSubsystem m_controlPanelSubsystem;
    private String m_startColor;
    private String m_colorString;
    private boolean m_isPositioned;
    private WPI_TalonSRX m_wheelTalonSRX;
    private String[] m_colorPositions = { "Yellow", "Red", "Green", "Blue" };
    private int isOnColorIncrementer;

    private final Logger m_logger = Logger.getLogger(this.getClass().getName());

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public PositionControlPanelCommand(ControlPanelSubsystem controlPanel) {
        m_controlPanelSubsystem = controlPanel;
        m_wheelTalonSRX = m_controlPanelSubsystem.getTalonSRX();
        isOnColorIncrementer = 0;
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_controlPanelSubsystem);
    }

    // Called when the command is initial%ly scheduled.
    @Override
    public void initialize() {
        m_isPositioned = false;
        m_startColor = m_controlPanelSubsystem.parseGameData(); // get the game data color
        System.out.println("Start Motor, p");
        m_colorString = m_controlPanelSubsystem.getCurrentColor();
        m_wheelTalonSRX.set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_SLOW);
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        m_colorString = m_controlPanelSubsystem.getCurrentColor();


        if (matchColor(m_colorString).equals(m_startColor) 
                            && m_controlPanelSubsystem.getConfidence() > 0.97) // check if the current color equals the data color
        {
            //in position
            System.out.println("Stop Motor");
            
                m_wheelTalonSRX.set(-0.2);
            
            m_isPositioned = true;
        }
        

        SmartDashboard.putString("startColor", matchColor(m_startColor));
        SmartDashboard.putString("colorString", m_colorString);
        SmartDashboard.putNumber("onColorIncrementer", isOnColorIncrementer);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_isPositioned;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        CommandScheduler.getInstance().schedule(new WaitCommand(0.3).andThen(() -> m_wheelTalonSRX.set(0)));
        CommandScheduler.getInstance().schedule(RobotContainer.getRumbleCommand().withTimeout(Constants.XBOX_RUMBLE_COMMAND_TIMEOUT));

        System.out.println("end position command");
    }

    public String matchColor(String str) {
        int index = Arrays.asList(m_colorPositions).indexOf(str);
        index += 2;
        if (index > m_colorPositions.length - 1) { // Since the game field color sensor is 2 colors off,  
            index -= 4; // the color you need is two off. This makes sure 
                        // traversing the color array doesn't go out of bounds        
        }
        return m_colorPositions[index];
    }
}
