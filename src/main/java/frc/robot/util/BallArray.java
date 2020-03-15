package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class BallArray implements TypedSendable {
    public int data;

    public void acquire() {
        data |= 1;
    }

    public void rotate() {
        data = ((data << 1) | (data >> 4)) & 31;
    }

    public void shoot() {
        data &= 27;
    }

    public void setAtIndex(int index, boolean occupied) {
        if (occupied)
            data |= (1 << index);
        else
            data ^= (1 << index);
    }

    public boolean getAtIndex(int index) {
        return (data | (1 << index)) == data;
    }

    public boolean isFull() {
        return data > 30;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("DrumWidget");
        builder.addDoubleProperty("DrumData", () -> data, null);
    }
    @Override
    public String widget() {
        return "DrumWidget";
    }
}