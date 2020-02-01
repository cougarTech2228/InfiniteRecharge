package frc.robot.subsystems;

import java.lang.Runnable;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import frc.robot.Constants;
import frc.robot.commands.MethodCommand;
import java.util.Map;

public class DrumSubsystem extends SubsystemBase {

    private DigitalInput m_inputBallChecker;
    private DigitalInput m_inputIndexChecker;
    private DigitalInput m_shootBallChecker;
    private DigitalInput m_shootIndexChecker;
    private boolean isShooting = false;
    //private TalonSRXMotor m_drumMotor;
    private Spark m_drumSparkMotor;
    private boolean[] drumArray = { false, false, false, false, false }; // false if unoccupied, true if occupied
    private Solenoid bopper;

    public enum StorageState {
        Shooting(true),
        Acquiring(false);

        boolean value;
        StorageState(boolean value) {
            this.value = value;
        }
    }

    public DrumSubsystem() {

        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        register();
        m_inputBallChecker = new DigitalInput(Constants.DIGITAL_IO_2);
        m_inputIndexChecker = new DigitalInput(Constants.DIGITAL_IO_1);
        m_shootBallChecker = new DigitalInput(Constants.DIGITAL_IO_3);
        m_shootIndexChecker = new DigitalInput(Constants.DIGITAL_IO_4);
        // m_drumMotor = new TalonSRXMotor(-1s);
        m_drumSparkMotor = new Spark(Constants.DRUM_SPARK_PWM_ID);

        bopper = new Solenoid(0);

        new Trigger(() -> !m_inputBallChecker.get()).whenActive(cmdRotateDrumOnce());

        // new ShuffleboardAdapter("")
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop6
        /*
         * if(OI.getXboxYButton()) { m_drumSparkMotor.set(0.5); } else {
         * m_drumSparkMotor.set(0); } /* if(OI.getXboxRightBumper()) { bopper.set(true);
         * } else { bopper.set(false); }
         */
        /*
         * 
         * if (m_inputIndexChecker.get()) { //System.out.println("ON!");
         * isIndexerNotBlocked = true; } else { //System.out.println("off...");
         * isIndexerNotBlocked = false; }
         * 
         * if (m_inputBallChecker.get()) { //System.out.println("ON!");
         * isCheckerNotBlocked = true; } else { //System.out.println("off...");
         * isCheckerNotBlocked = false; }
         * 
         * if (OI.getXboxLeftBumper()) // temporary, remove in the end { for(int i = 0;
         * i < drumArray.length; i++) { drumArray[i] = false; } isFull = false; }
         * 
         * if (!isFull) // if the drum is full, dont try to check if it needs to rotate
         * again { if(!m_inputBallChecker.get() && !hasBeenTripped) // something tripped
         * the laser sensor and it hasn't done it before { hasBeenTripped = true;
         * drumArray[drumArrayIndex] = true; cmdRotateDrum().schedule(); } }
         */

    }
    /**
     * Spins drum until a ball is loaded, then shoots
     */
    public Command cmdShootNext() {
        return cmdShootNext(5);
    }
    /**
     * This command independently schedules the TryShootOnce command while active to allow the driver to hold shoot while holding a button
     */
    public Command cmdShoot() {
        Command c = cmdTryShootOnce();
        return new MethodCommand(() -> c.schedule()).andThen(
            new MethodCommand(() -> {
                if(!c.isScheduled()) {
                    c.schedule();
                }
                return false;
            }).runOnEnd(() -> cmdSetMode(StorageState.Acquiring).schedule())
        );
    }
    /**
     * Spins drum until a ball is loaded, then shoots
     * @param maxTries The maximum number of times it rotates before ending the command
     */
    public Command cmdShootNext(int maxTries) {
        if(maxTries == 0) return new PrintCommand("Complete!");
        return new SequentialCommandGroup(
            cmdSetMode(StorageState.Shooting),
            new SelectCommand(
                Map.of(
                    true, cmdBop()
                        .andThen(new PrintCommand("Complete!")),
                    false, cmdRotateDrumOnce()
                        .andThen(new WaitCommand(0.04))
                        .andThen(cmdShootNext(maxTries - 1))
                ), () -> !m_shootBallChecker.get()
            )
        );
    }
    /**
     * Spins the drum one full revolution, while shooting all cells
     */
    public Command cmdShootAll() {
        return new SequentialCommandGroup(
            cmdSetMode(StorageState.Shooting),
            cmdTryShootOnce(),
            cmdTryShootOnce(),
            cmdTryShootOnce(),
            cmdTryShootOnce(),
            cmdTryShootOnce(),
            cmdSetMode(StorageState.Acquiring)
        );
    }
    /**
     * 
     */
    public Command cmdSetMode(StorageState state) {
        return new SelectCommand(
            Map.of(
                true, new MethodCommand(() -> isShooting = state.value)
                    .andThen(cmdRotateDrumOnce())
            ),
            () -> isShooting != state.value
        );
    }
    /**
     * Rotate the drum 1 index based on shooting mode
     */
    public Command cmdRotateDrumOnce() {
        return new SequentialCommandGroup(
            new MethodCommand(() -> m_drumSparkMotor.set(0.4)),
            new WaitCommand(0.2),
            new MethodCommand(() -> {
                if(isShooting ? !m_shootIndexChecker.get() : !m_inputIndexChecker.get()) {
                    m_drumSparkMotor.set(0);
                    return true;
                }
                return false;
            })
        );
    }
    /*
    public Command cmdRotateDrumOnce() {
        return new SequentialCommandGroup(
            new MethodCommand(() -> m_drumSparkMotor.set(0.4)),
            new WaitCommand(0.5),
            new MethodCommand(() -> {
                if(!m_inputIndexChecker.get()) {
                    m_drumSparkMotor.set(0);
                    return true;
                }
                return false;
            })
        );
    }*/
    /**
     * If a ball is loaded, shoot it, then rotate
     */
    public Command cmdTryShootOnce() {
        return new SequentialCommandGroup(
            cmdSetMode(StorageState.Shooting),
            new SelectCommand(
                Map.of(
                    true, cmdBop()
                ), () -> !m_shootBallChecker.get()
            ),
            cmdRotateDrumOnce()
        );
    }
    /**
     * run the solenoid on/off quickly to bop a ball into the shooter
     */
    public Command cmdBop() {
        return new SequentialCommandGroup(
            new MethodCommand(() -> bopper.set(true)),
            new WaitCommand(0.1),
            new MethodCommand(() -> bopper.set(false))
        );
    }
    public boolean[] getDrumArray()
    {
        return drumArray;
    }
}