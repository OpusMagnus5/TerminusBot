package pl.damian.bodzioch.events.handlers.button;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.dao.SiatkaWariantowDAO;
import pl.damian.bodzioch.events.handlers.EventHandler;
import pl.damian.bodzioch.fileService.FileServiceImpl;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WariantButtonHandler implements EventHandler<ButtonInteractionEvent> {

    public static final String SIATKA_WARIANTU_TYPE = "siatka_for_wariant";

    @Autowired
    Logger logger;

    @Autowired
    SiatkaWariantowDAO siatkaWariantowDAO;

    @Override
    public Mono<Void> handleEvent(ButtonInteractionEvent event, String param) {
        InteractionApplicationCommandCallbackSpec response;
        try {
            logger.info("Handling WariantButton event");
            String fileDir = FileServiceImpl.RESOURCE_DIR + FileServiceImpl.WARIANT_DIR + param + FileServiceImpl.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            logger.warn("File not found", e);
            return event.reply("Coś poszło nie tak, spróbuj ponownie");
        }
        List<Button> buttonsList = new ArrayList<>();
        if (isWariantHaveSiatka(param)) {
            buttonsList.add(Button.primary(SIATKA_WARIANTU_TYPE + "-" + param, "Pokaż siatkę dla wariantu"));
        }
        return event.reply(response.withComponents(ActionRow.of(buttonsList)));
    }

    private boolean isWariantHaveSiatka(String heroName) {
        return siatkaWariantowDAO.getSiatkaWariantowByHeroName(heroName).isPresent();
    }
}
