package de.marvinstier.bob;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.interaction.SlashCommandInteraction;

import com.vdurmont.emoji.EmojiParser;

import de.marvinstier.bob.commands.Command;
import de.marvinstier.bob.commands.EchoCommand;
import de.marvinstier.bob.commands.MockCommand;
import de.marvinstier.bob.commands.MozamCommand;
import de.marvinstier.bob.commands.PingCommand;
import de.marvinstier.bob.commands.PingSlashCommand;
import de.marvinstier.bob.commands.SlashCommandExecutor;
import de.marvinstier.bob.reactions.AcceptRulesReaction;
import de.marvinstier.bob.reactions.LmaoReaction;
import de.marvinstier.bob.reactions.ReactionCommand;

public class App {
    private static final String COMMAND_PREFIX = "!";
    private static final Map<String, Command> COMMANDS = new HashMap<>();
    private static final Map<String, ReactionCommand> REACTIONS = new HashMap<>();
    private static final Map<Long, SlashCommandExecutor> SLASH_COMMANDS = new HashMap<>();

    private static DiscordApi api;

    public static DiscordApi getApi() {
        if (api == null) {
            try {
                api = new DiscordApiBuilder().setToken(getToken()).login().join();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return api;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        registerCommands();
        registerReactions();
        registerSlashCommands();

        getApi().addMessageCreateListener(App::handleCommands);
        getApi().addReactionAddListener(App::handleReactions);
        getApi().addSlashCommandCreateListener(App::handleSlashCommands);
    }

    private static String getToken() throws IOException, URISyntaxException {
        byte[] data;
        try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("api_token.txt")) {
            data = in.readAllBytes();
        }
        return new String(data);
    }

    private static void registerCommands() {
        COMMANDS.put("ping", new PingCommand(false));
        COMMANDS.put("echo", new EchoCommand(true));
        COMMANDS.put("mock", new MockCommand(true));
        COMMANDS.put("apex", new MozamCommand(false));
    }

    private static void registerReactions() {
        REACTIONS.put(EmojiParser.parseToUnicode(":rofl:"), new LmaoReaction());
        REACTIONS.put(EmojiParser.parseToUnicode(":white_check_mark:"), new AcceptRulesReaction());
    }

    private static void registerSlashCommands() {
        registerSlashCommand(new PingSlashCommand());
    }

    private static void registerSlashCommand(SlashCommandExecutor command) {
        SLASH_COMMANDS.put(command.getId(), command);
    }

    private static void handleCommands(MessageCreateEvent event) {
        if (event.getMessage().getAuthor().isBotUser())
            return;

        if (!event.getMessageContent().startsWith(COMMAND_PREFIX))
            return;

        String[] args = event.getMessageContent().substring(COMMAND_PREFIX.length()).split(" ");

        if (args.length < 1)
            return;

        Command cmd = COMMANDS.get(args[0].toLowerCase());
        if (cmd == null)
            return;

        if (event.isServerMessage() && cmd.isAdminCommand() && !event.getMessageAuthor().isServerAdmin())
            return;

        COMMANDS.get(args[0].toLowerCase()).execute(event);
    }

    private static void handleReactions(ReactionAddEvent event) {
        String unicodeEmoji = event.getEmoji().getMentionTag();

        if (!REACTIONS.containsKey(unicodeEmoji))
            return;

        REACTIONS.get(unicodeEmoji).execute(event);
    }

    private static void handleSlashCommands(SlashCommandCreateEvent event) {
        SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();

        if (!SLASH_COMMANDS.containsKey(slashCommandInteraction.getId()))
            return;

        SLASH_COMMANDS.get(slashCommandInteraction.getId()).execute(event);
    }
}
