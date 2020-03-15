package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class UnstoppableCommand extends CommandBase {
    private Command command;
    public UnstoppableCommand(Command command) {
        this.command = command;
    }
    @Override
    public void initialize() {
        command.initialize();
    }
    @Override
    public void execute() {
        command.execute();
    }
    @Override
    public boolean isFinished() {
        return command.isFinished();
    }
    @Override
    public void end(boolean interrupted) {
        command.end(interrupted);
    }
    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}