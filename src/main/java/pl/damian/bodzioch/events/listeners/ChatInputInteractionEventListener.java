package pl.damian.bodzioch.events.listeners;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.commands.*;
import pl.damian.bodzioch.events.handlers.chatinput.*;
import reactor.core.publisher.Mono;

@Service
public class ChatInputInteractionEventListener implements EventListener<ChatInputInteractionEvent> {

    private static final String HERO_TYPE = HeroCommand.HERO_COMMAND;
    private static final String CALENDAR_TYPE = CalendarCommand.KALENDARZ_COMMAND;
    private static final String SPEED_TYPE = SpeedCommand.SPEED_COMMAND;
    private static final String ADD_TO_BLACKLIST_TYPE = AddToBlackListCommand.ADD_PLAYER_TO_BLACKLIST_COMMAND;
    private static final String CHECK_BLACKLIST_TYPE = CheckBlackListCommand.CHECK_BLACKLIST_COMMAND;

    @Autowired
    Logger logger;
    @Autowired
    HeroCommandHandler heroCommandHandler;
    @Autowired
    CalendarCommandHandler calendarCommandHandler;
    @Autowired
    SpeedCommandHandler speedCommandHandler;
    @Autowired
    AddToBlackListCommandHandler addToBlackListCommandHandler;
    @Autowired
    CheckBlackListCommandHandler checkBlackListCommandHandler;

    @Override
    public Class<ChatInputInteractionEvent> getEventType() {
        return ChatInputInteractionEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ChatInputInteractionEvent event) {
        String eventName = event.getCommandName();
        return switch (eventName) {
            case HERO_TYPE -> heroCommandHandler.handleEvent(event, null).onErrorResume(error -> handleError(error, event));
            case CALENDAR_TYPE -> calendarCommandHandler.handleEvent(event, null).onErrorResume(error -> handleError(error, event));
            case SPEED_TYPE -> speedCommandHandler.handleEvent(event, null).onErrorResume(error -> handleError(error, event));
            case ADD_TO_BLACKLIST_TYPE -> addToBlackListCommandHandler.handleEvent(event, null).onErrorResume(error -> handleError(error, event));
            case CHECK_BLACKLIST_TYPE -> checkBlackListCommandHandler.handleEvent(event, null).onErrorResume(error -> handleError(error, event));
            default -> Mono.empty();
        };
    }

    @Override
    public Mono<Void> handleError(Throwable error, ChatInputInteractionEvent event) {
        logger.info("Error during process ChatInputInteractionEvent", error);
        return event.reply(error.getMessage());
    }
}
