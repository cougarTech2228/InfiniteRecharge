package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.motors.TalonSRXMotor;
import frc.robot.subsystems.ControlPanelSubsystem;

import java.util.Arrays;



/**
 *
 */
public class PositionControlPanelCommand extends CommandBase {

    ControlPanelSubsystem m_controlPanelSubsystem;
    private String m_startColor;
    private String m_colorString;
    private boolean isPositioned = false;
    private final int m_rumbleTime = 1; // seconds
    private WPI_TalonSRX m_wheelTalonSRX;
    private String[] colorPositions = { "Yellow", "Red", "Green", "Blue" };

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    public PositionControlPanelCommand(ControlPanelSubsystem controlPanel) {
        m_controlPanelSubsystem = controlPanel;
        m_wheelTalonSRX = m_controlPanelSubsystem.getTalonSRX();
        //m_newWheelTalonSRX = new WPI_TalonSRX(Constants.CONTROL_PANEL_MOTOR_CAN_ID);
        System.out.println("Position control constructor");
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initial%ly scheduled.
    @Override
    public void initialize() {
        m_startColor = m_controlPanelSubsystem.parseGameData(); // get the game data color
        System.out.println("Start Motor, p");
        //m_wheelTalonSRX.setVelocity(Constants.WHEEL_MOTOR_VELOCITY);
        //m_newWheelTalonSRX.set(0.1);
        m_wheelTalonSRX.set(0.25);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        m_colorString = m_controlPanelSubsystem.getCurrentColor();

        if (matchColor(m_colorString).equals(m_startColor)) // check if the current color equals the data color
        {
            //in position
            System.out.println("Stop Motor");
            //m_newWheelTalonSRX.set(0);
            m_wheelTalonSRX.set(-0.5);
            isPositioned = true;
        }

        SmartDashboard.putString("startColor", m_startColor);
        SmartDashboard.putString("colorString", m_colorString);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isPositioned;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        CommandScheduler.getInstance().schedule(new WaitCommand(0.2).andThen(() -> m_wheelTalonSRX.set(0)));
        CommandScheduler.getInstance().schedule(RobotContainer.getRumbleCommand().withTimeout(m_rumbleTime));
    }

    public String matchColor(String str) {
        int index = Arrays.asList(colorPositions).indexOf(str);
        index += 2;
        if (index > colorPositions.length - 1) { // Since the game field color sensor is 2 colors off,  
            index -= 4; // the color you need is two off. This makes sure 
                        // traversing the color wheel doesn't go out of bounds        }
        }
        return colorPositions[index];
    }
}
