package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.commands.MethodCommand;
import frc.robot.motors.TalonSRXMotor;
import frc.robot.util.ShuffleboardAdapter;

import java.util.Arrays;

public class StorageSubsystem extends SubsystemBase {

    private DigitalInput m_inputBallChecker;
    private DigitalInput m_inputIndexChecker;
    private TalonSRXMotor m_drumMotor;
    private Spark m_drumSparkMotor;
    private boolean[] drumArray = {false, false, false, false, false}; // false if unoccupied, true if occupied
    private boolean hasBeenTripped = false;
    private int drumArrayIndex = 0;
    private Solenoid bopper;
    private boolean isIndexerNotBlocked = true;
    private boolean isCheckerNotBlocked = true;
    private boolean isFull;

    public StorageSubsystem() {
        System.out.println("subsystem constructor");
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        register();
        m_inputBallChecker = new DigitalInput(Constants.DIGITAL_IO_2);
        m_inputIndexChecker = new DigitalInput(Constants.DIGITAL_IO_1);
        //m_drumMotor = new TalonSRXMotor(-1s);
        m_drumSparkMotor = new Spark(Constants.DRUM_SPARK_PWM_ID);

        bopper = new Solenoid(0);
        isFull = false;

        //new ShuffleboardAdapter("")
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop6
        /*
        if(OI.getXboxYButton()) {
            m_drumSparkMotor.set(0.5);
        }
        else {
            m_drumSparkMotor.set(0);
        }
        /*
        if(OI.getXboxRightBumper()) {
            bopper.set(true);
        }
        else {
            bopper.set(false);
        }*/

        // Put code here to be run every loop

        SmartDashboard.putBooleanArray("m_drumArray", m_drumArray);
        SmartDashboard.putBoolean("m_isFull", m_isFull);
        SmartDashboard.putBoolean("isIndexCheckerNotBlocked", m_inputIndexChecker.get());
        SmartDashboard.putBoolean("isBallCheckerNotBlocked", m_inputBallChecker.get());
        SmartDashboard.putBoolean("m_drumArray[0]", m_drumArray[0]);
        SmartDashboard.putBoolean("m_drumArray[1]", m_drumArray[1]);
        SmartDashboard.putBoolean("m_drumArray[2]", m_drumArray[2]);
        SmartDashboard.putBoolean("m_drumArray[3]", m_drumArray[3]);
        SmartDashboard.putBoolean("m_drumArray[4]", m_drumArray[4]);
        SmartDashboard.putNumber("index", m_drumArrayIndex);

        if (OI.getXboxYButton()) // TODO just for testing
        {
            for(int i = 0; i < m_drumArray.length; i++)
            {
                m_drumArray[i] = false;
            }
            m_isFull = false;
        }

        if (!m_isFull) // if the drum is full, dont try to check if it needs to rotate again
        {
            if(!m_inputBallChecker.get() && !m_hasBeenTripped) // something tripped the laser sensor and it hasn't done it before
            {
                hasBeenTripped = true;
                drumArray[drumArrayIndex] = true;
                cmdRotateDrum().schedule();
            }
        }

    }
    public Command cmdRotateDrumOnce() {
        return new SequentialCommandGroup(
            new MethodCommand(() -> m_drumMotor.set(0.2)),
            new MethodCommand(() -> {
                if(!m_inputIndexChecker.get()) {
                    m_drumMotor.set(0);
                    return true;
                }
                return false;
            })
        );
    }
    public Command cmdRotateDrum() {
        return new SequentialCommandGroup(
            new MethodCommand(() -> m_drumMotor.set(0.2)),
            new MethodCommand(() -> {
                if(!m_inputIndexChecker.get()) {
                    m_drumMotor.set(0);
                    return true;
                }
                return false;
            }),
            new WaitCommand(1),
            new MethodCommand(() -> {
                finishIndex();
                isDrumFull();
            })
        );
    }
    public Command cmdRunDrum() {
        return new MethodCommand(() -> m_drumSparkMotor.set(0.5), true)
            .runOnEnd(() -> m_drumSparkMotor.set(0));
    }
    public Command cmdBop() {
        return new SequentialCommandGroup(
            new MethodCommand(() -> bopper.set(true)),
            new WaitCommand(0.1),
            new MethodCommand(() -> bopper.set(false))
        );
    }
    public boolean[] getDrumArray()
    {
        return m_drumArray;
    }

    public void finishIndex()
    {
        m_drumArrayIndex++;
        if (m_drumArrayIndex == m_drumArray.length) {
            m_drumArrayIndex = 0;
        }
        m_hasBeenTripped = false;
    }
    
    public boolean getIndexCheckerIsNotBlocked()
    {
        return m_inputIndexChecker.get();
    }
    
    public void isDrumFull()
    {
        for (boolean slot : m_drumArray)
        {
            if (!slot) {
                return;
            }
        }
        m_isFull = true;
    }

    public void setDrumArray(int index, boolean value)
    {
        m_drumArray[index] = value;
    }
}