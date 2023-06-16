package pl.damian.bodzioch.events.handlers.chatinput;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Attachment;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.commands.SiatkaAddCommand;
import pl.damian.bodzioch.dao.AttachmentDAO;
import pl.damian.bodzioch.dao.SiatkaDAO;
import pl.damian.bodzioch.events.handlers.EventHandler;
import pl.damian.bodzioch.fileService.FileService;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class SiatkaAddCommandHandler implements EventHandler<ChatInputInteractionEvent> {

    @Autowired
    Logger logger;
    @Autowired
    AttachmentDAO attachmentDAO;
    @Autowired
    SiatkaDAO siatkaDAO;
    @Autowired
    FileService fileService;

    @Override
    public Mono<Void> handleEvent(ChatInputInteractionEvent event, String param) {
        logger.info("Handling " + SiatkaAddCommand.SIATKA_ADD_COMMAND + " command");
        Attachment attachment = getAttachment(event);
        String attachmentName = getAttachmentName(event);

        if (siatkaDAO.getSiatkaByHeroName(attachmentName).isPresent()){
            return event.reply("Siatka o nazwie " + attachmentName + "znajduje się już w bazie");
        }

        try {
            validateAttachment(attachment);
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage());
            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .content(e.getMessage())
                    .build());
        }

        String url = attachment.getUrl();
        try {
            attachmentDAO.saveSiatkaAttachment(url, attachmentName);
        } catch (IOException e) {
            logger.error("Error during download Siatka photo", e);
            event.reply("Nie udało się pobrać zdjęcia. Spróbuj ponownie.");
        }

        fileService.updateAllLists();

        return event.reply("Pomyślnie dodano kartę o nazwie " + attachmentName);
    }

    private Attachment getAttachment(ChatInputInteractionEvent event) {
        return event.getOption(SiatkaAddCommand.SIATKA_ADD_COMMAND_ATTACHMENT_OPTION)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asAttachment)
                .orElseThrow(() -> new IllegalStateException("Siatka Photo should be exists"));
    }

    private String getAttachmentName(ChatInputInteractionEvent event) {
        return event.getOption(SiatkaAddCommand.SIATKA_ADD_COMMAND_NAME_OPTION)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElseThrow(() -> new IllegalStateException("Attachment name should be exists"));
    }

    private void validateAttachment(Attachment attachment) {
        attachment.getContentType().ifPresent(this::validateContentType);
        if (attachment.getSize() > 3000000)
            throw new IllegalArgumentException("Rozmiar pliku nie może przekraczać 3Mb");
    }

    private void validateContentType(String contentType) {
        if (!contentType.contains("image"))
            throw new IllegalArgumentException("Akceptowalne pliki to tylko zdjęcia");
    }
}
