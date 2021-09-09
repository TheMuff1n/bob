package de.marvinstier.bob.commands;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommand;

import de.marvinstier.bob.App;

/**
 * This class provides a ping command which just replies Pong! to check if the
 * bot is running. It uses the new slash command feature
 *
 * @author Marvin Stier
 * @version 1.1
 */
public class PingSlashCommand extends SlashCommandExecutor {
    public PingSlashCommand() {
        super("ping");
    }

    public PingSlashCommand(Server server) {
        super("sping", server);
    }

    @Override
    protected SlashCommand registerGlobal() {
        return SlashCommand.with(name, "Checks the functionality of this command").createGlobal(App.getApi()).join();
    }

    @Override
    protected SlashCommand registerServer() {
        return SlashCommand.with(name, "Checks the functionality of this command (server command)")
                .createForServer(server).join();
    }

    @Override
    public void execute(SlashCommandCreateEvent event) {
        event.getSlashCommandInteraction().createImmediateResponder().setContent("Pong!")
                .setFlags(MessageFlag.EPHEMERAL).respond();
    }
}
