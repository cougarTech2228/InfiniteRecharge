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
import frc.robot.Constants;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private final static OI m_oi = new OI();

  // Robot Subsystems
   private final static ControlPanelSubsystem m_controlPanelSubsystem = new ControlPanelSubsystem();
  private final static DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private final static BallDumpSubsystem m_dumperSubsystem = new BallDumpSubsystem();
  private final static AcquisitionSubsystem m_acquisitionSubsystem = new AcquisitionSubsystem();
  private final static StorageSubsystem m_storageSubsystem = new StorageSubsystem();
  private final static ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem(m_storageSubsystem);

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
    // stuff tucker wants
    // LB - shoot motor (toggle)
    // LT - shoot once
    // RB - Acquirer motor (toggle)
    // RT -Shoot entire drum
    // X - Rotation
    // Y - Position
    // dpad for climb

    // new Button(OI::getXboxXButton).whenPressed(getRotateControlPanelCommand());
    // new Button(OI::getXboxYButton).whenPressed(getPositionControlPanelCommand());

    new CommandToggler(
      m_acquisitionSubsystem.cmdSetClosedLoop(),
      null
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxRightBumper)
    .setCycle(true);

    // new CommandToggler( // TODO test this
    //   new InstantCommand(m_shooterSubsystem::startShooterMotor, m_shooterSubsystem),
    //   new InstantCommand(m_shooterSubsystem::stopShooterMotor, m_shooterSubsystem)
    // )
    // .setDefaultState(CommandState.Interruptible)
    // .setToggleButton(OI::getXboxLeftBumper)
    // .setCycle(true);
    
    //new Button(OI::getXboxLeftTrigger).whenPressed(shootonce());
    //new Button(OI::getXboxRightTrigger).whenPressed(shootentire());

    //------------------

    new Button(OI::getXboxLeftBumper).whenPressed(() -> m_storageSubsystem.resetDrum());

    new Button(OI::getXboxRightTriggerPressed).whenPressed(() -> m_shooterSubsystem.tryToShoot());

    new Button(OI::getXboxLeftTriggerPressed).whenPressed(() -> m_storageSubsystem.stopDrumMotor());

    new Button(OI::getXboxAButton).whenPressed(() -> m_storageSubsystem.startDrumMotor());

    new Button(OI::getXboxBButton).whenPressed(getRotateDrumOneSectionCommand());

    new Button(OI::getXboxXButton).whenPressed(() -> m_shooterSubsystem.startShooterMotor());
    new Button(OI::getXboxYButton).whenPressed(() -> m_shooterSubsystem.stopShooterMotor());

    

    new CommandToggler(
      m_drivebaseSubsystem.cmdUseArcadeDrive(),
      m_drivebaseSubsystem.cmdUseStraightDrive()
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxRightJoystickPress)
    .setCycle(true);


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

  public static RumbleCommand getRumbleCommand() {
    return new RumbleCommand();
  }

  public static BopperCommand getBopperCommand() {
    return new BopperCommand(m_shooterSubsystem);
  }
  
  public static RotateDrumOneSectionCommand getRotateDrumOneSectionCommand() {
    return new RotateDrumOneSectionCommand(m_storageSubsystem, m_shooterSubsystem);
  }

  public static PositionControlPanelCommand getPositionControlPanelCommand() {
    return new PositionControlPanelCommand(m_controlPanelSubsystem);
  }

  public static RotateControlPanelCommand getRotateControlPanelCommand() {
    return new RotateControlPanelCommand(m_controlPanelSubsystem);
  }

  // Subsystem Getters

  public static DrivebaseSubsystem getDrivebaseSubsystem() {
    return m_drivebaseSubsystem;
  }

   public static ControlPanelSubsystem getControlPanel() {
     return m_controlPanelSubsystem;
   }

  public static AcquisitionSubsystem getAcquisitionSubsystem(){
    return m_acquisitionSubsystem;
  }

  public static StorageSubsystem getStorageSubsystem(){
     return m_storageSubsystem;
  }

   public static ShooterSubsystem getShooterSubystem(){
     return m_shooterSubsystem;
   }

  
}
