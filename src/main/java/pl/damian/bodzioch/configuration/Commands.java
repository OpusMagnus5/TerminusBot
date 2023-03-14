package pl.damian.bodzioch.configuration;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.*;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    public static final String HERO_COMMAND = "hero";
    public static final String BOHATER_HERO_COMMAND_OPTION = "bohater";
    public static final String KALENDARZ_COMMAND = "kalendarz";
    public static final String SPEED_COMMAND = "speed";
    public static final String BASE_OPTION_SPEED_COMMAND = "baza";
    public static final String SIATKA_OPTION_SPEED_COMMAND = "siatka";
    public static final String GUN_OPTION_SPEED_COMMAND = "bron";
    public static final String FAMILY_OPTION_SPEED_COMMAND = "rodzina";
    public static final String SKILL_OPTION_SPEED_COMMAND = "skill";

    public static void setAllCommands(GatewayDiscordClient client) {
        List<ApplicationCommandRequest> commands = new ArrayList<>();
        commands.add(setHeroCommand());
        commands.add(setKalendarzCommand());
        commands.add(setSpeedCommand());
        activateCommand(commands, client);
    }

    private static ApplicationCommandRequest setHeroCommand() {
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

    private static ApplicationCommandRequest setKalendarzCommand() {
        return ApplicationCommandRequest.builder()
                .name(KALENDARZ_COMMAND)
                .description("Wyświetla aktualny kalendarz.")
                .build();
    }

    private static ApplicationCommandRequest setSpeedCommand() {
        return ApplicationCommandRequest.builder()
                .name(SPEED_COMMAND)
                .description("Sprawdza czy uda się przełamać szybkość bohatera")
                .addAllOptions(List.of(buildBaseOptionSpeedCommand(), buildSiatkaBonusOptionSpeedCommand(),
                        buildGunBonusOptionSpeedCommand(), buildFamilyBonusOptionSpeedCommand(), buildSkillBonusOptionSpeedCommand()))
                .build();
    }

    private static ImmutableApplicationCommandOptionData buildBaseOptionSpeedCommand() {
        return ApplicationCommandOptionData.builder()
                .name(BASE_OPTION_SPEED_COMMAND)
                .description("Podaj szybkość bazową karty.")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .required(true)
                .minValue(1D)
                .maxValue(100D)
                .build();
    }

    private static ImmutableApplicationCommandOptionData buildSiatkaBonusOptionSpeedCommand() {
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

    private static ImmutableApplicationCommandOptionData buildGunBonusOptionSpeedCommand() {
        return ApplicationCommandOptionData.builder()
                .name(GUN_OPTION_SPEED_COMMAND)
                .description("Podaj bonus szybkości broni")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .minValue(0D)
                .maxValue(9D)
                .build();
    }

    private static ImmutableApplicationCommandOptionData buildFamilyBonusOptionSpeedCommand() {
        return ApplicationCommandOptionData.builder()
                .name(FAMILY_OPTION_SPEED_COMMAND)
                .description("Podaj bonus szybkości rodziny")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .minValue(0D)
                .maxValue(100D)
                .build();
    }

    private static ImmutableApplicationCommandOptionData buildSkillBonusOptionSpeedCommand() {
        return ApplicationCommandOptionData.builder()
                .name(SKILL_OPTION_SPEED_COMMAND)
                .description("Podaj bonus szybkości umiejętności")
                .type(ApplicationCommandOption.Type.INTEGER.getValue())
                .minValue(0D)
                .maxValue(100D)
                .build();
    }

    private static void activateCommand(List<ApplicationCommandRequest> commands, GatewayDiscordClient client) {
        client.getRestClient().getApplicationService()
                .bulkOverwriteGlobalApplicationCommand(BotConfiguration.applicationId,  commands)
                .subscribe();
    }

    //TODO jak dodachą OAuth2 dopsisać ustawianie uprawnien dla komend
}
