package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());

    private static XboxController xboxIF;

    public OI() {

        xboxIF = new XboxController(0);
    }

    public static boolean getXboxAButton() {
        return xboxIF.getAButton();
    }

    public static boolean getXboxBButton() {
        return xboxIF.getBButton();
    }

    public static boolean getXboxXButton() {
        return xboxIF.getXButton();
    }

    public static boolean getXboxYButton() {
        return xboxIF.getYButton();
    }

    public static boolean getXboxStartButton() {
        return xboxIF.getStartButton();
    }

    public static boolean getXboxBackButton() {
        return xboxIF.getBackButton();
    }

    public static boolean getXboxRightBumper() {
        return xboxIF.getBumper(Hand.kRight);
    }

    public static boolean getXboxLeftBumper() {
        return xboxIF.getBumper(Hand.kLeft);
    }

    public static boolean getXboxLeftJoystickPress() {
        return xboxIF.getStickButton(Hand.kLeft);
    }

    public boolean getXboxRightJoystickPress() {
        return xboxIF.getStickButton(Hand.kRight);
    }

    public static double getXboxRightTrigger() {
        return xboxIF.getTriggerAxis(Hand.kRight);
    }

    public static double getXboxLeftTrigger() {
        return xboxIF.getTriggerAxis(Hand.kLeft);
    }

    public static double getXboxRightJoystickX() {
        return xboxIF.getX(Hand.kRight);
    }

    public static double getXboxRightJoystickY() {
        return xboxIF.getY(Hand.kRight);
    }

    public static double getXboxLeftJoystickX() {
        return xboxIF.getX(Hand.kLeft);
    }

    public static double getXboxLeftJoystickY() {
        return xboxIF.getY(Hand.kLeft);
    }

    public static boolean getXboxDpadUp() {
        int pov = xboxIF.getPOV(0);
        return (((pov >= 0) && (pov <= 45)) || ((pov >= 315) && (pov <= 360)));
    }

    public static boolean getXboxDpadRight() {
        int pov = xboxIF.getPOV(0);
        return ((pov >= 45) && (pov <= 135));
    }

    public static boolean getXboxDpadDown() {
        int pov = xboxIF.getPOV(0);
        return ((pov >= 135) && (pov <= 225));
    }

    public static boolean getXboxDpadLeft() {
        int pov = xboxIF.getPOV(0);
        return ((pov >= 225) && (pov <= 315));
    }

    public static void setXboxRumbleSpeed(double rumbleSpeed) {
        xboxIF.setRumble(RumbleType.kLeftRumble, rumbleSpeed);
        xboxIF.setRumble(RumbleType.kRightRumble, rumbleSpeed);
    }

    public static void setXboxLeftRumbleSpeed(double rumbleSpeed) {
        xboxIF.setRumble(RumbleType.kLeftRumble, rumbleSpeed);
    }

    public static void setXboxRightRumbleSpeed(double rumbleSpeed) {
        xboxIF.setRumble(RumbleType.kRightRumble, rumbleSpeed);
    }

    public static void setXboxRumbleStop() {
        OI.setXboxRumbleSpeed(0);
    }
}
