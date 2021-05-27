package de.marvinstier.bob;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import de.marvinstier.bob.commands.Command;
import de.marvinstier.bob.commands.EchoCommand;
import de.marvinstier.bob.commands.PingCommand;

public class App {
    private static final String COMMAND_PREFIX = "!";
    private static final Map<String, Command> COMMANDS = new HashMap<String, Command>();

    public static void main(String[] args) throws IOException, URISyntaxException {
        String token = getToken();

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        registerCommands();

        api.addMessageCreateListener(App::handleCommands);
    }

    private static String getToken() throws IOException, URISyntaxException {
        return Files.readAllLines(Paths.get(ClassLoader.getSystemClassLoader().getResource("api_token.txt").toURI()))
                .get(0);
    }

    private static void registerCommands() {
        COMMANDS.put("ping", new PingCommand());
        COMMANDS.put("echo", new EchoCommand());
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
}
