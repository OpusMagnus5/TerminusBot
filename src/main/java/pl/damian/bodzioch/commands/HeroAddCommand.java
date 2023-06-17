package pl.damian.bodzioch.commands;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HeroAddCommand implements Command{

    public static final String HERO_ADD_COMMAND = "hero-add";
    public static final String HERO_ADD_COMMAND_ATTACHMENT_OPTION = "hero-add-attachment";
    public static final String HERO_ADD_COMMAND_NAME_OPTION = "hero-add-name";

    @Override
    public ApplicationCommandRequest setCommand() {
        int manageGuildPermission = 1 << 5;
        return ApplicationCommandRequest.builder()
                .defaultMemberPermissions(Integer.toString(manageGuildPermission))
                .name(HERO_ADD_COMMAND)
                .description("Dodaje kartę bohatera")
                .addAllOptions(List.of(
                        ApplicationCommandOptionData.builder()
                                .name(HERO_ADD_COMMAND_ATTACHMENT_OPTION)
                                .description("Dodaj zdjęcie karty")
                                .type(ApplicationCommandOption.Type.ATTACHMENT.getValue())
                                .required(true)
                                .build(),
                        ApplicationCommandOptionData.builder()
                                .name(HERO_ADD_COMMAND_NAME_OPTION)
                                .description("Podaj nazwę karty")
                                .type(ApplicationCommandOption.Type.STRING.getValue())
                                .required(true)
                                .build()))
                .build();
    }
}
