package frc.robot.motors;

/**
 * An immutable type used for defining PID-loop gains
 */
public class Gains {
    public final double kP;
    public final double kI;
    public final double kD;
    public final double kF;
    public final int iZone;
    public final double peakOutput;

    public Gains(double kP, double kI, double kD, double kF, int iZone, double peakOutput) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.iZone = iZone;
        this.peakOutput = peakOutput;
    }
    public Gains setP(double kP) {
        return new Gains(kP, kI, kD, kF, iZone, peakOutput);
    }
    public Gains setI(double kI) {
        return new Gains(kP, kI, kD, kF, iZone, peakOutput);
    }
    public Gains setD(double kD) {
        return new Gains(kP, kI, kD, kF, iZone, peakOutput);
    }
    public Gains setF(double kF) {
        return new Gains(kP, kI, kD, kF, iZone, peakOutput);
    }
    public Gains setIntegralZone(int iZone) {
        return new Gains(kP, kI, kD, kF, iZone, peakOutput);
    }
    public Gains setPeakOutput(double peakOutput) {
        return new Gains(kP, kI, kD, kF, iZone, peakOutput);
    }

    public boolean isEqualTo(Gains test) {
        return
            test.kP == kP &&
            test.kI == kI &&
            test.kD == kD &&
            test.kF == kF &&
            test.iZone == iZone &&
            test.peakOutput == peakOutput;
    }
    @Override
    public String toString() {
        return
        "Gains(" +
        "kP: " + kP + ", " +
        "kI: " + kI + ", " +
        "kD: " + kD + ", " +
        "kF: " + kF + ", " +
        "iZone: " + iZone + ", " +
        "peakOutput: " + peakOutput + ")";
    }
}