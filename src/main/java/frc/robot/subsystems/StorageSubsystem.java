package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.UnstoppableCommand;
import frc.robot.util.BallArray;

public class StorageSubsystem extends SubsystemBase {

    private DigitalInput m_inputAcquireSlotChecker;
    private DigitalInput m_inputAcquireFlagChecker;
    private Spark m_drumSparkMotor;
    private boolean[] m_drumArray = { false, false, false, false, false }; // false if unoccupied, true if occupied
    private boolean m_hasBeenTripped;
    private int m_drumArrayIndex = 0;
    private boolean m_isFull;
    private boolean m_isShooting;
    private boolean m_isRepopulating;
    private BallArray m_ballArray = new BallArray();

    public StorageSubsystem() {
        register();

        m_inputAcquireSlotChecker = new DigitalInput(Constants.ACQUIRE_SLOT_DIO);
        m_inputAcquireFlagChecker = new DigitalInput(Constants.ACQUIRE_FLAG_DIO);
        m_drumSparkMotor = new Spark(Constants.DRUM_SPARK_PWM_ID);

        m_isFull = false;
        m_isShooting = false;
        m_hasBeenTripped = false;
        m_isRepopulating = false;
        SendableRegistry.add(m_ballArray, "balls");

        Shuffleboard.getTab("drum")
        .add(m_ballArray)
        .withWidget("DrumWidget");
    }

    @Override
    public void periodic() {

        if (!m_inputAcquireSlotChecker.get() && !m_hasBeenTripped)  // is there a ball in the acquire position?
        {
            if (!m_isFull) // if the drum is full, dont try to check if it needs to rotate again
            {
                if (!m_isShooting) 
                {
                    if (!m_isRepopulating) 
                    {
                        m_hasBeenTripped = true;
                        m_ballArray.acquire();

                        new UnstoppableCommand(
                            new SequentialCommandGroup(
                                new WaitCommand(0.5),
                                RobotContainer.getRotateDrumOneSectionCommand()
                            )
                        ).schedule();

                    } else { /* System.out.println("Ball detected, but robot is repopulating"); */ }

                } else { /* System.out.println("Ball detected, but robot is shooting"); */ }

            } else { /* System.out.println("Ball detected, but robot is full"); */  }
        }

        SmartDashboard.putBoolean("Is Robot Full", m_isFull);
        SmartDashboard.putBoolean("Is Acquire Flag Tripped", !m_inputAcquireFlagChecker.get());
        SmartDashboard.putBoolean("Is Acquire Slot Occupied", !m_inputAcquireSlotChecker.get());
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
        return !m_inputAcquireSlotChecker.get();
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
    public void resetDrum() { 
        System.out.println("reset drum");
        m_ballArray.data = 0;
    }

    /**
     * Starts the drum spark motor
     */
    public void startDrumMotor() {
        System.out.println("start drum motor");
        m_drumSparkMotor.set(Constants.DRUM_MOTOR_VELOCITY);
    }

    /**
     * Starts the drum spark motor backwards
     */
    public void startDrumMotorBackwards() {
        m_drumSparkMotor.set(-Constants.DRUM_MOTOR_VELOCITY);
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

    public BallArray getBallArray() {
        return m_ballArray;
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
        m_hasBeenTripped = false;
    }

    /**
     * Returns the opposite value of the getter for the sensor as for example if the
     * getter returns true that means the sensor is not blocked.
     * 
     * @return If the acquire flag was tripped
     */
    public boolean isAcquireFlagTripped() {
        return !m_inputAcquireFlagChecker.get(); // remove ! for new sensor (dio9)
    }

    /**
     * Checks if the drum is full (If all elements are true) Sets boolean m_isFull
     * to corresponding value
     */
    public void isDrumFull() {
        m_isFull = m_ballArray.isFull();
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