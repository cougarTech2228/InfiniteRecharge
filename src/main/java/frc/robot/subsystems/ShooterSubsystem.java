package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.motors.ShooterMotor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class ShooterSubsystem extends SubsystemBase {

    private ShooterMotor m_shooterMotor;
    private DigitalInput m_inputShooterSlotChecker;
    private DigitalInput m_inputShooterFlagChecker;
    private Solenoid m_bopper;
    private StorageSubsystem m_storageSubsystem;
    private GarminLidarSubsystem m_garminLidarSubsystem;
    private AcquisitionSubsystem m_acquisitionSubsystem;
    private boolean m_isShooting;

    public ShooterSubsystem(StorageSubsystem storageSubsystem, GarminLidarSubsystem garminLidarSubsystem, AcquisitionSubsystem acquisitionSubsystem) {
        register();

        m_storageSubsystem = storageSubsystem;
        m_garminLidarSubsystem = garminLidarSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;

        m_shooterMotor = new ShooterMotor();
        m_bopper = new Solenoid(Constants.PCM_CAN_ID, Constants.BOPPER_PCM_PORT);
        m_inputShooterSlotChecker = new DigitalInput(Constants.SHOOTER_SLOT_DIO);
        m_inputShooterFlagChecker = new DigitalInput(Constants.SHOOTER_FLAG_DIO);

        m_isShooting = false;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Shooter Velocity", m_shooterMotor.getTalon().getSelectedSensorVelocity());
        SmartDashboard.putBoolean("Is Shooter Slot Occupied", !m_inputShooterSlotChecker.get());
        SmartDashboard.putBoolean("Is Shooter Flag Blocked" , !m_inputShooterFlagChecker.get());
        SmartDashboard.putBoolean("Is Robot Shooting", m_isShooting);
    }
    
    /**
     * Returns the opposite value of the getter for the sensor as for example
     * if the getter returns true that means the sensor is not blocked.
     * @return If the shooter flag was tripped
     */
    public boolean isShooterFlagTripped() {
        return !m_inputShooterFlagChecker.get();
    }

    /**
     * Returns the opposite value of the getter for the sensor as for example
     * if the getter returns true that means the sensor is not blocked.
     * @return if the shooter slot is occupied by a powercell
     */
    public boolean isShooterSlotOccupied() {
        return !m_inputShooterSlotChecker.get();
    }

    /**
     * Raises the solenoid which pushes the powercell into the shooter motor
     */
    public void raiseBopper() {
        m_bopper.set(true);
    }

    /**
     * Lowers the solenoid
     */
    public void lowerBopper() {
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
        m_storageSubsystem.setIsShooting(isShooting);
        m_isShooting = isShooting;
    }

    public void setIsShootingTrue() {
        m_storageSubsystem.setIsShooting(true);
        m_isShooting = true;
    }

    public void setIsShootingFalse() {
        m_storageSubsystem.setIsShooting(false);
        m_isShooting = false;
    }

    /**
     * Starts the shooter motor, also sets the variable isShooting in the storage subsystem 
     * and in the shooter subsystem to true
     */
    public void startShooterMotor() {
        m_storageSubsystem.setIsShooting(true);
        m_acquisitionSubsystem.deployAcquirer();
        m_shooterMotor.start(m_garminLidarSubsystem.getAverage());
    }

    /**
     * Starts the shooter motor, also sets the variable isShooting in the storage subsystem 
     * and in the shooter subsystem to false. Rotates the drum back to acquire position.
     */
    public void stopShooterMotor() {
        m_storageSubsystem.setIsShooting(false);
        m_acquisitionSubsystem.retractAcquirer();
        m_shooterMotor.stop();
        m_isShooting = false;
        RobotContainer.getRotateDrumOneSectionCommand().schedule();
    }
}