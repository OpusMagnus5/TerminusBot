package pl.damian.bodzioch.commands;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpeedCommand {

    public static final String SPEED_COMMAND = "speed";
    public static final String BASE_OPTION_SPEED_COMMAND = "baza";
    public static final String SIATKA_OPTION_SPEED_COMMAND = "siatka";
    public static final String GUN_OPTION_SPEED_COMMAND = "bron";
    public static final String FAMILY_OPTION_SPEED_COMMAND = "rodzina";
    public static final String SKILL_OPTION_SPEED_COMMAND = "skill";

    public ApplicationCommandRequest setSpeedCommand() {
        return ApplicationCommandRequest.builder()
                .name(SPEED_COMMAND)
                .description("Sprawdza czy uda się przełamać szybkość bohatera")
                .addAllOptions(List.of(buildBaseOptionSpeedCommand(), buildSiatkaBonusOptionSpeedCommand(),
                        buildGunBonusOptionSpeedCommand(), buildFamilyBonusOptionSpeedCommand(), buildSkillBonusOptionSpeedCommand()))
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
}
