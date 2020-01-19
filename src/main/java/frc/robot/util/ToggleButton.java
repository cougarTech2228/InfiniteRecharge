package frc.robot.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Button;

/**
 * A class dedicated to make it easy to make buttons toggle between or through multiple commands
 */
public class ToggleButton {

    private int nextIndex;
    private boolean cycle;
    private List<Command> commands;
    private List<CommandState> commandStates;
    private Button upButton, downButton;
    private int currentIndex;
    private CommandState defaultCmdState = CommandState.Normal;

    /**
     * CommandStates define how the commands should be run in the ToggleButton's command list
     */
    public enum CommandState {
        /**
         * Normal mode means that this command can run simultaneously with the next command
         */
        Normal,
        /**
         * Interruptible mode means that when the next command is run, this one stops
         */
        Interruptible,
        /**
         * Precendented mode means that this command will take precedence over the next command,
         * it will have to finish before the next command can be run
         */
        Precedented
    }
    /**
     * Creates a new default ToggleButton instance
     */
    public ToggleButton() {
        this.commands = new ArrayList<Command>();
        this.commandStates = new ArrayList<CommandState>();
        nextIndex = 0;
        currentIndex = -1;
        cycle = false;
    }
    /**
     * Creates a new ToggleButton that toggles between the commands passed in
     * @param commands
     */
    public ToggleButton(Command... commands) {
        this.commands = Arrays.asList(commands);
        this.commandStates = new ArrayList<CommandState>();
        setDefaultState(CommandState.Normal);
        nextIndex = 0;
        currentIndex = -1;
        cycle = false;
    }
    /**
     * Adds the specified command onto the toggling list with the specified CommandState
     * @param command
     * @param state
     */
    public ToggleButton addCommand(Command command, CommandState state) {
        commands.add(command);
        commandStates.add(state);
        return this;
    }
    /**
     * Adds the specified command onto the toggling list with the default CommandState
     * @param command
     */
    public ToggleButton addCommand(Command command) {
        return addCommand(command, defaultCmdState);
    }
    /**
     * Sets the default CommandState, *This must be run before any calls to addCommand()
     * @param state
     * @return
     */
    public ToggleButton setDefaultState(CommandState state) {
        commandStates = new ArrayList<CommandState>();
        for(Command c : commands) {
            commandStates.add(state);
        }
        return this;
    }
    /**
     * Sets whether to cycle through the command list, if true, once the last command has been started,
     * the next time the set ToggleButton is pressed, it will loop to the beginning of the command list and run the first command
     * (The exact opposite happens for the ToggleDownButton if one is assigned)
     * @param on
     * @return
     */
    public ToggleButton setCycle(boolean on) {
        cycle = on;
        return this;
    }
    /**
     * Sets a command to be run when the ToggleButton is initialized
     * @param command
     */
    public ToggleButton setStartCommand(Command command) {
        command.schedule();
        return this;
    }
    /**
     * Assigns a button to control toggling upward through the command list
     * @param pressed = a reference to the method testing the button press
     * Ex: .setToggleButton(OI::getXboxLeftBumper)
     */
    public ToggleButton setToggleButton(BooleanSupplier pressed) {
        return setToggleButton(new Button(pressed) {});
    }
    /**
     * Assigns a button to control toggling upward through the command list
     * @param button = a Button object
     */
    public ToggleButton setToggleButton(Button button) {
        if(upButton == null) {
            upButton = button;
            upButton.whenPressed(() -> {
                if(currentIndex != -1) {
                    CommandState state = commandStates.get(currentIndex);
                    if(state == CommandState.Precedented && !commands.get(currentIndex).isFinished()) {
                        return;
                    }
                    if(state == CommandState.Interruptible && commands.get(currentIndex) != null) {
                        commands.get(currentIndex).cancel();
                    }
                }
                if(commands.get(nextIndex) != null) {
                    CommandScheduler.getInstance().schedule(commands.get(nextIndex));
                }
                currentIndex = nextIndex;

                nextIndex++;
                if(nextIndex > commands.size() - 1) nextIndex = cycle ? 0 : currentIndex;
            });
        }
        return this;
    }
    /**
     * Assigns a button to control toggling upward through the command list
     * @param pressed = a reference to the method testing the button press
     * Ex: .setToggleDownButton(OI::getXboxRightBumper)
     */
    public ToggleButton setToggleDownButton(BooleanSupplier pressed) {
        return setToggleDownButton(new Button(pressed) {});
    }
    /**
     * Assigns a button to control toggling downward through the command list
     * @param button = a Button object
     */
    public ToggleButton setToggleDownButton(Button button) {
        if(downButton == null) {
            downButton = button;
            downButton.whenPressed(() -> {
                if(currentIndex != -1) {
                    CommandState state = commandStates.get(currentIndex);
                    if(state == CommandState.Precedented && !commands.get(currentIndex).isFinished()) {
                        return;
                    }
                    if(state == CommandState.Interruptible) {
                        commands.get(currentIndex).cancel();
                    }
                }
                if(commands.get(nextIndex) != null) {
                    CommandScheduler.getInstance().schedule(commands.get(nextIndex));
                }
                currentIndex = nextIndex;

                nextIndex--;
                if(nextIndex < 0) nextIndex = cycle ? commands.size() - 1 : currentIndex;
            });
        }
        return this;
    }
    /**
     * Returns the command that is currently being run
     */
    public Command getActiveCommand() {
        return commands.get(currentIndex);
    }
    /**
     * returns the index of the command that is currently being run
     */
    public int getCommandIndex() {
        return currentIndex;
    }
}