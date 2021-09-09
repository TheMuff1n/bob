package de.marvinstier.bob.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import de.marvinstier.bob.App;

/**
 * This class provides a delete command which deletes a given number of
 * messages. Only administrators can execute this command
 *
 * @author Marvin Stier
 * @version 1.0
 */
public class DeleteSlashCommand extends SlashCommandExecutor {
    public DeleteSlashCommand() {
        super("delete");
    }

    public DeleteSlashCommand(Server server) {
        super("delete", server);
    }

    @Override
    protected SlashCommand registerGlobal() {
        return SlashCommand.with(name, "Deletes messages in the current channel")
                .addOption(SlashCommandOption.create(SlashCommandOptionType.INTEGER, "number",
                        "Number of messages to delete, 10 if unspecified", false))
                .createGlobal(App.getApi()).join();
    }

    @Override
    protected SlashCommand registerServer() {
        return SlashCommand.with(name, "Deletes messages in the current channel (server command)")
                .addOption(SlashCommandOption.create(SlashCommandOptionType.INTEGER, "number",
                        "Number of messages to delete, 10 if unspecified", false))
                .createForServer(server).join();
    }

    @Override
    public void execute(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();

        // TODO: advanced permissions
        if (!interaction.getServer().get().isAdmin(interaction.getUser())) {
            interaction.createImmediateResponder().setContent("Ur not an admin, doofus...")
                    .setFlags(MessageFlag.EPHEMERAL).respond();
            return;
        }

        int count = interaction.getFirstOptionIntValue().orElse(10);
        interaction.getChannel().ifPresentOrElse(channel -> {
            deleteMessages(channel, count);
            interaction.createImmediateResponder()
                    .setContent(String.format("Deleting (up to) %d messages, this might take a while!", count))
                    .setFlags(MessageFlag.EPHEMERAL).respond();
        }, () -> {
            SlashCommandExecutor.immediateUnexpectedError(interaction);
        });
    }

    /**
     * Tries to delete a given number of messages.
     *
     * @param channel the channel in which to delete messages
     * @param count   the number of messages to delete
     */
    private void deleteMessages(TextChannel channel, int count) {
        // TODO: use different delete method?
        channel.getMessagesAsStream().limit(count).forEach(message -> {
            if (message.canYouDelete())
                message.delete();
        });
    }
}
