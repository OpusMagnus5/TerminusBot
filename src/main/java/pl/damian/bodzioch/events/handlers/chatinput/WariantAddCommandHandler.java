package pl.damian.bodzioch.events.handlers.chatinput;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Attachment;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.commands.WariantAddCommand;
import pl.damian.bodzioch.dao.AttachmentDAO;
import pl.damian.bodzioch.dao.HeroWariantDAO;
import pl.damian.bodzioch.dao.database.DataInLists;
import pl.damian.bodzioch.events.handlers.EventHandler;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class WariantAddCommandHandler implements EventHandler<ChatInputInteractionEvent> {
    @Autowired
    Logger logger;
    @Autowired
    AttachmentDAO attachmentDAO;
    @Autowired
    HeroWariantDAO heroWariantDAO;

    @Override
    public Mono<Void> handleEvent(ChatInputInteractionEvent event, String param) {
        logger.info("Handling " + WariantAddCommand.WARIANT_ADD_COMMAND + " command");
        Attachment attachment = getAttachment(event);
        String attachmentName = getAttachmentName(event);
        logger.info("Attachment name: " + attachmentName);

        if (heroWariantDAO.getHeroWariantByName(attachmentName).isPresent()){
            return event.reply("Wariant o nazwie " + attachmentName + "znajduje się już w bazie");
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
        logger.info("URL: " + url);
        try {
            attachmentDAO.saveWariantAttachment(url, attachmentName);
        } catch (IOException e) {
            logger.error("Error during download Wariant photo", e);
            event.reply("Nie udało się pobrać zdjęcia. Spróbuj ponownie.");
        }

        DataInLists.HERO_WARIANT_LIST.add(attachmentName);

        return event.reply("Pomyślnie dodano kartę wariantu o nazwie " + attachmentName);
    }

    private Attachment getAttachment(ChatInputInteractionEvent event) {
        return event.getOption(WariantAddCommand.WARIANT_ADD_COMMAND_ATTACHMENT_OPTION)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asAttachment)
                .orElseThrow(() -> new IllegalStateException("Wariant Photo should be exists"));
    }

    private String getAttachmentName(ChatInputInteractionEvent event) {
        return event.getOption(WariantAddCommand.WARIANT_ADD_COMMAND_NAME_OPTION)
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
