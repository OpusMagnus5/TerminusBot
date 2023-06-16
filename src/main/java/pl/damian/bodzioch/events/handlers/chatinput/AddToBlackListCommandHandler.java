package pl.damian.bodzioch.events.handlers.chatinput;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.component.TextInput;
import discord4j.core.object.entity.Attachment;
import discord4j.core.spec.InteractionPresentModalSpec;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.commands.AddToBlackListCommand;
import pl.damian.bodzioch.dao.AttachmentDAO;
import pl.damian.bodzioch.dao.BlackListDAO;
import pl.damian.bodzioch.events.handlers.EventHandler;
import pl.damian.bodzioch.service.CommandsService;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
public class AddToBlackListCommandHandler implements EventHandler<ChatInputInteractionEvent> {

    public static final String BLACKLIST_FORM_MODAL = "blacklistForm";

    @Autowired
    Logger logger;
    @Autowired
    AttachmentDAO attachmentDAO;
    @Autowired
    BlackListDAO blackListDAO;

    @Override
    public Mono<Void> handleEvent(ChatInputInteractionEvent event, String param) {
        logger.info("Handling " + AddToBlackListCommand.ADD_PLAYER_TO_BLACKLIST_COMMAND + " command");
        Attachment attachment = getAttachment(event);
        String playerName = getPlayerName(event);
        logger.info("Player: " + playerName);
        if (!blackListDAO.getPlayersByName(playerName).isEmpty()){
            return event.reply("Gracz o nazwie " + playerName + " znajduje się już na czarnej liście");
        }

        try {
            validateAttachment(attachment);
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage());
            return event.reply(e.getMessage());
        }

        String url = attachment.getUrl();
        logger.info("URL: " + url);
        try {
            attachmentDAO.saveBlackListAttachment(url, playerName);
        } catch (IOException e) {
            logger.error("Error during download BlackList photo", e);
            event.reply("Nie udało się pobrać zdjęcia. Spróbuj ponownie.");
        }

        List<LayoutComponent> formFields = buildForm();
        return event.presentModal(InteractionPresentModalSpec.builder()
                .title("Dodawanie gracza do czarnej listy")
                .customId(BLACKLIST_FORM_MODAL)
                .addAllComponents(formFields)
                .build());
    }

    private Attachment getAttachment(ChatInputInteractionEvent event) {
        return event.getOption(AddToBlackListCommand.DEFENSE_PHOTO_OPTION_ADD_PLAYER_TO_BLACKLIST_COMMAND)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asAttachment)
                .orElseThrow(() -> new IllegalStateException("Defend Photo should be exists"));
    }

    private String getPlayerName(ChatInputInteractionEvent event) {
        return event.getOption(AddToBlackListCommand.PLAYER_NAME_OPTION_ADD_PLAYER_TO_BLACKLIST_COMMAND)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElseThrow(() -> new IllegalStateException("Player name should be exists"));
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

    private List<LayoutComponent> buildForm() {
        ActionRow playerName = ActionRow.of(TextInput.small(CommandsService.BLACKLIST_FORM_PLAYER_INPUT, "Nazwa gracza", "Podaj nazwę gracza").required(true));
        ActionRow reason = ActionRow.of(TextInput.paragraph(CommandsService.BLACKLIST_FORM_REASON_INPUT, "Powód dodania", "Podaj powód").required(true));
        ActionRow reportingPerson = ActionRow.of(TextInput.small(CommandsService.BLACKLIST_FORM_REPORTING_PERSON_INPUT, "Nazwa osoby/sojuszu zgłaszającego gracza", "Podaj nazwę osoby/sojuszu").required(false));
        ActionRow level = ActionRow.of(TextInput.small(CommandsService.BLACKLIST_FORM_PLAYER_LEVEL_INPUT, "Poziom gracza", "Podaj poziom gracza").required(false));

        return List.of(playerName, reason, reportingPerson, level);
    }
}
