package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.NavigationSubsystem;
import frc.robot.Constants;

/**
 * TurnRobotCommand
 * 
 * Turns the robot x degrees based on the integer passed into the command
 */
public class TurnRobotCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private boolean m_hasRotated;
    private int m_turnDegree;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private NavigationSubsystem m_navigationSubsystem;
    private boolean m_isReversed;

    public TurnRobotCommand(DrivebaseSubsystem drivebaseSubsystem, NavigationSubsystem navigationSubsystem, int turnDegree) {
        m_turnDegree = turnDegree;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_navigationSubsystem = navigationSubsystem;
        m_isReversed = false;
        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        System.out.println("starting to turn robot");
        m_hasRotated = false;
        m_navigationSubsystem.zeroHeading();
        if(m_turnDegree < 0) {
            m_isReversed = true;
            m_drivebaseSubsystem.tankDriveVelocity(-Constants.autoTurnSpeed, Constants.autoTurnSpeed);
        } else {
            m_drivebaseSubsystem.tankDriveVelocity(Constants.autoTurnSpeed, -Constants.autoTurnSpeed);
        }
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        SmartDashboard.putNumber("gyro angle", m_navigationSubsystem.getHeading());
        if(!m_isReversed && m_navigationSubsystem.getHeading() >= m_turnDegree) {
            m_drivebaseSubsystem.tankDriveVelocity(0, 0);
            m_hasRotated = true;
        } else if(m_navigationSubsystem.getHeading() <= m_turnDegree) {
            m_drivebaseSubsystem.tankDriveVelocity(0, 0);
            m_hasRotated = true;
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_hasRotated;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        System.out.println("Stop robot");
        m_navigationSubsystem.zeroHeading();
    }
}
