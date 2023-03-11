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
import pl.damian.bodzioch.fileService.HeroList;
import pl.damian.bodzioch.fileService.SiatkaList;
import pl.damian.bodzioch.fileService.SiatkiWariantowList;
import pl.damian.bodzioch.fileService.WariantList;

import java.util.*;

@Configuration
@EnableScheduling
public class BotConfiguration {

    private final String token = "MTA2ODQ5NjQwNTUxMTAyMDU5NA.GgmBbO.yPt19IsVdnDp2-A9vjrsnCkOM_cXSxdgBxnA1s";
    public static final long applicationId = 1068496405511020594L;
    public static final long guildId = 879059717358485574L;
    /*public static final long guildId = 1084093399453421681L;*/

    @Autowired
    Commands commands;
    @Autowired
    HeroList heroList;
    @Autowired
    SiatkaList siatkaList;
    @Autowired
    WariantList wariantList;
    @Autowired
    SiatkiWariantowList siatkiWariantowList;

    @Bean
    public <T extends Event> GatewayDiscordClient getBotClient(List<EventListener<T>> eventListeners) {
        final DiscordClient client = DiscordClient.create(token);
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
        heroList.updateHeroList();
        siatkaList.updateSiatkaList();
        wariantList.updateWariantList();
        siatkiWariantowList.updateSiatkiWariantowList();
    }
}
