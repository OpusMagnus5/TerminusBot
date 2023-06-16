package pl.damian.bodzioch.commands;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddToBlackListCommand {

    public static final String ADD_PLAYER_TO_BLACKLIST_COMMAND = "blacklist-add";
    public static final String DEFENSE_PHOTO_OPTION_ADD_PLAYER_TO_BLACKLIST_COMMAND = "defense-photo";
    public static final String PLAYER_NAME_OPTION_ADD_PLAYER_TO_BLACKLIST_COMMAND = "player-name-option-add-blacklist";

    public ApplicationCommandRequest setAddPlayerToBlackList() {
        int manageGuildPermission = 1 << 5;
        return ApplicationCommandRequest.builder()
                .defaultMemberPermissions(Integer.toString(manageGuildPermission))
                .name(ADD_PLAYER_TO_BLACKLIST_COMMAND)
                .description("Dodaje gracza do czarnej listy")
                .addAllOptions(List.of(
                        ApplicationCommandOptionData.builder()
                                .name(DEFENSE_PHOTO_OPTION_ADD_PLAYER_TO_BLACKLIST_COMMAND)
                                .description("Dodaj zdjęcie obrony gracza")
                                .type(ApplicationCommandOption.Type.ATTACHMENT.getValue())
                                .required(true)
                                .build(),
                        ApplicationCommandOptionData.builder()
                                .name(PLAYER_NAME_OPTION_ADD_PLAYER_TO_BLACKLIST_COMMAND)
                                .description("Podaj nazwę gracza")
                                .type(ApplicationCommandOption.Type.STRING.getValue())
                                .required(true)
                                .build()))
                .build();
    }
}
