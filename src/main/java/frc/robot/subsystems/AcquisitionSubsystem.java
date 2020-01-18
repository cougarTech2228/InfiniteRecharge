package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.commands.MethodCommand;
import frc.robot.motors.TalonSRXMotor;

public class AcquisitionSubsystem extends SubsystemBase {
    private TalonSRXMotor acquisitionMotor;
    private double acquisitionMaxSpeed = 0.5;
    private Timer t;

    public AcquisitionSubsystem() {
        System.out.println("dumper init");
        acquisitionMotor = new TalonSRXMotor(15);
        t = new Timer();
        acquisitionMotor.set(0.25);
    }
    @Override
    public void periodic() {

    }
    
    public Command cmdSetAquisitionSpeed(double speed) {
        return new MethodCommand(() -> {
            acquisitionMotor.set(acquisitionMaxSpeed * speed);
        });
    }
    public Command cmdSetClosedLoop() {
        return new MethodCommand(() -> 
        {
            if(acquisitionMotor.getCurrent() > 2 && t.get() == 0) {
                t.start();
                acquisitionMotor.set(0.6);
            }
            else if(t.get() > 0.2) {
                t.stop();
                t.reset();
                acquisitionMotor.set(0.25);
            }
        },
        true
        )
        .runOnEnd(
            () -> acquisitionMotor.set(0)
        );
    }
}