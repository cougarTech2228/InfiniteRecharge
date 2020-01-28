package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ShooterSubsystem;

public class StorageSubsystem extends SubsystemBase {

    private DigitalInput m_inputBallChecker;
    private DigitalInput m_inputIndexChecker;
    private Spark m_drumSparkMotor;
    private boolean[] m_drumArray = { false, false, false, false, false }; // false if unoccupied, true if occupied
    private boolean m_hasBeenTripped = false;
    private int m_drumArrayIndex = 0;
    private boolean m_isFull;
    private boolean m_isShooting;

    public StorageSubsystem() {
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        register();
        m_inputBallChecker = new DigitalInput(Constants.DIGITAL_IO_2);
        m_inputIndexChecker = new DigitalInput(Constants.DIGITAL_IO_1);
        m_drumSparkMotor = new Spark(Constants.DRUM_SPARK_PWM_ID);
        m_isFull = false;
        m_isShooting = false;
        // new ShuffleboardAdapter("")

        // ----------------------------------IndexFlagInterrupt----------------------------------
        // m_inputIndexChecker.requestInterrupts(new InterruptHandlerFunction<Object>()
        // {

        // @Override
        // public void interruptFired(int interruptAssertedMask, Object param) {

        // System.out.println("m_inputIndexChecker - interruptFired");

        // m_hasBeenTripped = true;
        // }
        // });

        // m_inputIndexChecker.setUpSourceEdge(true, false);

        // // Enable digital interrupt pin
        // m_inputIndexChecker.enableInterrupts();
    }

    @Override
    public void periodic() {

        if (!m_isShooting) {
        
            if (!m_isFull) // if the drum is full, dont try to check if it needs to rotate again
            {
                if (!m_inputBallChecker.get() && !m_hasBeenTripped)// is there a ball in the acquire position?
                {
                    m_hasBeenTripped = true;
                    System.out.println("Ball occupied at index: " + m_drumArrayIndex);
                    m_drumArray[m_drumArrayIndex] = true;
                    RobotContainer.getRotateDrumOneSectionCommand().schedule();
                }
            }
        }
    // Put code here to be run every loop
    SmartDashboard.putBooleanArray("m_drumArray",m_drumArray);SmartDashboard.putBoolean("m_isFull",m_isFull);SmartDashboard.putBoolean("isIndexCheckerNotBlocked",m_inputIndexChecker.get());SmartDashboard.putBoolean("isBallCheckerNotBlocked",m_inputBallChecker.get());SmartDashboard.putBoolean("m_drumArray[0]",m_drumArray[0]);SmartDashboard.putBoolean("m_drumArray[1]",m_drumArray[1]);SmartDashboard.putBoolean("m_drumArray[2]",m_drumArray[2]);SmartDashboard.putBoolean("m_drumArray[3]",m_drumArray[3]);SmartDashboard.putBoolean("m_drumArray[4]",m_drumArray[4]);SmartDashboard.putNumber("index",m_drumArrayIndex);

    }

    public void setIsShooting(boolean value) {
        m_isShooting = value;
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void resetDrum() { // TODO move this to shuffle board
        System.out.println("reset drum");
        for (int i = 0; i < m_drumArray.length; i++) {
            m_drumArray[i] = false;
        }
        m_isFull = false;
    }

    public void startDrumMotor() {
        m_drumSparkMotor.set(0.45);
    }

    public void stopDrumMotor() {
        m_drumSparkMotor.set(0);
    }

    public boolean[] getDrumArray() {
        return m_drumArray;
    }

    public int getDrumArrayIndex() {
        return m_drumArrayIndex;
    }

    public void finishIndex() {
        m_drumArrayIndex++;

        if (m_drumArrayIndex == m_drumArray.length) {
            m_drumArrayIndex = 0;
        }
        System.out.println("setting has been tripped to false");
        m_hasBeenTripped = false;
    }

    public boolean isIndexCheckerBlocked() {
        return !m_inputIndexChecker.get();
    }

    public void isDrumFull() {
        for (boolean slot : m_drumArray) {
            if (!slot) {
                m_isFull = false;
                return;
            }
        }
        m_isFull = true;
    }

    public void setDrumArray(int index, boolean value) {
        m_drumArray[index] = value;
    }
}