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
    private BooleanSupplier endCondition;
    private Runnable endMethod;
    private boolean isFinished;

    /**
     * This constructor should be used when the user wants to run a single method once.
     * @param method = the method to be run
     */
    public MethodCommand(Runnable method) {
        this.method = method;
        this.isFinished = false;
        this.endCondition = () -> false;
    }
    /**
     * This constructor should be used when the user wants to run a single method every loop iteration with no end condition.
     * @param method = the method to be run
     * @param loop = whether to run the method every loop iteration or just once
     */
    public MethodCommand(Runnable method, boolean loop) {
        this.method = method;
        this.isFinished = false;
        this.endCondition = () -> !loop;
    }
    /**
     * This constructor should be used when you need to define when the command should stop.
     * If the method passed in returns true, it will stop
     * @param method = the method to be run
     */
    public MethodCommand(BooleanSupplier method) {
        this.endCondition = method;
        this.isFinished = false;
    }
    /**
     * This makes the command run a given method when it ends
     * @param method = the method to be run when the command ends
     */
    public MethodCommand runOnEnd(Runnable method) {
        this.endMethod = method;
        return this;
    }
    @Override
    public void execute() {
        if(method != null) {
            method.run();
        }
        isFinished = endCondition.getAsBoolean();
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