package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.commands.ContinuousCommand;
import frc.robot.commands.MethodCommand;

public class ControlPanelSubsystem extends SubsystemBase {

    private WPI_TalonSRX m_newWheelTalonSRX;
    private String gameData;

    private final I2C.Port m_i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(m_i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();

    private final Color m_kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color m_kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color m_kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color m_kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    public ControlPanelSubsystem() {

        m_newWheelTalonSRX = new WPI_TalonSRX(Constants.CONTROL_PANEL_MOTOR_CAN_ID);

        m_colorMatcher.addColorMatch(m_kBlueTarget);
        m_colorMatcher.addColorMatch(m_kGreenTarget);
        m_colorMatcher.addColorMatch(m_kRedTarget);
        m_colorMatcher.addColorMatch(m_kYellowTarget);

        register();
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("FMS color", parseGameData());
    }

    public Command cmdSpinToColor(double speed) {
        return new MethodCommand(() -> gameData = parseGameData()).andThen(
            new ContinuousCommand(c -> c
                .when(
                    gameData.equals(getCurrentColor()) && getConfidence() > .97,
                    () -> m_newWheelTalonSRX.set(0)
                )
                .endAfterward()
            )
        );
    }
    private String spinStartColor;
    public Command cmdSpin4Times() {
        return new ContinuousCommand(c -> c
            .when(c.getIteration() == 0, () -> m_newWheelTalonSRX.set(0.5))
            .when(c.getIteration() == 4, () -> m_newWheelTalonSRX.set(0.2))
            .run(
                new WaitCommand(1).andThen(
                    new ContinuousCommand(c2 -> c2
                        .endWhen(spinStartColor.equals(getCurrentColor()) && getConfidence() > .97)
                    )
                )
            )
            .endAfter(4)
        )
        .runOnEnd(() -> m_newWheelTalonSRX.set(0))
        .beforeStarting(() -> spinStartColor = getCurrentColor());
    }
    public Command cmdSpinWhileActive() {
        return new MethodCommand(() -> m_newWheelTalonSRX.set(0.5))
            .runOnEnd(() -> m_newWheelTalonSRX.set(0))
            .perpetually();
    }
    public String getCurrentColor() 
    {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        if (match.color == m_kBlueTarget) {
            return "Blue";
        } else if (match.color == m_kRedTarget) {
            return "Red";
        } else if (match.color == m_kGreenTarget) {
            return "Green";
        } else if (match.color == m_kYellowTarget) {
            return "Yellow";
        } else {
            return "UnknownColor";
        }
    }
    public double getConfidence() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        return match.confidence;
    }

    public String parseGameData()
    {
        gameData = DriverStation.getInstance().getGameSpecificMessage();

        if(gameData.length() > 0)
        {
            switch (gameData.charAt(0)) 
            {
            case 'B':
                return "Blue";
            case 'G':
                return "Green";
            case 'R':
                return "Red";
            case 'Y':
                return "Yellow";
            default:
                return "bork";
            }
        } 
        else 
        {
            return "No Data Received yet";
        }
    }
}

