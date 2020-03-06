/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.util.CommandToggler;
import frc.robot.util.TrajectoryManager;
import frc.robot.util.CommandToggler.CommandState;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // Initializes the xbox controller at port 0
  private final static OI m_oi = new OI();

  // Robot Subsystems
  // Since it might take some time for the navigation module (Pigeon or ADXRS450_Gyro) to
  // calibrate and come up, we should put this instantiation first.
  private final static NavigationSubsystem m_navigationSubsystem = new NavigationSubsystem();
  private final static TrajectoryManager m_trajectoryManager = new TrajectoryManager();
  private final static ControlPanelSubsystem m_controlPanelSubsystem = new ControlPanelSubsystem();
  private final static DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private final static AcquisitionSubsystem m_acquisitionSubsystem = new AcquisitionSubsystem();
  private final static StorageSubsystem m_storageSubsystem = new StorageSubsystem();
  private final static GarminLidarSubsystem m_garminLidarSubsystem = new GarminLidarSubsystem();
  private final static ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem(m_storageSubsystem,
      m_garminLidarSubsystem, m_acquisitionSubsystem);
  private final static ClimberSubsystem m_climberSubsystem = new ClimberSubsystem();

  private final static SendableChooser<Command> m_autoChooser = new SendableChooser<>();
  // private final static TrajectoryCommand m_centerTrajectoryCommand = new TrajectoryCommand(m_trajectoryManager.getCenterTrajectory(), m_drivebaseSubsystem);
  // private final static TrajectoryCommand m_leftTrajectoryCommand = new TrajectoryCommand(m_trajectoryManager.getLeftTrajectory(), m_drivebaseSubsystem);
  // private final static TrajectoryCommand m_rightTrajectoryCommand = new TrajectoryCommand(m_trajectoryManager.getRightTrajectory(), m_drivebaseSubsystem);
  // private final static TrajectoryCommand m_basicTrajectoryCommand = new TrajectoryCommand(m_trajectoryManager.getBasicTrajectory(), m_drivebaseSubsystem);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button and shuffleboard bindings
    configureButtonBindings();
    configureShuffleboardBindings();
  }

  // Use this method to define shuffleboard buttons or widgets
  private void configureShuffleboardBindings() {

    // Command Buttons

    SmartDashboard.putData("Robot machine broke, reset",
        RobotContainer.getResetEverythingCommand().beforeStarting(() -> CommandScheduler.getInstance().cancelAll()));
    SmartDashboard.putData("Reset Drum Array",
        new PrintCommand("resetting drum array").andThen(() -> m_storageSubsystem.resetDrum()));
    SmartDashboard.putData("Repopulate drum array", getRepopulateArrayCommand());

    // Autonomous Options

    SmartDashboard.putData("Auto Chooser", m_autoChooser);
    m_autoChooser.setDefaultOption("Minimum", getMinAutoCommand());
    m_autoChooser.addOption("Basic", getBasicAutoCommand());
    m_autoChooser.addOption("Center", getCenterAutoCommand());
    m_autoChooser.addOption("Left", getLeftAutoCommand());
    m_autoChooser.addOption("Right", getRightAutoCommand());
    
    // Diagnostic buttons

    SmartDashboard.putData("Rotate Drum Forwards", new InstantCommand(m_storageSubsystem::startDrumMotor, m_storageSubsystem));
    SmartDashboard.putData("Rotate Drum Backwards", new InstantCommand(m_storageSubsystem::startDrumMotorBackwards, m_storageSubsystem));
    SmartDashboard.putData("Stop Drum Motor", new InstantCommand(m_storageSubsystem::stopDrumMotor, m_storageSubsystem));
    SmartDashboard.putData("Change mode to shooting", new InstantCommand(() -> m_shooterSubsystem.setIsShooting(true), m_shooterSubsystem));
    SmartDashboard.putData("Change mode to acquiring", new InstantCommand(() -> m_shooterSubsystem.setIsShooting(false), m_shooterSubsystem));
    SmartDashboard.putData("Rotate drum one index", getRotateDrumOneSectionCommand());
    SmartDashboard.putData("Bopper", getBopperCommand());
    SmartDashboard.putData("Shake Dial", getShakeDial());
    SmartDashboard.putData("Run Acquirer Motor", new InstantCommand(m_acquisitionSubsystem::startAcquirerMotor));
    SmartDashboard.putData("Stop Acquirer Motor", new InstantCommand(m_acquisitionSubsystem::stopAcquirerMotor));
    SmartDashboard.putData("Deploy Acquirer", new InstantCommand(m_acquisitionSubsystem::deployAcquirer));
    SmartDashboard.putData("Deploy Acquirer", new InstantCommand(m_acquisitionSubsystem::retractAcquirer));
    SmartDashboard.putData("Reverse Acquirer", new InstantCommand(m_acquisitionSubsystem::reverseAcquirer));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // new Button(OI::getXboxXButton).whenPressed(getRotateControlPanelCommand());
    // new Button(OI::getXboxYButton).whenPressed(getPositionControlPanelCommand());

//    new Button(OI::getXboxLeftTriggerPressed)
//        .whenPressed(getShootOnceCommand().beforeStarting(() -> m_shooterSubsystem.setIsShooting(true)));

    new CommandToggler( // Shoot Entire Drum Toggle - Right Trigger
        getShootEntireDrumCommand().beforeStarting(() -> m_shooterSubsystem.setIsShooting(true)), 
        null 
    )
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxRightTriggerPressed)
    .setCycle(true);

    new Button(OI::getXboxRightTriggerPressed).whenHeld(getShootWhenHeld()
    .beforeStarting(() -> {
      new SequentialCommandGroup(
        new InstantCommand(() -> m_shooterSubsystem.setIsShooting(true)),
        getRotateDrumOneSectionCommand()
      ).schedule();
    }));

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
        new InstantCommand(() -> m_acquisitionSubsystem.deployAndOrStartMotor(true, true)),
        new InstantCommand(() -> m_acquisitionSubsystem.retractAndOrStopMotor(true, true))   
    ) 
    .setDefaultState(CommandState.Interruptible)
    .setToggleButton(OI::getXboxRightBumper)
    .setCycle(true);

    // new CommandToggler( // Acquirer Motor Toggle - Right Bumper
    //     new SequentialCommandGroup(
    //       new PrintCommand("Deploy Acquirer")
    //       .andThen(() -> m_acquisitionSubsystem.startAcquirerMotor())
    //       .andThen(() -> m_acquisitionSubsystem.deployAcquirer())
    //     ),
    //     new SequentialCommandGroup(
    //       new PrintCommand("Retract Acquirer")
    //       .andThen(() -> m_acquisitionSubsystem.stopAcquirerMotor())
    //       .andThen(() -> m_acquisitionSubsystem.retractAcquirer())
    //     )
    // )
    // .setDefaultState(CommandState.Interruptible)
    // .setToggleButton(OI::getXboxRightBumper)
    // .setCycle(true);

  }

  // Command Getters

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_autoChooser.getSelected();
  }

  // Utilization Commands

  public static RumbleCommand getRumbleCommand(double time) {
    return new RumbleCommand(time);
  }

  // Control Panel Commands

  public static RotateControlPanelCommand getRotateControlPanelCommand() {
    return new RotateControlPanelCommand(m_controlPanelSubsystem);
  }

  public static PositionControlPanelCommand getPositionControlPanelCommand() {
    return new PositionControlPanelCommand(m_controlPanelSubsystem);
  }

  // Storage Commands

  public static RotateDrumOneSectionCommand getRotateDrumOneSectionCommand() {
    return new RotateDrumOneSectionCommand(m_storageSubsystem, m_shooterSubsystem);
  }

  // Shooting Commands

  public static TryToShootCommand getTryToShootCommand() {
    return new TryToShootCommand(m_shooterSubsystem, m_storageSubsystem);
  }

  public static ShootOnceCommand getShootOnceCommand() {
    return new ShootOnceCommand(m_shooterSubsystem);
  }

  public static ShootEntireDrumCommand getShootEntireDrumCommand() {
    return new ShootEntireDrumCommand(m_shooterSubsystem);
  }

  public static ShootWhenHeldCommand getShootWhenHeld() {
    return new ShootWhenHeldCommand(m_shooterSubsystem);
  }

  public static BopperCommand getBopperCommand() {
    return new BopperCommand(m_shooterSubsystem);
  }

  // Diagnostic Commands

  public static ResetEverythingCommand getResetEverythingCommand() {
    return new ResetEverythingCommand(m_storageSubsystem, m_shooterSubsystem, m_garminLidarSubsystem,
        m_drivebaseSubsystem, m_acquisitionSubsystem, m_climberSubsystem, m_controlPanelSubsystem);
  }

  public static RepopulateArrayCommand getRepopulateArrayCommand() {
    return new RepopulateArrayCommand(m_storageSubsystem);
  }

  public static ShakeDialCommand getShakeDial() {
    return new ShakeDialCommand(m_storageSubsystem);
  }

  // Autonomous Commands

  public static CenterAutoCommand getCenterAutoCommand() {
    return new CenterAutoCommand(m_storageSubsystem, m_shooterSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem);
  }

  public static LeftAutoCommand getLeftAutoCommand() {
    return new LeftAutoCommand(m_storageSubsystem, m_shooterSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem);
  }

  public static RightAutoCommand getRightAutoCommand() {
    return new RightAutoCommand(m_storageSubsystem, m_shooterSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem);
  }

  public static MinAutoCommand getMinAutoCommand() {
    return new MinAutoCommand(m_storageSubsystem, m_shooterSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem);
  }

  public static BasicAutoCommand getBasicAutoCommand() {
    return new BasicAutoCommand(m_storageSubsystem, m_shooterSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem);
  }

  // Trajectory Commands

  // public static TrajectoryCommand getLeftTrajectoryCommand() {
  //   return m_leftTrajectoryCommand;
  // }

  // public static TrajectoryCommand getRightTrajectoryCommand() {
  //   return m_rightTrajectoryCommand;
  // }

  // public static TrajectoryCommand getCenterTrajectoryCommand() {
  //   return m_centerTrajectoryCommand;
  // }

  // public static TrajectoryCommand getBasicTrajectoryCommand() {
  //   return m_basicTrajectoryCommand;
  // }

  

  // Subsystem Getters

  public static DrivebaseSubsystem getDrivebaseSubsystem() {
    return m_drivebaseSubsystem;
  }

  public static ControlPanelSubsystem getControlPanel() {
    return m_controlPanelSubsystem;
  }

  public static AcquisitionSubsystem getAcquisitionSubsystem() {
    return m_acquisitionSubsystem;
  }

  public static StorageSubsystem getStorageSubsystem() {
    return m_storageSubsystem;
  }

  public static ShooterSubsystem getShooterSubsystem() {
    return m_shooterSubsystem;
  }

  public static GarminLidarSubsystem getGarminLidarSubsystem() {
    return m_garminLidarSubsystem;
  }

  public static NavigationSubsystem getNavigationSubsystem() {
    return m_navigationSubsystem;
  }


  // Other methods

  public static void scheduleInstantResetOdometryCommand() {
    new InstantCommand(m_drivebaseSubsystem::resetOdometry, m_drivebaseSubsystem).schedule();
  }
}
