package de.marvinstier.bob.commands;

import java.util.HashMap;
import java.util.Map;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.event.ListenerManager;

/**
 * This class provides a mock command which can be activated in a channel to
 * reply every user's message in alternating lowercase and uppercase letters.
 * This command should be used with care, as it can currently be activated by
 * everyone.
 *
 * @author Marvin Stier
 * @version 1.0
 */
public class MockCommand extends Command implements MessageCreateListener {
    Map<Long, ListenerManager<MessageCreateListener>> managers = new HashMap<>();

    @Override
    public void execute(MessageCreateEvent event) {
        ListenerManager<MessageCreateListener> manager = managers.remove(event.getChannel().getId());
        if (manager == null)
            managers.put(event.getChannel().getId(), event.getChannel().addMessageCreateListener(this));
        else
            manager.remove();
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageAuthor().isBotUser())
            return;
        char[] msg = event.getMessageContent().toLowerCase().toCharArray();
        for (int i = 0; i < msg.length; i++)
            msg[i] = msg[i] > 122 || msg[i] < 97 || i % 2 == 0 ? msg[i] : (char) (msg[i] - 32);
        event.getChannel().sendMessage(new String(msg));
    }
}
