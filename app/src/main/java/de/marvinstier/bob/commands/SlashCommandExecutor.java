package de.marvinstier.bob.commands;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommand;

import de.marvinstier.bob.App;

/**
 * This is an abstract base class for every global slash command. It provides
 * methods that are useful for creating a slash command.
 *
 * @author Marvin Stier
 * @version 1.0
 */
public abstract class SlashCommandExecutor {
    protected String name;
    protected SlashCommand command;

    private static Map<String, SlashCommand> commands;

    /**
     * Method to retrieve the list of registered slash commands in a singleton
     * pattern.
     *
     * @return map of registered commands associated with their names
     */
    protected static Map<String, SlashCommand> getRegisteredSlashCommands() {
        if (commands == null)
            commands = App.getApi().getGlobalSlashCommands().join().stream()
                    .collect(Collectors.toMap(SlashCommand::getName, Function.identity()));

        return commands;
    }

    /**
     * When a command with given name can be found, it is retrieved from the
     * registered slash commands. Otherwise it is created by calling the
     * {@link #create()} method.
     *
     * @param name the command's name
     */
    public SlashCommandExecutor(String name) {
        this.name = name;

        command = getRegisteredSlashCommands().get(name);

        if (command == null)
            command = create();
    }

    /**
     * This command should return a newly registered slash command for the first
     * time creating an instance of this class.
     *
     * @return the registered slash command
     */
    protected abstract SlashCommand create();

    /**
     * This method is called when a user executed the slash command.
     *
     * @param event the event containing information about the command call
     */
    public abstract void execute(SlashCommandCreateEvent event);

    /**
     * @return the slash command
     */
    public SlashCommand getCommand() {
        return command;
    }

    /**
     * @return the slash command's id
     */
    public long getId() {
        return command.getId();
    }
}
