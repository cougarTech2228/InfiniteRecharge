package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.MethodCommand;
import frc.robot.motors.TalonSRXMotor;
import frc.robot.util.ShuffleboardAdapter;

public class AcquisitionSubsystem extends SubsystemBase {
    private TalonSRXMotor m_acquisitionMotor;
    private double acquisitionMaxSpeed = 0.5;
    private int r, g, b, a;
    private int state = 0;
    private Timer timer;
    private boolean m_stopAcquirer = false;

    public AcquisitionSubsystem() {
        register();

        m_acquisitionMotor = new TalonSRXMotor(Constants.ACQUISITION_MOTOR_CAN_ID);
        timer = new Timer();
    }
    @Override
    public void periodic() {
        
    }

    public void setAcquirerSpeed(double speed) {
        m_acquisitionMotor.set(acquisitionMaxSpeed * speed);
    }
    
    public Command cmdSetAquisitionSpeed(double speed) {
        return new MethodCommand(() -> {
            m_acquisitionMotor.set(acquisitionMaxSpeed * speed);
        });
    }
    public Command cmdSetClosedLoop() {
        
        return new SequentialCommandGroup(
            new MethodCommand(() -> m_acquisitionMotor.set(-0.5)),
            new MethodCommand(() -> 
            {
                if(m_acquisitionMotor.getCurrent() > 2 && timer.get() == 0) {
                    timer.start();
                    m_acquisitionMotor.set(-0.6);
                }
                else if(timer.get() > 0.2) {
                    timer.stop();
                    timer.reset();
                    m_acquisitionMotor.set(-0.5);
                }
                //System.out.println(m_acquisitionMotor.getCurrent());
                return false;
            }).runOnEnd(
                () -> m_acquisitionMotor.set(0)
            )
        );
    }

    public void runAcquireMotor(double speed) {
        m_acquisitionMotor.set(speed);
    }

    public TalonSRXMotor getAcquisitionMotor() {
        return m_acquisitionMotor;
    }

    public boolean getStopAcquirer() {
        return m_stopAcquirer;
    }
    
    public void setStopAcquirer(boolean value) {
        m_stopAcquirer = value;
    }
}