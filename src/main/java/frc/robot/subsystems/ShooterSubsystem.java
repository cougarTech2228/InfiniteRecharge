package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.MethodCommand;
import frc.robot.util.ShuffleboardAdapter;

public class ShooterSubsystem extends SubsystemBase {
    TalonSRX shooterMotor;
    double maxVel = 109464;
    double kP = 0;
    double kI = 0;
    double kD = 0;
    double kF = 0;
    double kIzone = 0;
    double targetVel = 0;
    double percentOutput = 0;

    public ShooterSubsystem() {
        register();
        shooterMotor = new TalonSRX(Constants.SHOOTER_CAN_ID);
        shooterMotor.setSensorPhase(true);

        shooterMotor.configFactoryDefault();
        shooterMotor.configSelectedFeedbackSensor(
            FeedbackDevice.CTRE_MagEncoder_Relative,
            Constants.PID_PRIMARY,
            Constants.kTimeoutMs
        );
        new ShuffleboardAdapter("shooter")
            .addDoubleText("kP", 0, value -> {kP = value; shooterMotor.config_kP(0, kP); })
            .addDoubleText("kI", 0, value -> {kI = value; shooterMotor.config_kF(0, kI); })
            .addDoubleText("kD", 0, value -> {kD = value; shooterMotor.config_kI(0, kD); })
            .addDoubleText("kF", 0, value -> {kF = value; shooterMotor.config_kF(0, kF); })
            .addDoubleText("kIZone", 0, value -> {kIzone = value; shooterMotor.config_IntegralZone(0, (int)kIzone); })
            .addDoubleText("TargetVel", 0, value -> targetVel = value)
            .addDouble("Velocity Error", 0, () -> shooterMotor.getClosedLoopError())
            .addDouble("Velocity", 0, () -> shooterMotor.getSelectedSensorVelocity())
            .addDouble("Current", 0, () -> shooterMotor.getSupplyCurrent())
            .addDoubleText("Percent Power", 0, value -> percentOutput = value);
        /*
        shooterMotor.config_kP(0, (.8 * 1023.0) / 90, Constants.kTimeoutMs);
		shooterMotor.config_kI(0, 0.016, Constants.kTimeoutMs);
		shooterMotor.config_kD(0, (16 * 1023.0) / 90, Constants.kTimeoutMs);
        shooterMotor.config_kF(0, (.95 * 1023.0) / 700, Constants.kTimeoutMs);
        shooterMotor.config_IntegralZone(0, 4);*/
        
        shooterMotor.configNominalOutputForward(0, Constants.kTimeoutMs);
		shooterMotor.configNominalOutputReverse(0, Constants.kTimeoutMs);
        
        //shooterMotor.configClosedLoopPeriod(0, 1, Constants.kTimeoutMs);
        shooterMotor.configVoltageCompSaturation(11);
        shooterMotor.enableVoltageCompensation(true);

        //shooterMotor.configClosedloopRamp(0);

        shooterMotor.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
		shooterMotor.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);
    }
    @Override
    public void periodic() {
        //double throt = Math.abs(OI.getXboxRightJoystickY());
        //if(throt < 0.1) throt = 0;
        if(percentOutput != 0) {
            shooterMotor.set(ControlMode.PercentOutput, percentOutput);
            System.out.println("Percent Output: " + percentOutput);
        }
        else if(targetVel != 0) {
            shooterMotor.set(ControlMode.Velocity, targetVel);
            System.out.println("Velocity: " + shooterMotor.getSelectedSensorVelocity()
            + " Position: " + shooterMotor.getSelectedSensorPosition()
            + " Error: " + shooterMotor.getClosedLoopError());
        }
        else {
            shooterMotor.set(ControlMode.PercentOutput, 0);
        }
    }

    public Command cmdEnableShooter() {
        return new MethodCommand(() ->
            shooterMotor.set(ControlMode.PercentOutput, -1)
        )
        .loop()
        .runOnEnd(() -> shooterMotor.set(ControlMode.PercentOutput, 0)
        );
    }
}