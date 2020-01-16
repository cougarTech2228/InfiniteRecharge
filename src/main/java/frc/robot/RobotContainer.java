/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private final static OI m_oi = new OI();

  // Robot Subsystems
  //private final static DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private final static ControlPanelSubsystem m_controlPanelSubsystem = new ControlPanelSubsystem();

  // Robot Commands
  //private final static SampleCommand m_sampleCommand = new SampleCommand(/*m_subSystem*/);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    OI.bindButton(OI::getXboxXButton).whenPressed(getRotateControlPanelCommand());
  }

  public static OI getOI()
  {
    return m_oi;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new InstantCommand();
  }

  // Command Getters
  public static RotateControlPanelCommand getRotateControlPanelCommand() {
    return new RotateControlPanelCommand(m_controlPanelSubsystem);
  }

  public static PositionControlPanelCommand getPositionControlPanelCommand() {
    return new PositionControlPanelCommand(m_controlPanelSubsystem);
   }

  public static RumbleCommand getRumbleCommand() {
    return new RumbleCommand(m_controlPanelSubsystem);
  }

  // Subsystem Getters
  // public static DrivebaseSubsystem getDrivebaseSubsystem() {
  //   return m_drivebaseSubsystem;
  // }

  public static ControlPanelSubsystem getControlPanel() {
    return m_controlPanelSubsystem;
  }
}
