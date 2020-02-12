package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class StorageSubsystem extends SubsystemBase {

    private DigitalInput m_inputBallChecker;
    private DigitalInput m_inputIndexAcquireChecker;
    private Spark m_drumSparkMotor;
    private boolean[] m_drumArray = { false, false, false, false, false }; // false if unoccupied, true if occupied
    private boolean m_hasBeenTripped;
    private int m_drumArrayIndex = 0;
    private boolean m_isFull;
    private boolean m_isShooting;
    private boolean m_isRepopulating;

    public StorageSubsystem() {
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        register();
        m_inputBallChecker = new DigitalInput(Constants.DIGITAL_IO_2);
        m_inputIndexAcquireChecker = new DigitalInput(Constants.DIGITAL_IO_1);

        m_drumSparkMotor = new Spark(Constants.DRUM_SPARK_PWM_ID);
        m_isFull = false;
        m_isShooting = false;
        m_hasBeenTripped = false;
        m_isRepopulating = false;
    }

    @Override
    public void periodic() {
        if (!m_isRepopulating) {

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
        }

        // Put code here to be run every loop
        SmartDashboard.putBooleanArray("m_drumArray", m_drumArray);
        SmartDashboard.putBoolean("m_isFull", m_isFull);
        SmartDashboard.putBoolean("isIndexAcquireCheckerBlocked", !m_inputIndexAcquireChecker.get());
        SmartDashboard.putBoolean("isAcquireSlotOccupied", !m_inputBallChecker.get());
        SmartDashboard.putBoolean("m_drumArray[0]", m_drumArray[0]);
        SmartDashboard.putBoolean("m_drumArray[1]", m_drumArray[1]);
        SmartDashboard.putBoolean("m_drumArray[2]", m_drumArray[2]);
        SmartDashboard.putBoolean("m_drumArray[3]", m_drumArray[3]);
        SmartDashboard.putBoolean("m_drumArray[4]", m_drumArray[4]);
        SmartDashboard.putNumber("index", m_drumArrayIndex);
    }

    /**
     * Set the repopulating variable 
     * @param isRepopulating the value to be set
     */
    public void setIsRepopulating(boolean isRepopulating) {
        m_isRepopulating = isRepopulating;
    }

    /**
     * Determines if the acquierer slot is occupied
     * @return if the the acquierer slot is occupied
     */
    public boolean isAcquireSlotOccupied() {
        return !m_inputBallChecker.get();
    }

    /**
     * Sets isShooting to the passed in value
     * 
     * @param isShooting the value to be passed in
     */
    public void setIsShooting(boolean value) {
        System.out.println("setting shooting mode: " + value);
        m_isShooting = value;
    }

    /**
     * Sets all elements in drumArray to false Sets m_isFull to false
     */
    public void resetDrum() { // TODO move this to shuffle board
        System.out.println("reset drum");
        for (int i = 0; i < m_drumArray.length; i++) {
            m_drumArray[i] = false;
        }
        m_isFull = false;
    }

    /**
     * Starts the drum spark motor
     */
    public void startDrumMotor() {
        System.out.println("start drum motor");
        m_drumSparkMotor.set(0.45);
    }

    /**
     * Starts the drum spark motor backwards
     */
    public void startDrumMotorBackwards() {
        m_drumSparkMotor.set(-0.45);
    }

    /**
     * Stops the drum spark motor
     */
    public void stopDrumMotor() {
        System.out.println("stop drum motor");
        m_drumSparkMotor.set(0);
    }

    /**
     * Gets the drumArray
     * 
     * @return the drumArray
     */
    public boolean[] getDrumArray() {
        return m_drumArray;
    }

    /**
     * Gets the current index of the drumArray
     * 
     * @return the current index of the drumArray
     */
    public int getDrumArrayIndex() {
        return m_drumArrayIndex;
    }

    /**
     * Indexs the array to the next element If the new index is the length (out of
     * bounds), set it back to 0
     */
    public void finishIndex() {
        m_drumArrayIndex++;

        if (m_drumArrayIndex == m_drumArray.length) {
            m_drumArrayIndex = 0;
        }
        m_hasBeenTripped = false;
    }

    /**
     * Returns the opposite value of the getter for the sensor as for example if the
     * getter returns true that means the sensor is not blocked.
     * 
     * @return If the acquire flag was tripped
     */
    public boolean isIndexAcquireCheckerBlocked() {
        return !m_inputIndexAcquireChecker.get();
    }

    /**
     * Checks if the drum is full (If all elements are true) Sets boolean m_isFull
     * to corresponding value
     */
    public void isDrumFull() {
        for (boolean slot : m_drumArray) {
            if (!slot) {
                m_isFull = false;
                return;
            }
        }
        m_isFull = true;
    }

    /**
     * Sets drum array at a specific index to a value
     * 
     * @param index the index that will be modified
     * @param value the value that will be set to the index
     */
    public void setDrumArray(int index, boolean value) {
        m_drumArray[index] = value;
    }

    /**
     * Returns if the drum is full
     * 
     * @return boolean m_isFull
     */
    public boolean isDrumVariableFull() {
        return m_isFull;
    }
}