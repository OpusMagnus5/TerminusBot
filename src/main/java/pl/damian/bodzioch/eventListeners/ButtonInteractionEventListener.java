package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.dao.SiatkaWariantowDAO;
import pl.damian.bodzioch.fileService.ListService;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ButtonInteractionEventListener implements EventListener<ButtonInteractionEvent>{

    public static final String SIATKA_TYPE = "siatka";
    public static final String WARIANT_TYPE = "wariant";
    public static final String SIATKA_WARIANTU_TYPE = "siatka_for_wariant";

    @Autowired
    Logger logger;
    @Autowired
    ListService listService;
    @Autowired
    SiatkaWariantowDAO siatkaWariantowDAO;

    @Override
    public Class<ButtonInteractionEvent> getEventType() {
        return ButtonInteractionEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ButtonInteractionEvent event) {
        String buttonId = event.getInteraction().getData().data().get().customId().get();
        String buttonType = getButtonType(buttonId);
        switch (buttonType){
            case SIATKA_TYPE:
                return event.reply(buildSiatkaButtonResponse(buttonId.replace(SIATKA_TYPE, ""))).onErrorResume(this::handleError);
            case WARIANT_TYPE:
                return event.reply(buildWariantButtonResponse(buttonId.replace(WARIANT_TYPE, ""))).onErrorResume(this::handleError);
            case SIATKA_WARIANTU_TYPE:
                return event.reply(buildSiatkaWariantuButtonResponse(buttonId.replace(SIATKA_WARIANTU_TYPE, ""))).onErrorResume(this::handleError);
            default:
                return Mono.empty();
        }
    }

    @Override
    public Mono<Void> handleError(Throwable error) {
        logger.info("Error during process ButtonInteractionEventListener");
        return Mono.empty();
    }

    private InteractionApplicationCommandCallbackSpec buildSiatkaButtonResponse(String heroName) {
        InteractionApplicationCommandCallbackSpec response;
        try {
            logger.info("Handling SiatkaButton event");
            String fileDir = listService.RESOURCE_DIR + listService.SIATKA_DIR + heroName + listService.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            logger.warn("File not found", e);
            return InteractionApplicationCommandCallbackSpec.builder().content("Coś poszło nie tak.").build();
        }
        return response;
    }

    private InteractionApplicationCommandCallbackSpec buildWariantButtonResponse(String heroName) {
        InteractionApplicationCommandCallbackSpec response;
        try {
            logger.info("Handling WariantButton event");
            String fileDir = listService.RESOURCE_DIR + listService.WARIANT_DIR + heroName + listService.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            logger.warn("File not found", e);
            return InteractionApplicationCommandCallbackSpec.builder().content("Coś poszło nie tak.").build();
        }
        List<Button> buttonsList = new ArrayList<>();
        if (isWariantHaveSiatka(heroName)) {
            buttonsList.add(Button.primary(SIATKA_WARIANTU_TYPE + heroName, "Pokaż siatkę dla wariantu"));
        }
        return response.withComponents(ActionRow.of(buttonsList));
    }

    private boolean isWariantHaveSiatka(String heroName) {
        return siatkaWariantowDAO.getSiatkaWariantowByHeroName(heroName).isPresent();
    }

    private InteractionApplicationCommandCallbackSpec buildSiatkaWariantuButtonResponse(String heroName) {
        InteractionApplicationCommandCallbackSpec response;
        try {
            logger.info("Handling SiatkaWariantuButton event");
            String fileDir = listService.RESOURCE_DIR + listService.SIATKA_WARIANTU_DIR + heroName + listService.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            logger.warn("File not found", e);
            return InteractionApplicationCommandCallbackSpec.builder().content("Coś poszło nie tak.").build();
        }
        return response;
    }

    private String getButtonType(String buttonName) {
        if (buttonName.contains(SIATKA_TYPE)) {
            return SIATKA_TYPE;
        }
        if (buttonName.contains(WARIANT_TYPE)) {
            return WARIANT_TYPE;
        }
        if (buttonName.contains(SIATKA_WARIANTU_TYPE)){
            return SIATKA_WARIANTU_TYPE;
        }
        return "";
    }
    
    
}
