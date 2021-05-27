package de.marvinstier.bob.reactions;

import org.javacord.api.event.message.reaction.ReactionAddEvent;

public abstract class ReactionCommand {
    public abstract void execute(ReactionAddEvent event);
}
