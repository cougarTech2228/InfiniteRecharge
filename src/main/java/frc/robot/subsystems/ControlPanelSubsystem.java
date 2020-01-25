package frc.robot.subsystems;

//import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.commands.MethodCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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

import edu.wpi.first.wpilibj.util.Color;

/**
 *
 */
public class ControlPanelSubsystem extends SubsystemBase {

    //------------------Subsystem------------------------
    private WPI_TalonSRX m_wheelTalonSRX;
    private final static DigitalInput m_digitalInterrupt = new DigitalInput(Constants.DIGITAL_IO_0);
    private boolean m_hasFiredRotate;

    private String m_gameData;

    private final I2C.Port m_i2cPort = I2C.Port.kOnboard;
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

    //----------------------Position----------------------
    private String m_startColor;
    private String m_colorStringPosition;
    private String[] m_colorPositions = { "Yellow", "Red", "Green", "Blue" };
    private int m_isOnColorIncrementer;

    //----------------------Rotate-----------------------
    private final int m_rotations = 3; // Setting this variable to 5 will not work correctly as of now
    private final int m_numOfRotationsToStop = (m_rotations * 2) + 2;
    // The (rotations * 2) because there are two of the same color on the wheel.
    // The + 1 is because that is when we have achieved the minimum number of
    // rotations

    private boolean m_rotationComplete;
    private int m_rotationsOfStartColor;
    private boolean m_hasChangedColor;
    private String m_colorStringRotate = "Unknown";
    private final String m_redColor = "Red";
    private boolean hasFoundRed = false;
    

    public ControlPanelSubsystem() {

        m_wheelTalonSRX = new WPI_TalonSRX(Constants.CONTROL_PANEL_MOTOR_CAN_ID);

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
                    System.out.println("interruptFired");
                    m_logger.info("ControlPanel Digital Interrupt fired");
                    CommandScheduler.getInstance()
                            .schedule(
                                RobotContainer.getRumbleCommand().withTimeout(Constants.XBOX_RUMBLE_COMMAND_TIMEOUT)
                                .alongWith(cmdRotateControlPanel())
                            );
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

    public Command cmdPositionControlPanel()
    {
        return new SequentialCommandGroup(
            new MethodCommand(() -> { // initialize and constructor stuff
                m_isOnColorIncrementer = 0;
                m_startColor = parseGameData(); // get the game data color
                System.out.println("Start Motor, p");
                m_colorStringPosition = getCurrentColor();
                m_wheelTalonSRX.set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_SLOW);
            }),
            new MethodCommand(() -> { // periodic
                m_colorStringPosition = matchColor(getCurrentColor());

                if (m_colorStringPosition.equals(m_startColor) 
                        && getConfidence() > 0.97) { // check if the current color equals the data color
                        //in position
                        System.out.println("Stop Motor");
                        m_wheelTalonSRX.set(-0.2);
                        return true; // end method command and stop here
                    }
        

                    SmartDashboard.putString("startColor", matchColor(m_startColor));
                    SmartDashboard.putString("colorString", m_colorStringPosition);
                    SmartDashboard.putNumber("onColorIncrementer", m_isOnColorIncrementer);

                    return false;
            })
            .runOnEnd(
                new WaitCommand(0.3)
                .andThen(RobotContainer.getRumbleCommand()
                .withTimeout(Constants.XBOX_RUMBLE_COMMAND_TIMEOUT))
            )
        );
    }

    public Command cmdRotateControlPanel()
    {
        return new SequentialCommandGroup (
            new MethodCommand(() -> { // initialize and constructor stuff
                m_rotationsOfStartColor = 0;
                m_rotationComplete = false;
                m_hasChangedColor = false;
        
                System.out.println("Start Motor");
                m_wheelTalonSRX.set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_FAST);
            }),
            new MethodCommand(() -> { // periodic
                // -------------------------------------GetCurrentColor---------------------------------------
                m_colorStringRotate = getCurrentColor();

                // -------------------------------------FindRed-----------------------------------------------
                if (!hasFoundRed) 
                {
                    if(m_colorStringRotate.equals(m_redColor)) 
                    {
                        hasFoundRed = true;
                        m_wheelTalonSRX.set(Constants.CONTROL_PANEL_MOTOR_VELOCITY_FAST);
                        System.out.println("red found");
                    }
                } 
                else 
                {
                    // ----------------------------------IncrementRotations----------------------------------------
                    if (!m_colorStringRotate.equals(m_redColor)) 
                    {
                        m_hasChangedColor = true; // the color is different and sensor has moved
                    } 
                    else if ((m_colorStringRotate.equals(m_redColor) && (m_hasChangedColor))
                            && getConfidence() > 0.97) // If the current color is red, has changed color, and has a high confident rating.
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
                        System.out.println("Stop Motor");
                        return true;
                    }
                    
                }
                return false;
                // -------------------------------------SmartDashboardWrites--------------------------------------
                // SmartDashboard.putString("startColor", m_redColor);
                // SmartDashboard.putString("colorString", m_colorString);
                // SmartDashboard.putBoolean("hasChangedColor", m_hasChangedColor);
                // SmartDashboard.putNumber("rotationsOfStartColor", m_rotationsOfStartColor);
            })
            .runOnEnd( // end
                (RobotContainer.getRumbleCommand()
                .withTimeout(Constants.XBOX_RUMBLE_COMMAND_TIMEOUT))
            )
        );
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

    public Command cmdRelatchInterrupt() {
        return new MethodCommand(() -> {
            m_hasFiredRotate = false;
            System.out.println("Reseting interrupt");
            m_digitalInterrupt.enableInterrupts();
            }
        );
    }

    public double getConfidence() {
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        // return "r:" + detectedColor.red + " g:" + detectedColor.green + " b:"
        // +detectedColor.blue;
        return match.confidence;
    }

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
