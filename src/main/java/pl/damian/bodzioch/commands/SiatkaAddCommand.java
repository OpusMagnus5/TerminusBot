package pl.damian.bodzioch.commands;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SiatkaAddCommand {

    public static final String SIATKA_ADD_COMMAND = "siatka-add";
    public static final String SIATKA_ADD_COMMAND_ATTACHMENT_OPTION = "siatka-add-attachment";
    public static final String SIATKA_ADD_COMMAND_NAME_OPTION = "siatka-add-name";

    public ApplicationCommandRequest setSiatkaAddCommand() {
        int manageGuildPermission = 1 << 5;
        return ApplicationCommandRequest.builder()
                .defaultMemberPermissions(Integer.toString(manageGuildPermission))
                .name(SIATKA_ADD_COMMAND)
                .description("Dodaje siatkę bohatera")
                .addAllOptions(List.of(
                        ApplicationCommandOptionData.builder()
                                .name(SIATKA_ADD_COMMAND_ATTACHMENT_OPTION)
                                .description("Dodaj siatki")
                                .type(ApplicationCommandOption.Type.ATTACHMENT.getValue())
                                .required(true)
                                .build(),
                        ApplicationCommandOptionData.builder()
                                .name(SIATKA_ADD_COMMAND_NAME_OPTION)
                                .description("Podaj nazwę karty dla siatki")
                                .type(ApplicationCommandOption.Type.STRING.getValue())
                                .required(true)
                                .build()))
                .build();
    }

}
