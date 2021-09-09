package de.marvinstier.bob.commands;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import de.marvinstier.bob.App;

/**
 * This is an abstract base class for every global slash command. It provides
 * methods that are useful for creating a slash command.
 *
 * @author Marvin Stier
 * @version 1.1
 */
public abstract class SlashCommandExecutor {
    protected String name;
    protected Server server;
    protected SlashCommand command;

    private static Map<String, SlashCommand> globalCommands;

    /**
     * Method to retrieve the list of registered slash commands in a singleton
     * pattern.
     *
     * @return map of registered commands associated with their names
     */
    protected static Map<String, SlashCommand> getRegisteredSlashCommands() {
        if (globalCommands == null)
            globalCommands = App.getApi().getGlobalSlashCommands().join().stream()
                    .collect(Collectors.toMap(SlashCommand::getName, Function.identity()));

        return globalCommands;
    }

    /**
     * Initialize global slash command
     *
     * @param name the command's name
     */
    public SlashCommandExecutor(String name) {
        this.name = name;

        command = getRegisteredSlashCommands().get(name);

        if (command == null)
            command = registerGlobal();
    }

    /**
     * Initialize server slash command
     *
     * @param name   the command's name
     * @param server the server to register command for
     */
    public SlashCommandExecutor(String name, Server server) {
        this.name = name;
        this.server = server;

        command = getServerCommand();

        if (command == null)
            command = registerServer();
    }

    /**
     * This command should return a newly registered global slash command for the
     * first time creating an instance of this class.
     *
     * @return the registered global slash command
     */
    protected abstract SlashCommand registerGlobal();

    /**
     * This command should return a newly registered server slash command for the
     * first time creating an instance of this class for a server.
     *
     * @return the registered server slash command
     */
    protected abstract SlashCommand registerServer();

    /**
     * This method is called when a user executed the slash command.
     *
     * @param event the event containing information about the command call
     */
    public abstract void execute(SlashCommandCreateEvent event);

    /**
     * Fetches registered commands for the server and searches for that with given
     * name
     *
     * @return the command or null if not found
     */
    protected SlashCommand getServerCommand() {
        Map<String, SlashCommand> commands;
        List<SlashCommand> list;
        list = App.getApi().getServerSlashCommands(server).join();
        commands = list.stream().collect(Collectors.toMap(SlashCommand::getName, Function.identity()));
        return commands.get(name);
    }

    /**
     * Utility method to create a quick error response
     *
     * @param interaction the slash command interaction reference
     */
    public static void immediateUnexpectedError(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder().setContent("An unexpected error occured").respond();
    }

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
