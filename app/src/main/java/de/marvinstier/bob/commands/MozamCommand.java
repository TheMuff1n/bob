package de.marvinstier.bob.commands;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import de.marvinstier.bob.requests.GiphyRequest;
import de.marvinstier.bob.requests.GiphyRequest.GiphyImage;

/**
 * This class provides a command that sends a random apex meme fetched from
 * giphy.com into the channel the command was executed in.
 *
 * @author Marvin Stier
 * @version 1.0
 */
public class MozamCommand extends Command {
    public MozamCommand(boolean isAdminCommand) {
        super(isAdminCommand);
    }

    @Override
    public void execute(MessageCreateEvent event) {
        GiphyImage apexImageData;
        try {
            apexImageData = GiphyRequest.getApexImage(new Random().nextInt(100));
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle(apexImageData.getTitle()).setUrl(apexImageData.getUrl())
                .setDescription("Powered by Giphy").setImage(apexImageData.getMedia())
                .setThumbnail(ClassLoader.getSystemResourceAsStream("giphy_logo.png"));
        event.getChannel().sendMessage(embed);
    }

}
