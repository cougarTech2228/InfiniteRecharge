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
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.BallDumpSubsystem.DumperState;
import frc.robot.util.CommandToggler;
import frc.robot.util.CommandToggler.CommandState;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private final static OI m_oi = new OI();
  private static boolean fireEntireDrum = false;

  // Robot Subsystems
  //private final static DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private final static ControlPanelSubsystem m_controlPanelSubsystem = new ControlPanelSubsystem();
  private final static DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private final static BallDumpSubsystem m_dumperSubsystem = new BallDumpSubsystem();
  private final static AcquisitionSubsystem m_acquisitionSubsystem = new AcquisitionSubsystem();
  private final static StorageSubsystem m_storageSubsytem = new StorageSubsystem();
  private final static ShooterSubsystem m_shooterSubsytem = new ShooterSubsystem();

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
    //Bind driveState Toggler to left bumper
    new CommandToggler(
      m_drivebaseSubsystem.cmdUseArcadeDrive(),
      m_drivebaseSubsystem.cmdUseStraightDrive()
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxLeftBumper)
    .setCycle(true);

    //Bind raise/lowering the dumper to right bumper
    /*
    new CommandToggler(
      m_dumperSubsystem.cmdSetPosition(DumperState.raised),
      m_dumperSubsystem.cmdSetPosition(DumperState.lowered)
    )
    .setToggleButton(OI::getXboxRightBumper)
    .setCycle(true);
    */
    //Bind enabling/disabling the aquirer closed loop to the right trigger
    new CommandToggler(
      m_acquisitionSubsystem.cmdSetClosedLoop(),
      null
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxRightTriggerPressed)
    .setCycle(true);

    new Button(OI::getXboxRightBumper).whenPressed(m_storageSubsytem.cmdBop());
    new Button(OI::getXboxYButton).whenHeld(m_storageSubsytem.cmdRunDrum());
    
    new CommandToggler(
      m_shooterSubsytem.cmdEnableShooter(),
      null
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxBButton)
    .setCycle(true);

    new Button(OI::getXboxAButton).whenPressed(m_storageSubsytem.cmdRotateDrumOnce());
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

  public static StartStopAcquisitionMotorCommand getStartAcquisitionMotorCommand() {
    return new StartStopAcquisitionMotorCommand(m_acquisitionSubsystem);
  }

  public static RunMotorCommand getRunMotorCommand() {
    return new RunMotorCommand();
  }
  
  // public static RotateDrumOneSectionCommand getRotateDrumOneSectionCommand() {
  //   return new RotateDrumOneSectionCommand(m_storageSubsystem);
  // }

  // public static ShootEntireDrumCommand getShootEntireDrumCommand() {
  //   return new ShootEntireDrumCommand(m_shooterSubsystem);
  // }

  // public static ShootSingleCellCommand getShootSingleCellCommand() {
  //   return new ShootSingleCellCommand(m_shooterSubsystem);
  // }

  // Subsystem Getters
  // public static DrivebaseSubsystem getDrivebaseSubsystem() {
  //   return m_drivebaseSubsystem;
  // }

  public static ControlPanelSubsystem getControlPanel() {
    return m_controlPanelSubsystem;
  }

  public static AcquisitionSubsystem getAcquisitionSubsystem(){
    return m_acquisitionSubsystem;
  }

  // public static StorageSubsystem getStorageSubsystem(){
  //   return m_storageSubsystem;
  // }

  // public static ShooterSubsystem getShooterSubystem(){
  //   return m_shooterSubsystem;
  // }

  
}
