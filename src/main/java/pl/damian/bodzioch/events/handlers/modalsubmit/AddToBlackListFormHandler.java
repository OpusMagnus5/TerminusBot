package pl.damian.bodzioch.events.handlers.modalsubmit;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.TextInput;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.dao.BlackListDAO;
import pl.damian.bodzioch.events.handlers.EventHandler;
import pl.damian.bodzioch.service.CommandsService;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddToBlackListFormHandler implements EventHandler<ModalSubmitInteractionEvent> {

    public static final String OPTION_3 = "3";
    public static final String OPTION_4 = "4";

    @Autowired
    Logger logger;
    @Autowired
    BlackListDAO blackListDAO;

    @Override
    public Mono<Void> handleEvent(ModalSubmitInteractionEvent event, String param) {
        logger.info("Handling addPlayerToBlackListForm modal");

        try {
            Map<String, Optional<String>> inputMap = event.getComponents(TextInput.class).stream()
                    .collect(Collectors.toMap(TextInput::getCustomId, this::validateInputAndReturn));
            String data = getDataToSave(inputMap);
            blackListDAO.savePlayer(data);
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error(e.getMessage());
            return event.reply("Coś poszło nie tak, spróbuj ponownie");
        } catch (IOException e) {
            logger.error("Error during save player to list", e);
            return event.reply("Coś poszło nie tak, spróbuj ponownie");
        }

        return event.reply("Pomyślnie dodano gracza do czarnej listy");
    }

    private String getDataToSave(Map<String, Optional<String>> inputMap) {
        return String.join(";",
                inputMap.get(CommandsService.BLACKLIST_FORM_PLAYER_INPUT).orElseThrow(() -> new IllegalStateException("Player name should be exists")),
                inputMap.get(CommandsService.BLACKLIST_FORM_REASON_INPUT).orElseThrow(() -> new IllegalStateException("Reason should be exists")),
                OPTION_3 + inputMap.get(CommandsService.BLACKLIST_FORM_REPORTING_PERSON_INPUT).orElse(""),
                OPTION_4 + inputMap.get( CommandsService.BLACKLIST_FORM_PLAYER_LEVEL_INPUT).orElse(""));
    }

    private Optional<String> validateInputAndReturn(TextInput textInput) {
        String text = textInput.getValue().orElse("");
        if (text.contains(";") || text.contains("\n")) {
            throw new IllegalArgumentException("Formularz nie może zawierać znaku ;");
        }
        return textInput.getValue();
    }
}
