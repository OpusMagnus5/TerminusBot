package pl.damian.bodzioch.events.listeners;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.events.handlers.button.SiatkaButtonHandler;
import pl.damian.bodzioch.events.handlers.button.SiatkaWariantuButtonHandler;
import pl.damian.bodzioch.events.handlers.button.WariantButtonHandler;
import pl.damian.bodzioch.events.handlers.chatinput.HeroCommandHandler;
import reactor.core.publisher.Mono;

@Service
public class ButtonInteractionEventListener implements EventListener<ButtonInteractionEvent> {

    private static final String SIATKA_BUTTON_ID = HeroCommandHandler.SIATKA_BUTTON_ID;
    private static final String WARIANT_BUTTON_ID = HeroCommandHandler.WARIANT_BUTTON_ID;
    private static final String SIATKA_WARIANTU_TYPE = WariantButtonHandler.SIATKA_WARIANTU_TYPE;

    @Autowired
    Logger logger;
    @Autowired
    SiatkaButtonHandler siatkaButtonHandler;
    @Autowired
    WariantButtonHandler wariantButtonHandler;
    @Autowired
    SiatkaWariantuButtonHandler siatkaWariantuButtonHandler;

    @Override
    public Class<ButtonInteractionEvent> getEventType() {
        return ButtonInteractionEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ButtonInteractionEvent event) {
        String buttonId = event.getInteraction().getData().data().get().customId().get();
        String buttonType = buttonId.split(HeroCommandHandler.BUTTON_DELIMITER)[0];
        String param = buttonId.split(HeroCommandHandler.BUTTON_DELIMITER)[1];
        return switch (buttonType) {
            case SIATKA_BUTTON_ID -> siatkaButtonHandler.handleEvent(event, param).onErrorResume(error -> handleError(error, event));
            case WARIANT_BUTTON_ID -> wariantButtonHandler.handleEvent(event, param).onErrorResume(error -> handleError(error, event));
            case SIATKA_WARIANTU_TYPE -> siatkaWariantuButtonHandler.handleEvent(event, param).onErrorResume(error -> handleError(error, event));
            default -> Mono.empty();
        };
    }

    @Override
    public Mono<Void> handleError(Throwable error, ButtonInteractionEvent event) {
        logger.info("Error during process ButtonInteractionEventListener");
        return event.reply(error.getMessage());
    }
}
