package pl.damian.bodzioch.configuration;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.damian.bodzioch.eventListeners.EventListener;
import pl.damian.bodzioch.fileService.DataInLists;
import pl.damian.bodzioch.fileService.ListService;

import java.util.*;

@Configuration
@EnableScheduling
public class BotConfiguration {

    private final String TOKEN = "MTA2ODQ5NjQwNTUxMTAyMDU5NA.GgmBbO.yPt19IsVdnDp2-A9vjrsnCkOM_cXSxdgBxnA1s";
    public static final long applicationId = 1068496405511020594L;

    @Autowired
    Commands commands;
    @Autowired
    ListService listService;

    @Bean
    public <T extends Event> GatewayDiscordClient getBotClient(List<EventListener<T>> eventListeners) {
        final DiscordClient client = DiscordClient.create(TOKEN);
        final GatewayDiscordClient gateway = client.login().block();
        commands.setAllCommands(gateway);
        updateAllLists();
        for (EventListener<T> listener : eventListeners) {
            gateway.on(listener.getEventType()).flatMap(listener::processCommand).subscribe();
        }
        gateway.onDisconnect().block();
        return gateway;
    }

    @Scheduled(cron = "@daily")
    private void updateAllLists() {
        DataInLists.HERO_NAMES = listService.updateList(ListService.HERO_DIR);
        DataInLists.HERO_SIATKA_LIST = listService.updateList(ListService.SIATKA_DIR);
        DataInLists.HERO_WARIANT_LIST = listService.updateList(ListService.WARIANT_DIR);
        DataInLists.SIATKI_WARIANTOW_LIST = listService.updateList(ListService.SIATKA_WARIANTU_DIR);
    }
}
