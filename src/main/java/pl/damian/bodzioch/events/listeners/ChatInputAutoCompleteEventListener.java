package pl.damian.bodzioch.events.listeners;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.commands.HeroCommand;
import pl.damian.bodzioch.events.handlers.autocomplete.HeroCommandCompleteHandler;
import pl.damian.bodzioch.dao.HeroDAO;
import reactor.core.publisher.Mono;

@Service
public class ChatInputAutoCompleteEventListener implements EventListener<ChatInputAutoCompleteEvent> {

    private static final String HERO_COMMAND_COMPLETE = HeroCommand.HERO_COMMAND;

    @Autowired
    HeroCommandCompleteHandler heroCommandCompleteHandler;

    @Override
    public Class<ChatInputAutoCompleteEvent> getEventType() {
        return ChatInputAutoCompleteEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ChatInputAutoCompleteEvent event) {
        String commandName = event.getCommandName();
        return switch (commandName) {
            case HERO_COMMAND_COMPLETE -> heroCommandCompleteHandler.handleEvent(event, null).onErrorResume(error -> handleError(error, event));
            default -> Mono.empty();
        };
    }

    @Override
    public Mono<Void> handleError(Throwable error, ChatInputAutoCompleteEvent event) {
        return Mono.empty();
    }
}
