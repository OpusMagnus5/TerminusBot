package pl.damian.bodzioch.commands;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WariantAddCommand implements Command{

    public static final String WARIANT_ADD_COMMAND = "wariant-add";
    public static final String WARIANT_ADD_COMMAND_ATTACHMENT_OPTION = "wariant-add-attachment";
    public static final String WARIANT_ADD_COMMAND_NAME_OPTION = "wariant-add-name";

    @Override
    public ApplicationCommandRequest setCommand() {
        int manageGuildPermission = 1 << 5;
        return ApplicationCommandRequest.builder()
                .defaultMemberPermissions(Integer.toString(manageGuildPermission))
                .name(WARIANT_ADD_COMMAND)
                .description("Dodaje kartę wariantu bohatera")
                .addAllOptions(List.of(
                        ApplicationCommandOptionData.builder()
                                .name(WARIANT_ADD_COMMAND_ATTACHMENT_OPTION)
                                .description("Dodaj zdjęcie karty wariantu")
                                .type(ApplicationCommandOption.Type.ATTACHMENT.getValue())
                                .required(true)
                                .build(),
                        ApplicationCommandOptionData.builder()
                                .name(WARIANT_ADD_COMMAND_NAME_OPTION)
                                .description("Podaj nazwę karty wariantu")
                                .type(ApplicationCommandOption.Type.STRING.getValue())
                                .required(true)
                                .build()))
                .build();
    }
}
