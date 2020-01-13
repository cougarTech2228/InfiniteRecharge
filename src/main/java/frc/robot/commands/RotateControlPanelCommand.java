package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ControlPanelSubsystem;

/**
 *
 */
public class RotateControlPanelCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private final ControlPanelSubsystem m_controlPanelSubsystem;
    private final I2C.Port m_i2cPort = I2C.Port.kMXP;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(m_i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();

    private final Color m_kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color m_kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color m_kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color m_kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    private final int m_rotations = 3; // Setting this variable to 5 will not work correctly as of now
    private final int m_numOfRotationsToStop = (m_rotations * 2) + 1;
    // The (rotations * 2) because there are two of the same color on the wheel.
    // The + 1 is because that is when we have achieved the minimum number of
    // rotations

    private boolean m_isRotationComplete;
    private int m_rotationsOfStartColor;
    private boolean m_hasChangedColor;
    private String m_colorString = "Unknown";
    private String m_startColor = "UnknownStartColor";

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
        m_colorMatcher.addColorMatch(m_kBlueTarget);
        m_colorMatcher.addColorMatch(m_kGreenTarget);
        m_colorMatcher.addColorMatch(m_kRedTarget);
        m_colorMatcher.addColorMatch(m_kYellowTarget);

        m_rotationsOfStartColor = 0;

        // ---------------------------------GetStartColor---------------------------------------
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        if (match.color == m_kBlueTarget) {
            m_startColor = "Blue";
        } else if (match.color == m_kRedTarget) {
            m_startColor = "Red";
        } else if (match.color == m_kGreenTarget) {
            m_startColor = "Green";
        } else if (match.color == m_kYellowTarget) {
            m_startColor = "Yellow";
        } else {
            m_startColor = "UnknownStartColor";
        }

        m_isRotationComplete = false;
        m_hasChangedColor = false;

        System.out.println("Start Motor");

        // rumble controller
        CommandScheduler.getInstance().schedule(RobotContainer.getRumbleCommand().withTimeout(Constants.XBOX_RUMBLE_TIME));
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        // -------------------------------------GetCurrentColor---------------------------------------
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        if (match.color == m_kBlueTarget) {
            m_colorString = "Blue";
        } else if (match.color == m_kRedTarget) {
            m_colorString = "Red";
        } else if (match.color == m_kGreenTarget) {
            m_colorString = "Green";
        } else if (match.color == m_kYellowTarget) {
            m_colorString = "Yellow";
        } else {
            m_colorString = "Unknown";
        }
        // ----------------------------------IncrementRotations----------------------------------------

        if (!m_colorString.equals(m_startColor)) {
            m_hasChangedColor = true; // the color is different and sensor has moved
        } else if (m_colorString.equals(m_startColor) && m_hasChangedColor) // If the current color string equals the
                                                                            // the start color and it has changed color,
        { // increment rotationsOfStartColor
            m_rotationsOfStartColor++;
            m_hasChangedColor = false;
        }

        if (m_rotationsOfStartColor == m_numOfRotationsToStop) // If we have reached the required amount of rotations,
        { // stop the motor and change rotationComplete to true;
            m_isRotationComplete = true;
            System.out.println("Stop Motor");
        }

        // -------------------------------------SmartDashboardWrites--------------------------------------
        SmartDashboard.putString("startColor", m_startColor);
        SmartDashboard.putString("colorString", m_colorString);
        SmartDashboard.putBoolean("hasChangedColor", m_hasChangedColor);
        SmartDashboard.putNumber("rotationsOfStartColor", m_rotationsOfStartColor);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_isRotationComplete;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        CommandScheduler.getInstance().schedule(RobotContainer.getRumbleCommand().withTimeout(Constants.XBOX_RUMBLE_TIME));
    }
}
