package pl.damian.bodzioch.events.handlers.chatinput;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
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
public class CalendarCommandHandler implements EventHandler<ChatInputInteractionEvent> {

    @Autowired
    Logger logger;

    @Override
    public Mono<Void> handleEvent(ChatInputInteractionEvent event, String param) {
        logger.info("Handling KalendarzCommand event");
        InteractionApplicationCommandCallbackSpec response;
        try {
            String fileDir = FileServiceImpl.RESOURCE_DIR + FileServiceImpl.CALENDAR_DIR + "kalendarz" + FileServiceImpl.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            logger.warn("File not found", e);
            return event.reply("Nie znaleziono kalendarza");
        }
        return event.reply(response);
    }
}
