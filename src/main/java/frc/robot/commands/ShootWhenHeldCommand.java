package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ShooterSubsystem;

public class ShootWhenHeldCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private ShooterSubsystem m_shooterSubsystem;
    private boolean m_isShooting;
    
    public ShootWhenHeldCommand(ShooterSubsystem shooterSubsystem) {

        m_shooterSubsystem = shooterSubsystem;

        // Use addRequirements() here to declare subsystem dependencies.
        //addRequirements();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_shooterSubsystem.setIsShooting(true);
        m_isShooting = false;
    }

     // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if(!m_isShooting) {
            m_isShooting = true;
            RobotContainer.getTryToShootCommand()
            .andThen(new WaitCommand(Constants.timeBetweenShots))
            .andThen(() -> m_isShooting = false).schedule();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooterSubsystem.setIsShooting(false);
    }
}
