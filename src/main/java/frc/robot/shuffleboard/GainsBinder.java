package frc.robot.shuffleboard;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.commands.MethodCommand;
import frc.robot.motors.Gains;
import frc.robot.motors.MotorBase;

public class GainsBinder {
    private MotorBase motor;
    private String motorName;
    private List<Gains> gains;
    private List<NetworkTableEntry> values;

    public GainsBinder(String motorName, MotorBase motor, Gains... gains) {
        this.motorName = motorName;
        this.motor = motor;
        this.gains = new ArrayList<Gains>();
        this.values = new ArrayList<NetworkTableEntry>();
        for(int i = 0; i < gains.length; i++) {
            this.gains.add(gains[i]);
        }
        configShuffleboard();
    }
    public void configShuffleboard() {
        var box = Shuffleboard.getTab("Motors")
            .getLayout(motor.getClass().getSimpleName() + ": " + motorName, BuiltInLayouts.kList)
            .withSize(2, 4);
        for(int i = 0; i < gains.size(); i++) {
            int slot = i;
            values.add(box.add("kF", gains.get(slot).kF).withWidget(BuiltInWidgets.kTextView).getEntry());
            values.add(box.add("kP", gains.get(slot).kP).withWidget(BuiltInWidgets.kTextView).getEntry());
            values.add(box.add("kI", gains.get(slot).kI).withWidget(BuiltInWidgets.kTextView).getEntry());
            values.add(box.add("kD", gains.get(slot).kD).withWidget(BuiltInWidgets.kTextView).getEntry());
            values.add(box.add("IZone", gains.get(slot).iZone).withWidget(BuiltInWidgets.kTextView).getEntry());
            values.add(box.add("PeakOutput", gains.get(slot).peakOutput).withWidget(BuiltInWidgets.kTextView).getEntry());
        }

        new MethodCommand(
            () -> {
                for(int i = 0; i < gains.size(); i += 6) {
                    Gains newGains = new Gains(
                        values.get(i).getDouble(0),
                        values.get(i + 1).getDouble(0),
                        values.get(i + 2).getDouble(0),
                        values.get(i + 3).getDouble(0),
                        (int)values.get(i + 4).getDouble(0),
                        values.get(i + 5).getDouble(0)
                    );
                    if(!newGains.isEqualTo(gains.get(i / 6))) {
                        gains.set(i / 6, newGains);
                        motor.setPID(i / 6, newGains);
                        System.out.println(this);
                    }
                }
                System.out.println("running");
            },
            true
        ).schedule();
    }
    @Override
    public String toString() {
        String ret = "slot 0: " + gains.get(0).toString();
        for(int i = 1; i < gains.size(); i++) {
            ret += "slot " + i + ": " + gains.get(i);
        }
        return ret;
    }
}