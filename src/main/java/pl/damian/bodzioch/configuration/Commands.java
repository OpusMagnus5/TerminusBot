package pl.damian.bodzioch.configuration;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

@Component
public class Commands {
    public void setHeroCommand(GatewayDiscordClient client){

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

        client.getRestClient().getApplicationService()
                .createGuildApplicationCommand(BotConfiguration.applicationId, BotConfiguration.guildId, command);
    }
}
