package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.motors.ShooterMotor;
import frc.robot.util.ShuffleboardAdapter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.HashMap;

public class ShooterSubsystem extends SubsystemBase {

    private ShooterMotor m_shooterMotor;
    private DigitalInput m_inputBallShooterChecker;
    private DigitalInput m_indexShooterChecker;
    private Solenoid m_bopper;
    private StorageSubsystem m_storageSubsystem;
    private boolean m_isShooting;

    public ShooterSubsystem(StorageSubsystem storageSubsystem) {
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        register();

        m_storageSubsystem = storageSubsystem;

        m_shooterMotor = new ShooterMotor();
        m_bopper = new Solenoid(Constants.PCM_PORT_0);

        m_inputBallShooterChecker = new DigitalInput(Constants.DIGITAL_IO_3);
        m_indexShooterChecker = new DigitalInput(Constants.DIGITAL_IO_4);
        m_isShooting = false;


        new ShuffleboardAdapter("Shooter")
            .addDoubleText("target velocity", 0, value -> {
                //m_shooterMotor.set(ControlMode.Velocity, value);
                //System.out.println("Set it to: " + value);
            });
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("isShooterSlotOccupied", !m_inputBallShooterChecker.get());
        SmartDashboard.putBoolean("isAcquireIndexBlocked" , !m_indexShooterChecker.get());
        SmartDashboard.putBoolean("isShooting", m_isShooting);
    }
    /**
     * Returns the opposite value of the getter for the sensor as for example
     * if the getter returns true that means the sensor is not blocked.
     * @return If the shooter flag was tripped
     */
    public boolean isIndexShooterCheckerBlocked() {
        return !m_indexShooterChecker.get();
    }
    /**
     * Returns the opposite value of the getter for the sensor as for example
     * if the getter returns true that means the sensor is not blocked.
     * @return if the shooter slot is occupied by a powercell
     */
    public boolean isShooterSlotOccupied()
    {
        return !m_inputBallShooterChecker.get();
    }
    /**
     * Raises the solenoid which pushes the powercell into the shooter motor
     */
    public void raiseBopper()
    {
        m_bopper.set(true);
    }
    /**
     * Lowers the solenoid
     */
    public void lowerBopper()
    {
        m_bopper.set(false);
    }
    /**
     * Returns the boolean isShooting which determines the state of the drum slot location
     * @return boolean isShooting
     */
    public boolean getIsShooting() {
        return m_isShooting;
    }
    /**
     * Sets isShooting to the passed in value
     * @param isShooting
     */
    public void setIsShooting(boolean isShooting) {
        m_isShooting = isShooting;
    }


    public void startShooterMotor() {
        m_storageSubsystem.setIsShooting(true);
        m_shooterMotor.start();
    }

    public void stopShooterMotor() {
        m_storageSubsystem.setIsShooting(false);
        m_shooterMotor.stop();
        m_isShooting = false;
        RobotContainer.getRotateDrumOneSectionCommand().schedule();
    }
}