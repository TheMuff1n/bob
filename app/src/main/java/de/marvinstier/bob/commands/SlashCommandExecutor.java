package de.marvinstier.bob.commands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommand;

public abstract class SlashCommandExecutor {
    protected SlashCommand command;
    
    public abstract void execute(SlashCommandCreateEvent event);

    public SlashCommand getCommand() {
        return command;
    }
    
    public long getId() {
        return command.getId();
    }
}
