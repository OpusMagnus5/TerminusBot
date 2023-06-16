package pl.damian.bodzioch.events.handlers.button;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.events.handlers.EventHandler;
import pl.damian.bodzioch.fileService.FileServiceImpl;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class SiatkaButtonHandler implements EventHandler<ButtonInteractionEvent> {

    @Autowired
    Logger logger;

    @Override
    public Mono<Void> handleEvent(ButtonInteractionEvent event, String param) {
        InteractionApplicationCommandCallbackSpec response;
        try {
            logger.info("Handling SiatkaButton event");
            String fileDir = FileServiceImpl.RESOURCE_DIR + FileServiceImpl.SIATKA_DIR + param + FileServiceImpl.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            logger.warn("File not found", e);
            return event.reply(InteractionApplicationCommandCallbackSpec.builder().content("Coś poszło nie tak.").build());
        }
        return event.reply(response);
    }
}
