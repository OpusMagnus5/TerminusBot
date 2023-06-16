package pl.damian.bodzioch.dao.Impl;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import pl.damian.bodzioch.dao.AttachmentDAO;
import pl.damian.bodzioch.fileService.FileServiceImpl;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class AttachmentDAOImpl implements AttachmentDAO {

    @Autowired
    Logger logger;

    public void saveBlackListAttachment(ChatInputInteractionEvent event, String url, String playerName) {
        WebClient webClient = WebClient.create();
        Mono<byte[]> fileMono = webClient.get().uri(url).retrieve().bodyToMono(byte[].class);
        byte[] fileData = fileMono.block();
        Path filePath = Paths.get(FileServiceImpl.RESOURCE_DIR + FileServiceImpl.BLACKLIST_DIR, playerName + FileServiceImpl.JPG_FILE_EXTENSION);
        try {
            Files.write(filePath, fileData);
            logger.info("BlackList photo downloaded successfully");
        } catch (Exception e) {
            logger.error("Error during download BlackList photo", e);
            event.reply("Nie udało się pobrać zdjęcia. Spróbuj ponownie.");
        }
    }
}
