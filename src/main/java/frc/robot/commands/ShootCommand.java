package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 * ShootCommand
 * 
 */
public class ShootCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private final int SHOOTER_STATE_CHECK_IF_OCCUPIED = 0;
    private final int SHOOTER_STATE_SHOOT_CELL = 1;

    private ShooterSubsystem m_shooterSubsystem;
    private int m_shooterState;
    private int m_amountOfTimesShot;
    private int m_timesNeededToShoot;
    private boolean m_hasFiredAll;
    private int m_shootMode;

    public ShootCommand(ShooterSubsystem shooterSubsystem, int shootMode) {
        // Use addRequirements() here to declare subsystem dependencies.
        m_shooterSubsystem = shooterSubsystem;
        m_shooterState = SHOOTER_STATE_CHECK_IF_OCCUPIED;
        m_amountOfTimesShot = 0;
        m_hasFiredAll = false;
        m_shootMode = shootMode;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_shooterSubsystem.startFlywheel();
        if(m_shootMode == Constants.SHOOT_MODE_ALL_CELLS)
        {
            m_timesNeededToShoot = 5;
        }
        else // Constants.SHOOT_MODE_SINGLE_CELL
        {
            m_timesNeededToShoot = 1;
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() { // TODO Alter wait times accordingly

        switch (m_shooterState) 
        {
        case SHOOTER_STATE_CHECK_IF_OCCUPIED:
            if (m_shooterSubsystem.isShooterSlotOccupied()) // Check if the current shooter slot has a ball in it
            {
                m_shooterState = SHOOTER_STATE_SHOOT_CELL; // If there is a ball there, move on to the next state
            } 
            else 
            {
                // CommandScheduler.getInstance() // Other wise move the drum one section
                //         .schedule(RobotContainer.getRotateDrumOneSectionCommand()
                //         .andThen(new WaitCommand(1)));
                
                if(m_shootMode == Constants.SHOOT_MODE_ALL_CELLS)
                {
                    m_amountOfTimesShot++; // Shooting the entire drum requires the drum to spin 5 times, not necessarily shoot 5 times
                }                          // So even if there is no ball there it still increments the variable even though the name suggests otherwise
            }
            break;

        case SHOOTER_STATE_SHOOT_CELL: 
            CommandScheduler.getInstance()  // Raise and lower the lifter to shoot the ball
                .schedule(new WaitCommand(0.1)
                .andThen(() -> m_shooterSubsystem.raiseLifter())
                .andThen(new WaitCommand(1))
                .andThen(() -> m_shooterSubsystem.lowerLifter())
                .andThen(new WaitCommand(1)));

            m_amountOfTimesShot++;
            m_shooterState = SHOOTER_STATE_CHECK_IF_OCCUPIED;
            break;

        default:
            System.out.println("Reached default case in ShootEntireDrumCommand, state results in: " + m_shooterState);
            break;
        }

        if(m_amountOfTimesShot == m_timesNeededToShoot) // If the necessary amount of balls/spins have been shot/completed end the command
        {
            m_hasFiredAll = true;
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_hasFiredAll;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooterSubsystem.stopFlywheel();
    }
}
