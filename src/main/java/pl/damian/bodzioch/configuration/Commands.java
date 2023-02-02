package pl.damian.bodzioch.configuration;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Commands {

    public static final String HERO_COMMAND = "hero";
    public static final String BOHATER_COMMAND_OPTION = "bohater";
    public static final String KALENDARZ_COMMAND = "kalendarz";

    public void setAllCommands(GatewayDiscordClient client){
        List<ApplicationCommandRequest> commands = new ArrayList<>();
        commands.add(setHeroCommand());
        commands.add(setKalendarzCommand());
        activateCommand(commands, client);
    }

    private ApplicationCommandRequest setHeroCommand(){
        return ApplicationCommandRequest.builder()
                .name(HERO_COMMAND)
                .description("Wyświetla zdjęcie bohatera.")
                .addOption(ApplicationCommandOptionData.builder()
                        .name(BOHATER_COMMAND_OPTION)
                        .description("Nazwa bohatera")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .autocomplete(true)
                        .build())
                .build();
    }

    private ApplicationCommandRequest setKalendarzCommand(){
        return ApplicationCommandRequest.builder()
                .name(KALENDARZ_COMMAND)
                .description("Wyświetla aktualny kalendarz.")
                .build();
    }

    private void activateCommand(List<ApplicationCommandRequest> commands, GatewayDiscordClient client){
        client.getRestClient().getApplicationService()
                .bulkOverwriteGuildApplicationCommand(BotConfiguration.applicationId, BotConfiguration.guildId, commands)
                .subscribe();
    }
}
