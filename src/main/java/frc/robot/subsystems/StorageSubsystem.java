package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.util.BallArray;

public class StorageSubsystem extends SubsystemBase {

    private DigitalInput m_inputAcquireBallChecker;
    private DigitalInput m_inputAcquirePositionChecker;
    private Spark m_drumSparkMotor;
    private boolean m_hasBeenTripped;
    private boolean m_isFull;
    private boolean m_isRepopulating;
    private BallArray m_ballArray = new BallArray();

    public StorageSubsystem() {
        register();

        m_inputAcquireBallChecker = new DigitalInput(Constants.ACQUIRE_BALL_DIO);
        m_inputAcquirePositionChecker = new DigitalInput(Constants.ACQUIRE_POSITION_DIO);
        m_drumSparkMotor = new Spark(Constants.DRUM_SPARK_PWM_ID);

        m_isFull = false;
        m_hasBeenTripped = false;
        m_isRepopulating = false;
        SendableRegistry.add(m_ballArray, "balls");

        Shuffleboard.getTab("drum")
        .add(m_ballArray)
        .withWidget("DrumWidget");

        //SmartDashboard.putData(m_ballArray);
    }

    @Override
    public void periodic() {

        if (!RobotContainer.getShooterSubsystem().getIsShooting()) 
        {
            if (!m_inputAcquireBallChecker.get() && !m_hasBeenTripped)  // is there a ball in the acquire position?
            {
                if (!m_isFull) // if the drum is full, dont try to check if it needs to rotate again
                {
                    if (!m_isRepopulating) 
                    {
                        System.out.println("Ball detected");
                        m_hasBeenTripped = true;
                        m_ballArray.acquire();

                            new SequentialCommandGroup(
                                new WaitCommand(0.1),
                                RobotContainer.getRotateDrumOneSectionCommand()
                            ).schedule();

                    } else { /* System.out.println("Ball detected, but robot is repopulating"); */ }

                } else { /* System.out.println("Ball detected, but robot is shooting"); */ }

            } else { /* System.out.println("Ball detected, but robot is full"); */  }
        }

        SmartDashboard.putBoolean("Is Robot Full", m_isFull);
        SmartDashboard.putBoolean("Is Acquire Flag Tripped", !m_inputAcquirePositionChecker.get());
        SmartDashboard.putBoolean("Is Acquire Slot Occupied", !m_inputAcquireBallChecker.get());
        SmartDashboard.putBoolean("Is Robot Empty", m_ballArray.isEmpty());
    }

    /**
     * Set the repopulating variable
     * 
     * @param isRepopulating the value to be set
     */
    public void setIsRepopulating(boolean isRepopulating) {
        m_isRepopulating = isRepopulating;
    }

    /**
     * Determines if the acquierer slot is occupied
     * 
     * @return if the the acquierer slot is occupied
     */
    public boolean isAcquireBallOccupied() {
        return !m_inputAcquireBallChecker.get();
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
        // System.out.println("start drum motor");
        m_drumSparkMotor.set(-Constants.DRUM_MOTOR_VELOCITY);
    }

    /**
     * Starts the drum spark motor backwards
     */
    public void startDrumMotorBackwards() {
        m_drumSparkMotor.set(Constants.DRUM_MOTOR_VELOCITY);
    }

    /**
     * Stops the drum spark motor
     */
    public void stopDrumMotor() {
        // System.out.println("stop drum motor");
        m_drumSparkMotor.set(0);
    }

    /**
     * Gets the ball array
     * 
     * @return the ball array instance
     */
    public BallArray getBallArray() {
        return m_ballArray;
    }

    /**
     * Indexs the array to the next element If the new index is the length (out of
     * bounds), set it back to 0
     */
    public void resetHasBeenTripped() {
        m_hasBeenTripped = false;
    }

    /**
     * Returns the opposite value of the getter for the sensor as for example if the
     * getter returns true that means the sensor is not blocked.
     * 
     * @return If the acquire flag was tripped
     */
    public boolean isAcquirePositionTripped() {
        return !m_inputAcquirePositionChecker.get(); // remove ! for new sensor (dio9)
    }

    /**
     * Checks if the drum is full (If all elements are true) Sets boolean m_isFull
     * to corresponding value
     */
    public void checkIfDrumFull() {
        m_isFull = m_ballArray.isFull();
    }
}