package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.RobotContainer;
//import frc.robot.motors.TalonSRXMotor;
import frc.robot.subsystems.ControlPanelSubsystem;


/**
 *
 */
public class RotateControlPanelCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private final ControlPanelSubsystem m_controlPanelSubsystem;

    private final int m_rotations = 3; // Setting this variable to 5 will not work correctly as of now
    private final int m_numOfRotationsToStop = (m_rotations * 2) + 1; 
    // The (rotations * 2) because there are two of the same color on the wheel. 
    // The + 1 is because that is when we have achieved the minimum number of rotations

    private boolean m_rotationComplete;
    private int m_rotationsOfStartColor;
    private boolean m_hasChangedColor;
    private final int m_rumbleTime = 1; // seconds
    private String m_colorString = "Unknown";
    private String m_startColor = "UnknownStartColor";

    //private TalonSRXMotor m_wheelTalonSRX;
    private WPI_TalonSRX m_wheelTalonSRX;

    public RotateControlPanelCommand(ControlPanelSubsystem controlPanel) {

        m_controlPanelSubsystem = controlPanel;
        //m_wheelTalonSRX = m_controlPanelSubsystem.getTalonSRX();
        m_wheelTalonSRX = m_controlPanelSubsystem.getNewTalonSRX();
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(m_controlPanelSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_rotationsOfStartColor = 0;

        //---------------------------------GetStartColor---------------------------------------
        m_startColor = m_controlPanelSubsystem.getCurrentColor();

        m_rotationComplete = false;
        m_hasChangedColor = false;

        System.out.println("Start Motor");
        //m_wheelTalonSRX.setVelocity(Constants.WHEEL_MOTOR_VELOCITY);
        m_wheelTalonSRX.set(0.1);

        //rumble controller
        CommandScheduler.getInstance().schedule(RobotContainer.getRumbleCommand().withTimeout(m_rumbleTime));
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

    //-------------------------------------GetCurrentColor---------------------------------------    
    m_colorString = m_controlPanelSubsystem.getCurrentColor();

    //----------------------------------IncrementRotations----------------------------------------
    if(!m_colorString.equals(m_startColor))
    {
        m_hasChangedColor = true; // the color is different and sensor has moved
    }
    else if(m_colorString.equals(m_startColor) && m_hasChangedColor) // If the current color string equals the the start color and it has changed color, 
    {                                                          // increment rotationsOfStartColor
        m_rotationsOfStartColor++;
        m_hasChangedColor = false;
    }

    if(m_rotationsOfStartColor == m_numOfRotationsToStop) // If we have reached the required amount of rotations,
    {                                                 // stop the motor and change rotationComplete to true;
        m_rotationComplete = true;
            System.out.println("Stop Motor");
        m_wheelTalonSRX.set(0);
        //m_wheelTalonSRX.set(0);
    }   

    //-------------------------------------SmartDashboardWrites--------------------------------------
    // SmartDashboard.putString("startColor", m_startColor);
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
        CommandScheduler.getInstance().schedule(RobotContainer.getRumbleCommand().withTimeout(m_rumbleTime));
    }
}
