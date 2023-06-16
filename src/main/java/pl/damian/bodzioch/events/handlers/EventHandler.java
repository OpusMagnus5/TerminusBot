package pl.damian.bodzioch.events.handlers;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public interface EventHandler<T extends Event> {
    Mono<Void> handleEvent(T event, String param);
}
