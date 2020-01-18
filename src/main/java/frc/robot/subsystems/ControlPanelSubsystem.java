package frc.robot.subsystems;

//import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.RobotContainer;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;

import frc.robot.subsystems.ControlPanelSubsystem;
import frc.robot.motors.*;

import edu.wpi.first.wpilibj.DriverStation;
import java.util.logging.Logger;

/**
 *
 */
public class ControlPanelSubsystem extends SubsystemBase {

    private TalonSRXMotor m_wheelTalonSRX;
    private WPI_TalonSRX m_newWheelTalonSRX;
    private final static DigitalInput m_digitalInterrupt = new DigitalInput(Constants.DIGITAL_IO_0);
    private boolean hasFiredRotate;
    private boolean hasFiredPosition;

    private String gameData;

    private final I2C.Port m_i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(m_i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();

    private final Color m_kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color m_kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color m_kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color m_kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    private final Logger m_logger = Logger.getLogger(this.getClass().getName());

    public ControlPanelSubsystem() {
        System.out.println("controlpanelsubsystem");

        m_wheelTalonSRX = new TalonSRXMotor(Constants.CONTROL_PANEL_MOTOR_CAN_ID);
        m_newWheelTalonSRX = new WPI_TalonSRX(Constants.CONTROL_PANEL_MOTOR_CAN_ID);

        m_colorMatcher.addColorMatch(m_kBlueTarget);
        m_colorMatcher.addColorMatch(m_kGreenTarget);
        m_colorMatcher.addColorMatch(m_kRedTarget);
        m_colorMatcher.addColorMatch(m_kYellowTarget);

        //System.out.println("controlpanelsubsystem");
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        CommandScheduler.getInstance().registerSubsystem(this);
        hasFiredRotate = false;
        hasFiredPosition = false;
        // ----------------------------------RotateControlPanelInterrupt----------------------------------
        m_digitalInterrupt.requestInterrupts(new InterruptHandlerFunction<Object>() {

            @Override
            public void interruptFired(int interruptAssertedMask, Object param) {

                if (!hasFiredRotate) {
                    hasFiredRotate = true;
                    m_digitalInterrupt.disableInterrupts();
                    System.out.println("interruptFired");
                    m_logger.info("ControlPanel Digital Interrupt fired");
                    CommandScheduler.getInstance().schedule(RobotContainer.getRotateControlPanelCommand());
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

    // --------------------------------------PositionControlPanelButton-------------------------------
    //     if (OI.getXboxBButton() && !hasFiredPosition) {
    //         hasFiredPosition = true;
    //         System.out.println("B Button pressed");
    //         CommandScheduler.getInstance().schedule(RobotContainer.getPositionControlPanelCommand());
    // }

        // Put methods for controlling this subsystem
        // here. Call these from Commands.
        relatchInterrupt();
    }

    public TalonSRXMotor getTalonSRX() {
        return m_wheelTalonSRX;
    }

    public WPI_TalonSRX getNewTalonSRX() {
        return m_newWheelTalonSRX;
    }

    public void relatchInterrupt() {
        if (OI.getXboxAButton()) 
        {
            hasFiredPosition = false;
            hasFiredRotate = false;
            System.out.println("Reseting interrupt");
            m_digitalInterrupt.enableInterrupts();
        }
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
                //This is borked data
            }
        } 
        else 
        {
            //Code for no data received yet
            return "No Data Received yet";
        }
    }
}

