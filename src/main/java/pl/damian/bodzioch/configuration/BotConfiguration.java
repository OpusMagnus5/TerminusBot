package pl.damian.bodzioch.configuration;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.damian.bodzioch.EventListener;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Configuration
public class BotConfiguration {
    private final String token = "MTA2ODQ5NjQwNTUxMTAyMDU5NA.GgmBbO.yPt19IsVdnDp2-A9vjrsnCkOM_cXSxdgBxnA1s";
    private final Logger logger = LoggerFactory.getLogger(BotConfiguration.class);
    private final List<String> heroNames = getHeroNames();
    private final long applicationId = 1068496405511020594L; //or long applicationId = client.getRestClient().getApplicationId().block();
    private final long guildId = 879059717358485574L;

    @Bean
    public <T extends Event> GatewayDiscordClient getBotClient(List<EventListener<T>> eventListeners) {
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();
        setHeroCommand(client);
        for (EventListener<T> listener : eventListeners) {
            gateway.on(listener.getEventType()).flatMap(listener::processCommand).subscribe();
        }
        gateway.onDisconnect().block();
        return gateway;
    }

    public void setHeroCommand(DiscordClient client){

        ApplicationCommandRequest command = ApplicationCommandRequest.builder()
                .name("hero")
                .description("Pobiera zdjęcie bohatera.")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("bohater")
                        .description("Nazwa bohatera")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .autocomplete(true)
                        .build())
                .build();

        client.withGateway((GatewayDiscordClient gateway) -> setHeroCommand(gateway, command));
    }

    public Mono<Void> setHeroCommand(GatewayDiscordClient gateway, ApplicationCommandRequest command) {
        gateway.getRestClient().getApplicationService().createGuildApplicationCommand(applicationId, guildId, command).subscribe();
        return Mono.empty();
    }

    private List<String> getHeroNames(){
        ArrayList<String> heroNames = new ArrayList<>();
        String filePath = "src/main/resources/hero_names.txt";
        try {
            Scanner scanner = new Scanner(new File(filePath));
            scanner.useDelimiter(";");
            while (scanner.hasNext()){
                heroNames.add(scanner.nextLine());
            }
            return heroNames;
        }catch (FileNotFoundException exception) {
            logger.error("Nie udało się otworzyć pliku hero_names.txt");
        }

        return Collections.emptyList();
    }

    private List<String> getFewChoicesOfHero(String userInput) {
        List<String> options = heroNames.stream()
                .filter(name -> name.contains(userInput))
                .toList();

        if (options.size() < 5) {
            return options;
        }

        return options.subList(0, 4);
    }
}
