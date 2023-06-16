package pl.damian.bodzioch.events.listeners;

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.events.handlers.chatinput.AddToBlackListCommandHandler;
import pl.damian.bodzioch.events.handlers.modalsubmit.AddToBlackListFormHandler;
import reactor.core.publisher.Mono;

@Service
public class ModalSubmitInteractionEventListener implements EventListener<ModalSubmitInteractionEvent> {

    private static final String ADD_TO_BLACKLIST_TYPE = AddToBlackListCommandHandler.BLACKLIST_FORM_MODAL;

    @Autowired
    Logger logger;
    @Autowired
    AddToBlackListFormHandler addToBlackListFormHandler;

    @Override
    public Class<ModalSubmitInteractionEvent> getEventType() {
        return ModalSubmitInteractionEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ModalSubmitInteractionEvent event) {
        String eventName = event.getCustomId();
        return switch (eventName) {
            case ADD_TO_BLACKLIST_TYPE -> addToBlackListFormHandler.handleEvent(event, null).onErrorResume(error -> handleError(error, event));
            default -> Mono.empty();
        };
    }

    @Override
    public Mono<Void> handleError(Throwable error, ModalSubmitInteractionEvent event) {
        logger.info("Error during process ModalSubmitInteractionEvent", error);
        return event.reply(error.getMessage());
    }
}
