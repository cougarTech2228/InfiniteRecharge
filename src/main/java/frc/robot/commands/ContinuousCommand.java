package frc.robot.commands;

import java.util.function.Function;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * The ContinuousCommand class is meant to be used to easily define commands. It
 * can be used to run any methods it has access to, or define its own to use.
 */
public class ContinuousCommand extends CommandBase {
    private Controller controller;
    private Function<Controller, Controller> conditions;
    private boolean isFinished;
    private boolean hasStarted;
    private boolean cancelOnFinished;
    private Command runOnEnd;

    public ContinuousCommand(Function<Controller, Controller> conditions) {
        this.conditions = conditions;
        this.isFinished = false;
        this.hasStarted = false;
        this.cancelOnFinished = false;
    }
    private void startNext() {
        if(!controller.isFinished) {
            controller.activeCommand.andThen(() -> {
                if(controller.afterwardStop || isFinished) {
                    isFinished = true;
                } else {
                    controller = conditions.apply(new Controller(controller.iteration + 1));
                    startNext();
                }
            }).schedule();
        }
        else {
            isFinished = true;
        }
    }
    @Override
    public void initialize() {
        hasStarted = false;
        isFinished = false;
    }
    @Override
    public void execute() {
        if(!hasStarted) {
            controller = conditions.apply(new Controller(0));
            startNext();
            hasStarted = true;
        }
    }
    public ContinuousCommand cancelOnFinished() {
        cancelOnFinished = true;
        return this;
    }
    @Override
    public boolean isFinished() {
        return isFinished;
    }
    @Override
    public void end(boolean interrupted) {
        if(cancelOnFinished) {
            controller.activeCommand.cancel();
        }
        if(runOnEnd != null) {
            runOnEnd.schedule();
        }
    }
    public ContinuousCommand runOnEnd(Command c) {
        runOnEnd = c;
        return this;
    }
    public ContinuousCommand runOnEnd(Runnable r) {
        runOnEnd = new MethodCommand(r);
        return this;
    }
    public final class Controller {
        private int iteration;
        private Boolean when;
        private boolean wasJustTrue;
        private Command activeCommand;
        private boolean isFinished;
        private boolean afterwardStop;
        private Controller(int iteration) {
            this.iteration = iteration;
            this.when = null;
            this.isFinished = false;
            this.wasJustTrue = false;
            this.afterwardStop = false;
        }
        public int getIteration() {
            return iteration;
        }
        public Controller endAfterward() {
            if(wasJustTrue) afterwardStop = true;
            return this;
        }
        public Controller endWhen(boolean b) {
            wasJustTrue = false;
            if(b) isFinished = true;
            return this;
        }
        public Controller endOn(int iteration) {
            wasJustTrue = false;
            if(this.iteration == iteration) isFinished = true;
            return this;
        }
        public Controller endAfter(int iteration) {
            wasJustTrue = false;
            if(this.iteration == iteration + 1) isFinished = true;
            return this;
        }
        public Controller when(boolean b, Command c) {
            wasJustTrue = false;
            if(b) {
                addCommand(c);
                wasJustTrue = true;
            }
            when = b;
            return this;
        }
        public Controller when(boolean b, Runnable r) {
            return when(b, new MethodCommand(r));
        }
        public Controller otherwiseWhen(boolean b, Command c) {
            wasJustTrue = false;
            if(when == null) {
                //ahh
            }
            else if(!when && b) {
                addCommand(c);
                wasJustTrue = true;
            }
            when = when || b;
            return this;
        }
        public Controller otherwiseWhen(boolean b, Runnable r) {
            return otherwiseWhen(b, new MethodCommand(r));
        }
        public Controller otherwise(Command c) {
            wasJustTrue = false;
            if(when == null) {
                //ahh
            }
            else if(!when) {
                addCommand(c);
                wasJustTrue = true;
            }
            when = null;
            return this;
        }
        public Controller otherwise(Runnable r) {
            return otherwise(new MethodCommand(r));
        }
        public Controller run(Command c) {
            wasJustTrue = false;
            addCommand(c);
            return this;
        }
        public Controller run(Runnable r) {
            return run(new MethodCommand(r));
        }
        private void addCommand(Command c) {
            if(activeCommand == null) {
                activeCommand = c;
            } else {
                activeCommand = activeCommand.alongWith(c);
            }
        }
    }
}