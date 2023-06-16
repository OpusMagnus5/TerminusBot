package pl.damian.bodzioch.configuration;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.gateway.intent.IntentSet;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pl.damian.bodzioch.events.listeners.EventListener;
import pl.damian.bodzioch.fileService.FileService;
import pl.damian.bodzioch.service.CommandsService;

import java.util.*;

@Configuration
public class BotConfiguration {

    private final String TOKEN = "MTA2ODQ5NjQwNTUxMTAyMDU5NA.GgmBbO.yPt19IsVdnDp2-A9vjrsnCkOM_cXSxdgBxnA1s";
    public static final long applicationId = 1068496405511020594L;

    @Autowired
    FileService FIleService;
    @Autowired
    CommandsService commandsService;
    @Autowired
    Logger logger;

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        final DiscordClient client = DiscordClient.create(TOKEN);
        final GatewayDiscordClient gateway = client.gateway().setEnabledIntents(IntentSet.all()).login().block();
        commandsService.setAllCommands(gateway);
        logger.info("Aktywowano komendy");
        FIleService.updateAllLists();
        logger.info("Zaktualizowano listy");
        for (EventListener<T> listener : eventListeners) {
            gateway.on(listener.getEventType()).flatMap(listener::processCommand).subscribe();
        }
        gateway.onDisconnect().block();
        return gateway;
    }

    @Bean
    public WebClient webClient() {
        final int size = 4 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codec -> codec.defaultCodecs().maxInMemorySize(size))
                .build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }
}
