package pl.damian.bodzioch.configuration;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

@Component
public class Commands {

    public static final String HERO_COMMAND = "hero";
    public static final String BOHATER_COMMAND_OPTION = "bohater";

    public void setHeroCommand(GatewayDiscordClient client){

        ApplicationCommandRequest command = ApplicationCommandRequest.builder()
                .name(HERO_COMMAND)
                .description("Pobiera zdjęcie bohatera.")
                .addOption(ApplicationCommandOptionData.builder()
                        .name(BOHATER_COMMAND_OPTION)
                        .description("Nazwa bohatera")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .autocomplete(true)
                        .build())
                .build();

        client.getRestClient().getApplicationService()
                .createGuildApplicationCommand(BotConfiguration.applicationId, BotConfiguration.guildId, command);
    }


}
