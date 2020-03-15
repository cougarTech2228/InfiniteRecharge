package frc.robot.util;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

public class ShuffleboardAdapter {
    /**
     * @param s the sendable to be sent
     * @param location location on Shuffleboard ie: Smartdashboard/Title (Title being the name)
     */
    public static ComplexWidget add(Sendable sendable, String location) {
        String[] parts = location.split("/");
        SendableRegistry.add(sendable, parts[parts.length - 1]);
        ShuffleboardContainer tab = Shuffleboard.getTab(parts[0]);
        for(int i = 1; i < parts.length - 1; i++) {
            tab = tab.getLayout(parts[i], BuiltInLayouts.kList);
        }
        SendableRegistry.setName(sendable, location);
        
        return tab.add(sendable);
    }
    public static ComplexWidget add(TypedSendable sendable, String location) {
        return add((Sendable)sendable, location).withWidget(sendable.widget());
    }
}