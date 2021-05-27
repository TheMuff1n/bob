package de.marvinstier.bob.commands;

import org.javacord.api.event.message.MessageCreateEvent;

/**
 * This is an abstract base class for every Command.
 * It provides methods that are useful for creating a command.
 * @author Marvin Stier
 * @version 1.0
 */
public abstract class Command {
    /**
     * This method is called when a user typed the command with a set prefix.
     * @param event holds data about the message sent
     */
    public abstract void execute(MessageCreateEvent event);
}
