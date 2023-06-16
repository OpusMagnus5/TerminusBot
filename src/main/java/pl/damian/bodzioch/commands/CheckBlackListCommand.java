package pl.damian.bodzioch.commands;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

@Component
public class CheckBlackListCommand {

    public static final String CHECK_BLACKLIST_COMMAND = "blacklist";
    public static final String PLAYER_NAME_OPTION = "player-name";

    public ApplicationCommandRequest setCheckBlackListCommand() {
        return ApplicationCommandRequest.builder()
                .name(CHECK_BLACKLIST_COMMAND)
                .description("Sprawdza gracza czy znajduje się na czarnej liście")
                .addOption(ApplicationCommandOptionData.builder()
                        .name(PLAYER_NAME_OPTION)
                        .description("Nazwa gracza")
                        .required(true)
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .build())
                .build();
    }
}
