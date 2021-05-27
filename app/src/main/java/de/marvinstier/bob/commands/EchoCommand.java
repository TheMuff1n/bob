package de.marvinstier.bob.commands;

import org.javacord.api.event.message.MessageCreateEvent;

/**
 * This class provides an echo command which replies to the sender with the exact message content.
 * The content is surrounded in backticks so it is sent as cleartext.
 * This class should mostly be used for debugging purposes.
 * @author Marvin Stier
 * @version 1.0
 */
public class EchoCommand extends Command {
    @Override
    public void execute(MessageCreateEvent event) {
        event.getChannel().sendMessage("\u0060" + event.getMessageContent() + "\u0060");
    }
}
