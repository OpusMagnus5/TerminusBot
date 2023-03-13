package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.fileService.DataInLists;
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
        return Mono.empty();
    }

    private InteractionApplicationCommandCallbackSpec buildSiatkaButtonResponse(String heroName) {
        InteractionApplicationCommandCallbackSpec response;
        try {
            String fileDir = ListService.RESOURCE_DIR + ListService.SIATKA_DIR + heroName + ListService.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            return InteractionApplicationCommandCallbackSpec.builder().content("Coś poszło nie tak.").build();
        }
        return response;
    }

    private InteractionApplicationCommandCallbackSpec buildWariantButtonResponse(String heroName) {
        InteractionApplicationCommandCallbackSpec response;
        try {
            String fileDir = ListService.RESOURCE_DIR + ListService.WARIANT_DIR + heroName + ListService.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            return InteractionApplicationCommandCallbackSpec.builder().content("Coś poszło nie tak.").build();
        }
        List<Button> buttonsList = new ArrayList<>();
        if (isWariantHaveSiatka(heroName)) {
            buttonsList.add(Button.primary(SIATKA_WARIANTU_TYPE + heroName, "Pokaż siatkę dla wariantu"));
        }
        return response.withComponents(ActionRow.of(buttonsList));
    }

    private boolean isWariantHaveSiatka(String heroName) {
        return DataInLists.SIATKI_WARIANTOW_LIST.contains(heroName);
    }

    private InteractionApplicationCommandCallbackSpec buildSiatkaWariantuButtonResponse(String heroName) {
        InteractionApplicationCommandCallbackSpec response;
        try {
            String fileDir = ListService.RESOURCE_DIR + ListService.SIATKA_WARIANTU_DIR + heroName + ListService.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
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
