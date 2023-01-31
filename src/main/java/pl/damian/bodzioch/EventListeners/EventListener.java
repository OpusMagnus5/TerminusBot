package pl.damian.bodzioch.EventListeners;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public interface EventListener<T extends Event> {
     Class<T> getEventType();
     Mono<Void> processCommand(T event);
}
