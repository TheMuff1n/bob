package de.marvinstier.bob.commands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommand;

import de.marvinstier.bob.App;

/**
 * This class provides a ping command which just replies Pong! to check if the
 * bot is running. It uses the new slash command feature
 *
 * @author Marvin Stier
 * @version 1.0
 */
public class PingSlashCommand extends SlashCommandExecutor {
    public PingSlashCommand() {
        command = SlashCommand.with("ping", "Checks the functionality of this command").createGlobal(App.getApi())
                .join();
    }

    @Override
    public void execute(SlashCommandCreateEvent event) {
        event.getSlashCommandInteraction().createImmediateResponder().setContent("Pong!").respond();
    }

}
