package pl.damian.bodzioch.dao.Impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import pl.damian.bodzioch.dao.AttachmentDAO;
import pl.damian.bodzioch.fileService.FileServiceImpl;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class AttachmentDAOImpl implements AttachmentDAO {

    @Autowired
    Logger logger;

    public void saveBlackListAttachment(String url, String playerName) throws IOException {
        saveAttachment(url, playerName, FileServiceImpl.BLACKLIST_DIR);
        logger.info("BlackList photo downloaded successfully");
    }

    @Override
    public void saveHeroAttachment(String url, String photoId) throws IOException {
        saveAttachment(url, photoId, FileServiceImpl.HERO_DIR);
        logger.info("Hero photo downloaded successfully");
    }

    @Override
    public void saveSiatkaAttachment(String url, String photoId) throws IOException {
        saveAttachment(url, photoId, FileServiceImpl.SIATKA_DIR);
        logger.info("Siatka photo downloaded successfully");
    }

    @Override
    public void saveWariantAttachment(String url, String photoId) throws IOException {
        saveAttachment(url, photoId, FileServiceImpl.WARIANT_DIR);
        logger.info("Wariant photo downloaded successfully");
    }

    @Override
    public void saveSiatkaWariantAttachment(String url, String photoId) throws IOException {
        saveAttachment(url, photoId, FileServiceImpl.SIATKA_WARIANTU_DIR);
        logger.info("Siatka Wariantu photo downloaded successfully");
    }

    private void saveAttachment(String url, String playerName, String directory) throws IOException {
        WebClient webClient = WebClient.create();
        Mono<byte[]> fileMono = webClient.get().uri(url).retrieve().bodyToMono(byte[].class);
        byte[] fileData = fileMono.block();
        Path filePath = Paths.get(FileServiceImpl.RESOURCE_DIR + directory, playerName + FileServiceImpl.JPG_FILE_EXTENSION);
        Files.write(filePath, fileData);
    }


}
