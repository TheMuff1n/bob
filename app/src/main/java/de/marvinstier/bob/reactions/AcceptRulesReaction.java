package de.marvinstier.bob.reactions;

import org.javacord.api.event.message.reaction.ReactionAddEvent;

public class AcceptRulesReaction extends ReactionCommand {
    private final long CHANNEL_ID = 802977627941109790L;
    private final long ROLE_ID = 803014025083879474L;

    @Override
    public void execute(ReactionAddEvent event) {
        if (event.getChannel().getId() != CHANNEL_ID)
            return;

        if (!event.getServer().isPresent() || !event.getServer().get().getRoleById(ROLE_ID).isPresent()) {
            event.removeReaction();
            return;
        }

        event.requestUser().thenAccept(user -> {
            user.addRole(event.getServer().get().getRoleById(ROLE_ID).get(), "user accepted rules");
        });
    }
}
