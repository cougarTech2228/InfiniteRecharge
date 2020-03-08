package frc.robot.subsystems;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.commands.MethodCommand;
import frc.robot.util.Config;
import frc.robot.util.ConfigurableSubsystem;

public class ClimberSubsystem extends ConfigurableSubsystem {

    CANSparkMax climberMotor;
    Solenoid brake, deploy;
    DigitalInput topFlag, bottomFlag;
    private boolean m_startedClimb;

    
    public double climbSpeed = 0.9;

    public ClimberSubsystem() {
        // You need to register the subsystem to get it's periodic
        // method to be called by the Scheduler
        // register();

        climberMotor = new CANSparkMax(42, MotorType.kBrushless);

        m_startedClimb = false;

        brake = new Solenoid(1);
        deploy = new Solenoid(2);

        topFlag = new DigitalInput(5);
        bottomFlag = new DigitalInput(6);

        climberMotor.setIdleMode(IdleMode.kBrake);

        brake.set(false);

        bottomFlag.requestInterrupts(new InterruptHandlerFunction<Object>() {
            @Override
            public void interruptFired(int interruptAssertedMask, Object param) {
                System.out.println("Bottom Flag interrupt fired");
                climberMotor.set(0);
            }
        });
        bottomFlag.setUpSourceEdge(false, true);
        bottomFlag.enableInterrupts();

        topFlag.requestInterrupts(new InterruptHandlerFunction<Object>() {
            @Override
            public void interruptFired(int interruptAssertedMask, Object param) {
                System.out.println("Top Flag interrupt fired");
                climberMotor.set(0);
            }
        });
        topFlag.setUpSourceEdge(false, true);
        topFlag.enableInterrupts();
        /*
        new CommandToggler(
            new MethodCommand(() -> deploy.set(true))
                .andThen(() -> brake.set(false)),
            cmdRaiseToMaxPos(),
            null,
            cmdLowerToMaxPos().andThen(() -> brake.set(true))
        ).setCycle(true).setToggleButton(OI::getXboxAButton);
        */
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("TopFlag", topFlag.get());
        SmartDashboard.putBoolean("BottomFlag", bottomFlag.get());
    }
    public Command cmdRaiseToMaxPos() {
        return new MethodCommand(() -> climberMotor.set(climbSpeed));
        //    .loopWhile(() -> topFlag.get())
        //    .andThen(() -> climberMotor.set(0));
    }
    public Command cmdLowerToMaxPos() {
        return new MethodCommand(() -> climberMotor.set(-climbSpeed));
        //    .loopWhile(() -> bottomFlag.get())
        //    .andThen(() -> climberMotor.set(0));
    }

    public boolean isClimbing() {
        return m_startedClimb;
    }

    public void deployElevator() {
        brake.set(true);
        deploy.set(true);
    }
    public void retractElevator() {
        brake.set(false);
        deploy.set(false);
    }
    public Command cmdRaiseElevator() {
        return new MethodCommand(() -> {
            if(!topFlag.get()) {
                System.out.println("Can't raise elevator, top flag tripped");
            } else {
                m_startedClimb = true;
                RobotContainer.getAcquisitionSubsystem().deployAcquirer();
                brake.set(true);
                climberMotor.set(climbSpeed);
            }
        })
        .loop()
        .runOnEnd(() -> {
            //brake.set(true);
            climberMotor.set(0);
        });
    }
    public Command cmdLowerElevator() {
        return new MethodCommand(() -> {
            if(!bottomFlag.get()) {
                System.out.println("Can't lower elevator, bottom flag tripped");
            } else {
                brake.set(true);
                climberMotor.set(-climbSpeed);
            }
        })
        .loop()
        .runOnEnd(() -> {
            //brake.set(true);
            climberMotor.set(0);
        });
    }
}