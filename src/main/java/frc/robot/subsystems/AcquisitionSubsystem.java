package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.MethodCommand;
import frc.robot.motors.TalonSRXMotor;
import frc.robot.util.ShuffleboardAdapter;

public class AcquisitionSubsystem extends SubsystemBase {
    private TalonSRXMotor acquisitionMotor;
    private double acquisitionMaxSpeed = 0.5;
    private int r, g, b, a;
    private int state = 0;
    private Timer timer;

    public AcquisitionSubsystem() {
        register();

        System.out.println("dumper init");
        acquisitionMotor = new TalonSRXMotor(Constants.ACQUISITION_MOTOR_CAN_ID);
        timer = new Timer();
        /*
        new ShuffleboardAdapter("Acquirer")
            .inBox("Test2")
                .addColorBox("Color", () -> new Color8Bit(r, g, b))

                .addDoubleSlider("R", 0, value -> r = (int)value, 0, 255, 1)
                .addDoubleSlider("G", 0, value -> g = (int)value, 0, 255, 1)
                .addDoubleSlider("B", 0, value -> b = (int)value, 0, 255, 1);*/
    }
    @Override
    public void periodic() {
        
    }

    public void setAcquirerSpeed(double speed) {
        acquisitionMotor.set(acquisitionMaxSpeed * speed);
    }
    
    public Command cmdSetAquisitionSpeed(double speed) {
        return new MethodCommand(() -> {
            acquisitionMotor.set(acquisitionMaxSpeed * speed);
        });
    }
    public Command cmdSetClosedLoop() {
        return new SequentialCommandGroup(
            new MethodCommand(() -> acquisitionMotor.set(-0.5)),
            new MethodCommand(() -> 
            {
                if(acquisitionMotor.getCurrent() > 2 && timer.get() == 0) {
                    timer.start();
                    acquisitionMotor.set(-0.6);
                }
                else if(timer.get() > 0.2) {
                    timer.stop();
                    timer.reset();
                    acquisitionMotor.set(-0.5);
                }
                //System.out.println(acquisitionMotor.getCurrent());
                return false;
            }).runOnEnd(
                () -> acquisitionMotor.set(0)
            )
        );
    }
}