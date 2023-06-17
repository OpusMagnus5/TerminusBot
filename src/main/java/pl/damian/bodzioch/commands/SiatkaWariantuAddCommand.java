package pl.damian.bodzioch.commands;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SiatkaWariantuAddCommand implements Command{
    public static final String SIATKA_WARIANTU_ADD_COMMAND = "siatka_wariantu-add";
    public static final String SIATKA_WARIANTU_ADD_COMMAND_ATTACHMENT_OPTION = "siatka_wariantu-add-attachment";
    public static final String SIATKA_WARIANTU_COMMAND_NAME_OPTION = "siatka_wariantu-add-name";

    @Override
    public ApplicationCommandRequest setCommand() {
        int manageGuildPermission = 1 << 5;
        return ApplicationCommandRequest.builder()
                .defaultMemberPermissions(Integer.toString(manageGuildPermission))
                .name(SIATKA_WARIANTU_ADD_COMMAND)
                .description("Dodaje siatkę dla wariantu bohatera")
                .addAllOptions(List.of(
                        ApplicationCommandOptionData.builder()
                                .name(SIATKA_WARIANTU_ADD_COMMAND_ATTACHMENT_OPTION)
                                .description("Dodaj zdjęcie siatki dla wariantu")
                                .type(ApplicationCommandOption.Type.ATTACHMENT.getValue())
                                .required(true)
                                .build(),
                        ApplicationCommandOptionData.builder()
                                .name(SIATKA_WARIANTU_COMMAND_NAME_OPTION)
                                .description("Podaj nazwę wariantu dla którego dodajesz siatkę")
                                .type(ApplicationCommandOption.Type.STRING.getValue())
                                .required(true)
                                .build()))
                .build();
    }

}
