package pl.damian.bodzioch.configuration;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.damian.bodzioch.EventListeners.EventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Configuration
public class BotConfiguration {
    private final String token = "MTA2ODQ5NjQwNTUxMTAyMDU5NA.GgmBbO.yPt19IsVdnDp2-A9vjrsnCkOM_cXSxdgBxnA1s";
    private static final Logger logger = LoggerFactory.getLogger(BotConfiguration.class);
    public static final List<String> heroNames = getHeroNames();
    public static final List<String> heroSiatkaList = getHeroSiatkaList();
    public static final long applicationId = 1068496405511020594L; //or long applicationId = client.getRestClient().getApplicationId().block();
    public static final long guildId = 879059717358485574L;

    @Autowired
    Commands commands;

    @Bean
    public <T extends Event> GatewayDiscordClient getBotClient(List<EventListener<T>> eventListeners) {
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();
        commands.setHeroCommand(gateway);
        for (EventListener<T> listener : eventListeners) {
            gateway.on(listener.getEventType()).flatMap(listener::processCommand).subscribe();
        }
        gateway.onDisconnect().block();
        return gateway;
    }

    private static List<String> getHeroNames(){
        ArrayList<String> heroNames = new ArrayList<>();
        String filePath = "src/main/resources/hero_names.txt";
        try {
            Scanner scanner = new Scanner(new File(filePath));
            scanner.useDelimiter(";");
            while (scanner.hasNext()){
                heroNames.add(scanner.next());
            }
            heroNames.sort(String::compareTo);
            return heroNames;
        }catch (FileNotFoundException exception) {
            logger.error("Nie udało się otworzyć pliku hero_names.txt");
        }

        return Collections.emptyList();
    }

    private static List<String> getHeroSiatkaList() {
        ArrayList<String> heroNames = new ArrayList<>();
        String filePath = "src/main/resources/siatka_list.txt";
        try {
            Scanner scanner = new Scanner(new File(filePath));
            scanner.useDelimiter(";");
            while (scanner.hasNext()){
                heroNames.add(scanner.next());
            }
            return heroNames;
        }catch (FileNotFoundException exception) {
            logger.error("Nie udało się otworzyć pliku siatka_list.txt");
        }

        return Collections.emptyList();
    }
}
