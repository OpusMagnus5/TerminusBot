package pl.damian.bodzioch.configuration;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Commands {

    public final String HERO_COMMAND = "hero";
    public final String BOHATER_HERO_COMMAND_OPTION = "bohater";
    public final String KALENDARZ_COMMAND = "kalendarz";
    public final String SPEED_COMMAND = "speed";
    public final String BASE_OPTION_SPEED_COMMAND = "baza";
    public final String SIATKA_OPTION_SPEED_COMMAND = "siatka";
    public final String GUN_OPTION_SPEED_COMMAND = "bron";
    public final String FAMILY_OPTION_SPEED_COMMAND = "rodzina";
    public final String SKILL_OPTION_SPEED_COMMAND = "skill";
    public final String ADD_PLAYER_TO_BLACKLIST_COMMAND = "blacklist-add";
    public final String BLACKLIST_FORM_MODAL = "blacklistForm";
    public final String BLACKLIST_FORM_PLAYER_INPUT = "blackListFormPlayerInput";
    public final String BLACKLIST_FORM_REASON_INPUT = "blackListFormReasonInput";
    public final String BLACKLIST_FORM_PLAYER_LEVEL_INPUT = "blackListFormPlayerLevelInput";
    public final String BLACKLIST_FORM_OTHER_NAMES_INPUT = "blackListFormOtherNamesInput";
    public final String BLACKLIST_FORM_REPORTING_PERSON_INPUT = "blackListFormReportingPersonInput";

    public void setAllCommands(GatewayDiscordClient client) {
        List<ApplicationCommandRequest> commands = new ArrayList<>();
        commands.add(setHeroCommand());
        commands.add(setKalendarzCommand());
        commands.add(setSpeedCommand());
        commands.add(setAddPlayerToBlackList());
        activateCommand(commands, client);
    }

    private ApplicationCommandRequest setHeroCommand() {
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

    private ApplicationCommandRequest setKalendarzCommand() {
        return ApplicationCommandRequest.builder()
                .name(KALENDARZ_COMMAND)
                .description("Wyświetla aktualny kalendarz.")
                .build();
    }

    private ApplicationCommandRequest setSpeedCommand() {
        return ApplicationCommandRequest.builder()
                .name(SPEED_COMMAND)
                .description("Sprawdza czy uda się przełamać szybkość bohatera")
                .addAllOptions(List.of(buildBaseOptionSpeedCommand(), buildSiatkaBonusOptionSpeedCommand(),
                        buildGunBonusOptionSpeedCommand(), buildFamilyBonusOptionSpeedCommand(), buildSkillBonusOptionSpeedCommand()))
                .build();
    }

    private ApplicationCommandRequest setAddPlayerToBlackList() {
        int manageGuildPermission = 1 << 5;
        return ApplicationCommandRequest.builder()
                .defaultMemberPermissions(Integer.toString(manageGuildPermission))
                .name(ADD_PLAYER_TO_BLACKLIST_COMMAND)
                .description("Dodaje gracza do czarnej listy")
                .build();
    }

    private ImmutableApplicationCommandOptionData buildBaseOptionSpeedCommand() {
        return ApplicationCommandOptionData.builder()
                .name(BASE_OPTION_SPEED_COMMAND)
                .description("Podaj szybkość bazową karty.")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .required(true)
                .minValue(1D)
                .maxValue(100D)
                .build();
    }

    private ImmutableApplicationCommandOptionData buildSiatkaBonusOptionSpeedCommand() {
        return ApplicationCommandOptionData.builder()
                .name(SIATKA_OPTION_SPEED_COMMAND)
                .description("Podaj procent do szybkości ze siatki")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .choices(List.of(ApplicationCommandOptionChoiceData.builder()
                                .name("2%")
                                .value(2)
                                .build(),
                        ApplicationCommandOptionChoiceData.builder()
                                .name("4%")
                                .value(4)
                                .build()))
                .build();
    }

    private ImmutableApplicationCommandOptionData buildGunBonusOptionSpeedCommand() {
        return ApplicationCommandOptionData.builder()
                .name(GUN_OPTION_SPEED_COMMAND)
                .description("Podaj bonus szybkości broni")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .minValue(0D)
                .maxValue(9D)
                .build();
    }

    private ImmutableApplicationCommandOptionData buildFamilyBonusOptionSpeedCommand() {
        return ApplicationCommandOptionData.builder()
                .name(FAMILY_OPTION_SPEED_COMMAND)
                .description("Podaj bonus szybkości rodziny")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .minValue(0D)
                .maxValue(100D)
                .build();
    }

    private ImmutableApplicationCommandOptionData buildSkillBonusOptionSpeedCommand() {
        return ApplicationCommandOptionData.builder()
                .name(SKILL_OPTION_SPEED_COMMAND)
                .description("Podaj bonus szybkości umiejętności")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .minValue(0D)
                .maxValue(100D)
                .build();
    }

    private void activateCommand(List<ApplicationCommandRequest> commands, GatewayDiscordClient client) {
        client.getRestClient().getApplicationService()
                .bulkOverwriteGlobalApplicationCommand(BotConfiguration.applicationId,  commands)
                .subscribe();
    }

    //TODO jak dodachą OAuth2 dopsisać ustawianie uprawnien dla komend
}
