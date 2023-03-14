package pl.damian.bodzioch.configuration;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.damian.bodzioch.eventListeners.EventListener;
import pl.damian.bodzioch.fileService.ListService;

import java.util.*;

@Configuration
@EnableScheduling
public class BotConfiguration {

    private final String TOKEN = "MTA2ODQ5NjQwNTUxMTAyMDU5NA.GgmBbO.yPt19IsVdnDp2-A9vjrsnCkOM_cXSxdgBxnA1s";
    public static final long applicationId = 1068496405511020594L;

    @Bean
    public <T extends Event> GatewayDiscordClient getBotClient(List<EventListener<T>> eventListeners) {
        final DiscordClient client = DiscordClient.create(TOKEN);
        final GatewayDiscordClient gateway = client.login().block();
        Commands.setAllCommands(gateway);
        logger().info("Aktywowano komendy");
        ListService.updateAllLists();
        logger().info("Zaktualizowano listy");
        for (EventListener<T> listener : eventListeners) {
            gateway.on(listener.getEventType()).flatMap(listener::processCommand).subscribe();
        }
        gateway.onDisconnect().block();
        return gateway;
    }

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger("pl.damian.bodzioch");
    }


}
