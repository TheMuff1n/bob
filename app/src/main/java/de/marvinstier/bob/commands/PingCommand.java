package de.marvinstier.bob.commands;

import org.javacord.api.event.message.MessageCreateEvent;

/**
 * This class provides a ping command which just replies Pong! to check if the bot is running
 * @author Marvin Stier
 * @version 1.0
 */
public class PingCommand extends Command {
    @Override
    public void execute(MessageCreateEvent event) {
        event.getChannel().sendMessage("Pong!");
    }
}
