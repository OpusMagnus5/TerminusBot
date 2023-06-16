package pl.damian.bodzioch.commands;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

@Component
public class HeroCommand {
    public static final String HERO_COMMAND = "hero";
    public static final String BOHATER_HERO_COMMAND_OPTION = "bohater";

    public ApplicationCommandRequest setHeroCommand() {
        return ApplicationCommandRequest.builder()
                .name(HERO_COMMAND)
                .description("Wyświetla zdjęcie bohatera.")
                .addOption(ApplicationCommandOptionData.builder()
                        .name(BOHATER_HERO_COMMAND_OPTION)
                        .description("Nazwa bohatera")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .autocomplete(true)
                        .build())
                .build();
    }
}
