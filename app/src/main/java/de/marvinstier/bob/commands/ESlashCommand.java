package de.marvinstier.bob.commands;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.listener.message.MessageCreateListener;

/**
 * This is a meme command, lol.
 *
 * @author Marvin Stier
 * @version 1.0
 */
public class ESlashCommand extends SlashCommandExecutor implements MessageCreateListener {
	public ESlashCommand(Server server) {
		super("e", server);
	}

	@Override
	protected SlashCommand registerGlobal() {
		throw new UnsupportedOperationException("For \"security\" reasons this command can only be registered for a server.");
	}

	@Override
	protected SlashCommand registerServer() {
		return SlashCommand.with(name, "A present for E-Bot.exe").createForServer(server).join();
	}

	@Override
	public void execute(SlashCommandCreateEvent event) {
		event.getSlashCommandInteraction().getChannel().ifPresentOrElse(channel -> {
			channel.addMessageCreateListener(this);
			event.getSlashCommandInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL)
					.setContent("This is what it feels like when AI takes over your job.").respond();
		}, () -> event.getSlashCommandInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL)
				.setContent("The day AI takes over your job has not yet come (command failed)").respond());
	}

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		event.getMessage().addReaction("🇪").join();
	}

}
