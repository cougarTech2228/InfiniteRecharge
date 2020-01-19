package frc.robot.subsystems;

import java.util.Map;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.commands.MethodCommand;
import frc.robot.motors.TalonSRXMotor;
import frc.robot.util.ShuffleboardAdapter;

public class AcquisitionSubsystem extends SubsystemBase {
    private TalonSRXMotor acquisitionMotor;
    private double acquisitionMaxSpeed = 0.5;
    private int r, g, b, a;
    private int state = 0;
    private Timer t;

    public AcquisitionSubsystem() {
        System.out.println("dumper init");
        acquisitionMotor = new TalonSRXMotor(15);
        t = new Timer();
        r = 0;
        g = 0;
        b = 0;

        new ShuffleboardAdapter("Acquirer")
            .addBox("Test2")
            .addColorBox("Color", () -> new Color8Bit(r, g, b))

            .addDouble("R", 0, value -> r = (int)value)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 255, "block increment", 1))
            
            .addDouble("G", 0, value -> g = (int)value)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 255, "block increment", 1))

            .addDouble("B", 0, value -> b = (int)value)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 255, "block increment", 1));
            //*/
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
        return new SequentialCommandGroup(
            new MethodCommand(() -> acquisitionMotor.set(0.25)),
            new MethodCommand(() -> 
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
                System.out.println(acquisitionMotor.getCurrent());
                return false;
            }).runOnEnd(
                () -> acquisitionMotor.set(0)
            )
        );
    }
}