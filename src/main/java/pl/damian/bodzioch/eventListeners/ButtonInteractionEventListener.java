package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.fileService.FileConstans;
import reactor.core.publisher.Mono;

@Service
public class ButtonInteractionEventListener implements EventListener<ButtonInteractionEvent>{

    public static final String SIATKA_TYPE = "siatka";

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
            default:
                return Mono.empty();
        }
    }

    @Override
    public Mono<Void> handleError(Throwable error) {
        return Mono.empty();
    }

    private InteractionApplicationCommandCallbackSpec buildSiatkaButtonResponse(String heroName) {
        return InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .image(ListenersConstans.PHOTOS_URL + ListenersConstans.SIATKA_PATH + heroName + FileConstans.JPG_FILE_EXTENSION)
                        .build())
                .build();
    }

    private String getButtonType(String buttonName) {
        if (buttonName.contains(SIATKA_TYPE)){
            return SIATKA_TYPE;
        }
        return "";
    }
    
    
}
