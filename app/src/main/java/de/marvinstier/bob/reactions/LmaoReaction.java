package de.marvinstier.bob.reactions;

import org.javacord.api.event.message.reaction.ReactionAddEvent;

public class LmaoReaction extends ReactionCommand {
    @Override
    public void execute(ReactionAddEvent event) {
        event.getChannel().sendMessage(event.getEmoji().toString());
    }
}
