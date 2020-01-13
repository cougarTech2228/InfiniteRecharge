package frc.robot.motors;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class Gains implements Sendable {
    public double kP;
    public double kI;
    public double kD;
    public double kF;
    public int iZone;
    public double peakOutput;

    public Gains(double kP, double kI, double kD, double kF, int iZone, double peakOutput) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.iZone = iZone;
        this.peakOutput = peakOutput;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gain");
        builder.addDoubleProperty("kP", () -> kP, value -> value = kP);
        builder.addDoubleProperty("kI", () -> kI, value -> value = kI);
        builder.addDoubleProperty("kD", () -> kD, value -> value = kD);
        builder.addDoubleProperty("kF", () -> kF, value -> value = kF);
        builder.addDoubleProperty("iZone", () -> iZone, value -> value = iZone);
        builder.addDoubleProperty("PeakOutput", () -> peakOutput, value -> value = peakOutput);
    }
}