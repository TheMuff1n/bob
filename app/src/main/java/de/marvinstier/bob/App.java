package de.marvinstier.bob;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.reaction.ReactionAddEvent;

import com.vdurmont.emoji.EmojiParser;

import de.marvinstier.bob.commands.Command;
import de.marvinstier.bob.commands.EchoCommand;
import de.marvinstier.bob.commands.PingCommand;
import de.marvinstier.bob.reactions.AcceptRulesReaction;
import de.marvinstier.bob.reactions.LmaoReaction;
import de.marvinstier.bob.reactions.ReactionCommand;

public class App {
    private static final String COMMAND_PREFIX = "!";
    private static final Map<String, Command> COMMANDS = new HashMap<>();
    private static final Map<String, ReactionCommand> REACTIONS = new HashMap<>();

    public static void main(String[] args) throws IOException, URISyntaxException {
        String token = getToken();

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        registerCommands();
        registerReactions();

        api.addMessageCreateListener(App::handleCommands);
        api.addReactionAddListener(App::handleReactions);
    }

    private static String getToken() throws IOException, URISyntaxException {
        byte[] data;
        try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("api_token.txt")) {
            data = in.readAllBytes();
        }
        return new String(data);
    }

    private static void registerCommands() {
        COMMANDS.put("ping", new PingCommand());
        COMMANDS.put("echo", new EchoCommand());
    }

    private static void registerReactions() {
        REACTIONS.put(EmojiParser.parseToUnicode(":rofl:"), new LmaoReaction());
        REACTIONS.put(EmojiParser.parseToUnicode(":white_check_mark:"), new AcceptRulesReaction());
    }

    private static void handleCommands(MessageCreateEvent event) {
        if (event.getMessage().getAuthor().isBotUser())
            return;

        if (!event.getMessageContent().startsWith(COMMAND_PREFIX))
            return;

        String[] args = event.getMessageContent().substring(COMMAND_PREFIX.length()).split(" ");

        if (args.length < 1)
            return;

        if (!COMMANDS.containsKey(args[0].toLowerCase()))
            return;

        COMMANDS.get(args[0].toLowerCase()).execute(event);
    }

    private static void handleReactions(ReactionAddEvent event) {
        String unicodeEmoji = event.getEmoji().getMentionTag();

        if (!REACTIONS.containsKey(unicodeEmoji))
            return;

        REACTIONS.get(unicodeEmoji).execute(event);
    }
}
