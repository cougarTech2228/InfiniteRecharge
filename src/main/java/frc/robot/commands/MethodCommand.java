package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * The MethodCommand class is meant to be used to easily define commands.
 * It can be used to run any methods it has access to, or define its own to use.
 */
public class MethodCommand extends CommandBase {

    private Runnable method;
    private Runnable endMethod;
    private boolean isFinished;
    private BooleanSupplier endCondition;
    /**
     * This constructor should be used when the user wants to run a single method once.
     * @param method = the method to be run
     */
    public MethodCommand(Runnable method) {
        this.method = method;
        this.isFinished = false;
        this.endCondition = () -> false;
    }
    public MethodCommand() {
        this.method = null;
        this.isFinished = false;
        this.endCondition = () -> false;
    }
    public MethodCommand loop() {
        endCondition = () -> true;
        return this;
    }
    public MethodCommand loopWhile(BooleanSupplier condition) {
        endCondition = condition;
        return this;
    }
    public MethodCommand runOnEnd(Runnable method) {
        this.endMethod = method;
        return this;
    }
    public MethodCommand runOnEnd(Command command) {
        this.endMethod = () -> command.schedule();
        return this;
    }
    @Override
    public void execute() {
        if(method != null) {
            method.run();
        }
        isFinished = !endCondition.getAsBoolean();
    }
    @Override
    public boolean isFinished() {
        return isFinished;
    }
    @Override
    public void end(boolean interrupted) {
        if(endMethod != null) {
            endMethod.run();
        }
    }
}