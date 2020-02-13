/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
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

  // Robot Subsystems
   private final static ControlPanelSubsystem m_controlPanelSubsystem = new ControlPanelSubsystem();
  private final static DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private final static AcquisitionSubsystem m_acquisitionSubsystem = new AcquisitionSubsystem();
  private final static StorageSubsystem m_storageSubsystem = new StorageSubsystem();
  private final static ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem(m_storageSubsystem);
  private final static GarminLidarSubsystem m_garminLidarSubsystem = new GarminLidarSubsystem();
  private final static ClimberSubsystem m_climberSubsystem = new ClimberSubsystem();
  private final static VisionSubsystem m_visionSubsystem = new VisionSubsystem();

  // Robot Commands
  //private final static SampleCommand m_sampleCommand = new SampleCommand(/*m_subSystem*/);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    configureShuffleboardBindings();
  }


  // Use this method to define shuffleboard buttons or widgets
  private void configureShuffleboardBindings() {
    SmartDashboard.putData("Robot machine broke, reset", RobotContainer.getResetEverythingCommand()
                                                         .beforeStarting(() -> CommandScheduler.getInstance().cancelAll()));
    SmartDashboard.putData("Reset Drum Array", new PrintCommand("resetting drum array")
                                              .andThen(() -> m_storageSubsystem.resetDrum()));
    SmartDashboard.putData("Repopulate drum array", getRepopulateArrayCommand());

                                                         
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // stuff tucker wants
    // LB - shoot motor (toggle) -done
    // LT - shoot once -done
    // RB - Acquirer motor (toggle)
    // RT -Shoot entire drum -done
    // X - Rotation -done
    // Y - Position -done
    // dpad for climb

    new Button(OI::getXboxXButton).whenPressed(getRotateControlPanelCommand());
    new Button(OI::getXboxYButton).whenPressed(getPositionControlPanelCommand());
    new Button(OI::getXboxDpadUp).whenPressed(getRotateDrumOneSectionCommand());
    new Button(OI::getXboxDpadDown).whenPressed(() -> m_storageSubsystem.resetDrum());

    new Button(OI::getXboxLeftTriggerPressed)
        .whenPressed(getShootOnceCommand().beforeStarting(() -> m_shooterSubsystem.setIsShooting(true)));

    new CommandToggler( // Shoot Entire Drum Toggle - Left Dpad
      getShootEntireDrumCommand().beforeStarting(() -> m_shooterSubsystem.setIsShooting(true)),
      null // TODO when the command is interrrupted, the array does not always index properly. 
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxRightTriggerPressed)
    .setCycle(true);

    new CommandToggler( // Shooter Motor Toggle - Left Bumper
      new InstantCommand(m_shooterSubsystem::startShooterMotor, m_shooterSubsystem),
      new InstantCommand(m_shooterSubsystem::stopShooterMotor, m_shooterSubsystem)
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxLeftBumper)
    .setCycle(true);

    new CommandToggler( // Drum Motor Toggle - A Button
      new InstantCommand(m_storageSubsystem::startDrumMotor, m_storageSubsystem),
      new InstantCommand(m_storageSubsystem::stopDrumMotor, m_storageSubsystem)
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxAButton)
    .setCycle(true);

    new CommandToggler( // Drum Motor Backwards Toggle - B Button
      new InstantCommand(m_storageSubsystem::startDrumMotorBackwards, m_storageSubsystem),
      new InstantCommand(m_storageSubsystem::stopDrumMotor, m_storageSubsystem)
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxBButton)
    .setCycle(true);

    new CommandToggler( // Acquirer Motor Toggle - Right Bumper
      m_acquisitionSubsystem.cmdSetClosedLoop(),
      null
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxRightBumper)
    .setCycle(true);

    //------------------

    // new CommandToggler(
    //   m_drivebaseSubsystem.cmdUseArcadeDrive(),
    //   m_drivebaseSubsystem.cmdUseStraightDrive()
    // )
    // .setDefaultState(CommandState.Interruptible)
    // .setToggleButton(OI::getXboxRightJoystickPress)
    // .setCycle(true);
    //m_drivebaseSubsystem.cmdUseArcadeDrive();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return getAutonomousOneCommand();
  }

  // Command Getters

  public static RumbleCommand getRumbleCommand() {
    return new RumbleCommand();
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

  public static TryToShootCommand getTryToShootCommand() {
    return new TryToShootCommand(m_shooterSubsystem, m_storageSubsystem);
  }

  public static ShootOnceCommand getShootOnceCommand() {
    System.out.println("getShootOnce");
    return new ShootOnceCommand(m_shooterSubsystem);
  }

  public static ShootEntireDrumCommand getShootEntireDrumCommand() {
    return new ShootEntireDrumCommand(m_shooterSubsystem);
  }

  public static BopperCommand getBopperCommand() {
    return new BopperCommand(m_shooterSubsystem);
  }

  public static ResetEverythingCommand getResetEverythingCommand() {
    return new ResetEverythingCommand(m_storageSubsystem, m_shooterSubsystem, 
                                      m_garminLidarSubsystem, m_drivebaseSubsystem, 
                                      m_acquisitionSubsystem, m_climberSubsystem, 
                                      m_controlPanelSubsystem, m_visionSubsystem);
  }

  public static AutonomousOneCommand getAutonomousOneCommand() {
    return new AutonomousOneCommand(m_storageSubsystem, m_shooterSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem);
  }

  public static RepopulateArrayCommand getRepopulateArrayCommand() {
    return new RepopulateArrayCommand(m_storageSubsystem);
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

   public static GarminLidarSubsystem getGarminLidarSubsystem() {
     return m_garminLidarSubsystem;
   }
}
