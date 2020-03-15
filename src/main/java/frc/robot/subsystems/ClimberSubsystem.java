package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.MethodCommand;
import frc.robot.util.Config;
import frc.robot.util.ConfigurableSubsystem;

public class ClimberSubsystem extends ConfigurableSubsystem {

    CANSparkMax climberMotor;
    Solenoid brake, deploy;
    DigitalInput topFlag, bottomFlag;

    @Config
    public double climbSpeed = 0.05;

    public ClimberSubsystem() {
        register();

        climberMotor = new CANSparkMax(42, MotorType.kBrushless);

        brake = new Solenoid(1);
        deploy = new Solenoid(2);

        topFlag = new DigitalInput(5);
        bottomFlag = new DigitalInput(6);

        climberMotor.setIdleMode(IdleMode.kBrake);

        brake.set(false);

        bottomFlag.requestInterrupts(new InterruptHandlerFunction<Object>() {
            @Override
            public void interruptFired(int interruptAssertedMask, Object param) {
                climberMotor.set(0);
            }
        });
        bottomFlag.setUpSourceEdge(false, true);
        bottomFlag.enableInterrupts();

        topFlag.requestInterrupts(new InterruptHandlerFunction<Object>() {
            @Override
            public void interruptFired(int interruptAssertedMask, Object param) {
                climberMotor.set(0);
            }
        });
        topFlag.setUpSourceEdge(false, true);
        topFlag.enableInterrupts();
    }
    public Command cmdDeployElevator() {
        return new MethodCommand(() -> {
            deploy.set(true);
            ShooterSubsystem.shouldShoot.vote(false);
            DrivebaseSubsystem.shouldLowerSpeed.vote(true);
            AcquisitionSubsystem.shouldAcquirerExtend.vote(true);
            AcquisitionSubsystem.shouldAcquire.vote(false);
        });
    }
    public Command cmdRetractElevator() {
        return new MethodCommand(() -> {
            deploy.set(false);
            ShooterSubsystem.shouldShoot.vote(true);
            DrivebaseSubsystem.shouldLowerSpeed.vote(false);
            AcquisitionSubsystem.shouldAcquirerExtend.vote(false);
            AcquisitionSubsystem.shouldAcquire.vote(true);
        });
    }
    public Command cmdRaiseElevator() {
        return cmdDeployElevator()
        .andThen(
            new MethodCommand(() -> {
                brake.set(true);
                climberMotor.set(climbSpeed);
            })
            .runOnEnd(() -> climberMotor.set(0))
            .perpetually()
        );
    }
    public Command cmdLowerElevator() {
        return new MethodCommand(() -> {
            brake.set(false);
            climberMotor.set(-climbSpeed);
        })
        .runOnEnd(() -> climberMotor.set(0))
        .perpetually();
    }
}