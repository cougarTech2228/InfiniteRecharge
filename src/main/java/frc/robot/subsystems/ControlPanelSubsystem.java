package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;

import frc.robot.subsystems.ControlPanelSubsystem;

import edu.wpi.first.wpilibj.DriverStation;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * ControlPanelSubsystem
 */
public class ControlPanelSubsystem extends SubsystemBase {

    // ------------------Subsystem------------------------
    private WPI_TalonSRX m_wheelMotor;
    private final static DigitalInput m_digitalInterrupt = new DigitalInput(Constants.CONTROL_PANEL_INTERRUPT_DIO);
    private boolean m_hasFiredRotate;

    private String m_gameData;

    private final I2C.Port m_i2cPort = I2C.Port.kMXP;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(m_i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();

    // private final Color m_kBlueTarget = Color.kAqua;
    // private final Color m_kGreenTarget = Color.kLime;
    // private final Color m_kRedTarget = Color.kRed;
    // private final Color m_kYellowTarget = Color.kYellow;
    private final Color m_kBlueTarget = ColorMatch.makeColor(0.12, 0.42, 0.45);
    private final Color m_kGreenTarget = ColorMatch.makeColor(0.17, 0.57, 0.25);
    private final Color m_kRedTarget = ColorMatch.makeColor(0.51, 0.34, 0.13);
    private final Color m_kYellowTarget = ColorMatch.makeColor(0.32, 0.55, 0.12);
    private final Logger m_logger = Logger.getLogger(this.getClass().getName());

    private String[] m_colorPositions = { "Yellow", "Red", "Green", "Blue" };

    public ControlPanelSubsystem() {

        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        //register();

        m_wheelMotor = new WPI_TalonSRX(Constants.CONTROL_PANEL_MOTOR_CAN_ID);

        m_colorMatcher.addColorMatch(m_kBlueTarget);
        m_colorMatcher.addColorMatch(m_kGreenTarget);
        m_colorMatcher.addColorMatch(m_kRedTarget);
        m_colorMatcher.addColorMatch(m_kYellowTarget);

        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        CommandScheduler.getInstance().registerSubsystem(this);

        m_hasFiredRotate = false;

        // ----------------------------------RotateControlPanelInterrupt----------------------------------
        m_digitalInterrupt.requestInterrupts(new InterruptHandlerFunction<Object>() {

            @Override
            public void interruptFired(int interruptAssertedMask, Object param) {

                if (!m_hasFiredRotate) {
                    m_hasFiredRotate = true;
                    m_digitalInterrupt.disableInterrupts();

                    System.out.println("ControlPanel Digital Interrupt fired");
                    m_logger.info("ControlPanel Digital Interrupt fired");

                    RobotContainer.getRumbleCommand(1)
                    .andThen(RobotContainer.getRotateControlPanelCommand()).schedule();
                }
            }
        });

        m_digitalInterrupt.setUpSourceEdge(false, true);

        // Enable digital interrupt pin
        m_digitalInterrupt.enableInterrupts();
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        // Put methods for controlling this subsystem
        // here. Call these from Commands.
        SmartDashboard.putString("Current color", getCurrentColor());
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void relatchInterrupts() {
        m_hasFiredRotate = false;
        System.out.println("Resetting interrupt");
        m_digitalInterrupt.enableInterrupts();
    }

    public void setHasFiredRotate(boolean hasFiredRotate) {
        m_hasFiredRotate = false;
    }

    /** 
     * @return WPI_TalonSRX
     */
    public WPI_TalonSRX getWheelMotor() {
        return m_wheelMotor;
    }

    public void startMotor() {
        m_wheelMotor.set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_FAST);
    }

    public void stopMotor() {
        m_wheelMotor.set(0);
    }

    /** 
     * @param str
     * @return String
     */
    public String matchColor(String str) {
        int index = Arrays.asList(m_colorPositions).indexOf(str);
        index += 2;
        if (index > m_colorPositions.length - 1) { // Since the game field color sensor is 2 colors off,
            index -= 4; // the color you need is two off. This makes sure
                        // traversing the color array doesn't go out of bounds
        }
        return m_colorPositions[index];
    }
    
    /** 
     * @return double
     */
    public double getConfidence() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        // return "r:" + detectedColor.red + " g:" + detectedColor.green + " b:"
        // +detectedColor.blue;
        return match.confidence;
    }
    
    /** 
     * @return String
     */
    public String getCurrentColor() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        // return "r:" + detectedColor.red + " g:" + detectedColor.green + " b:"
        // +detectedColor.blue;
        // SmartDashboard.putNumber("red", detectedColor.red * 255);
        // SmartDashboard.putNumber("green", detectedColor.green * 255);
        // SmartDashboard.putNumber("blue", detectedColor.blue * 255);
        // SmartDashboard.putNumber("confidence", match.confidence);

        if (match.color == m_kBlueTarget) {
            return "Blue";
        } else if (match.color == m_kRedTarget) {
            return "Red";
        } else if (match.color == m_kGreenTarget) {
            return "Green";
        } else if (match.color == m_kYellowTarget) {
            return "Yellow";
        } else {
            // m_logger.severe("Unknown color seen");
            return "UnknownColor";
        }
    }

    /** 
     * @return String
     */
    public String parseGameData() {
        m_gameData = DriverStation.getInstance().getGameSpecificMessage();

        if (m_gameData.length() > 0) {
            switch (m_gameData.charAt(0)) {
            case 'B':
                return "Blue";
            case 'G':
                return "Green";
            case 'R':
                return "Red";
            case 'Y':
                return "Yellow";
            default:
                m_logger.severe("Unexpected FMS game data");
                return "bork";
            // This is borked data
            }
        } else {
            // Code for no data received yet
            return "No Data Received yet";
        }
    }
}
