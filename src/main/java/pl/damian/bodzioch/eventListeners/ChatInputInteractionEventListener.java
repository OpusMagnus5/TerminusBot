package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.configuration.Commands;
import pl.damian.bodzioch.fileService.DataInLists;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatInputInteractionEventListener implements EventListener<ChatInputInteractionEvent> {

    @Override
    public Class<ChatInputInteractionEvent> getEventType() {
        return ChatInputInteractionEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ChatInputInteractionEvent event) {
        String eventName = event.getCommandName();
        switch (eventName){
            case "hero" :
                return event.reply(buildRespondForHeroCommand(event)).onErrorResume(this::handleError);
            case "kalendarz" :
                return event.reply(buildRespondForKalendarzCommand()).onErrorResume(this::handleError);
            case "speed" :
                return event.reply(buildRespondForSpeedCommand(event)).onErrorResume(this::handleError);
            default:
                return Mono.empty();
        }
    }

    @Override
    public Mono<Void> handleError(Throwable error) {
        return Mono.empty();
    }

    private InteractionApplicationCommandCallbackSpec buildRespondForHeroCommand(ChatInputInteractionEvent event) {
        String heroName = event.getOption(Commands.BOHATER_HERO_COMMAND_OPTION)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString).orElse("");
        if (!DataInLists.HERO_NAMES.contains(heroName)) {
            return InteractionApplicationCommandCallbackSpec.builder()
                    .content("Nie znaleziono bohatera o nazwie: " + heroName)
                    .build();
        }

        InteractionApplicationCommandCallbackSpec response = InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .image(ListenersConstans.PHOTOS_URL + ListenersConstans.HERO_PATH +
                                heroName + ListenersConstans.JPG_FILE_EXTENSION)
                        .build())
                .build();
        List<Button> buttonsList = new ArrayList<>();
        if (isHaveSiatkaForHero(heroName)) {
            buttonsList.add(Button.primary(ButtonInteractionEventListener.SIATKA_TYPE + heroName, "Pokaż siatkę"));
        }
        if (isHeroHaveWariant(heroName)) {
            buttonsList.add(Button.primary(ButtonInteractionEventListener.WARIANT_TYPE + heroName, "Pokaż wariant bohatera"));
        }
        return response.withComponents(ActionRow.of(buttonsList));
    }

    private boolean isHaveSiatkaForHero(String heroName) {
        return DataInLists.HERO_SIATKA_LIST.contains(heroName);
    }

    private boolean isHeroHaveWariant(String heroName) {
        return DataInLists.HERO_WARIANT_LIST.contains(heroName);
    }

    private InteractionApplicationCommandCallbackSpec buildRespondForKalendarzCommand() {
        return InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .image(ListenersConstans.PHOTOS_URL + ListenersConstans.KALENDARZ_PATH
                                + Commands.KALENDARZ_COMMAND + ListenersConstans.JPG_FILE_EXTENSION)
                        .build())
                .build();
    }

    private InteractionApplicationCommandCallbackSpec buildRespondForSpeedCommand(ChatInputInteractionEvent event) {
        long base = event.getOption(Commands.BASE_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long gun = event.getOption(Commands.GUN_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long siatka = event.getOption(Commands.SIATKA_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long family = event.getOption(Commands.FAMILY_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long skill = event.getOption(Commands.SKILL_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);

        return InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .color(Color.GREEN)
                        .addField("Bazowa prędkość", String.valueOf(base), false)
                        .addField("Prędkość broni", String.valueOf(gun), false)
                        .addField("Bonus z siatki", siatka + "%", false)
                        .addField("Bonus rodziny", family + "%", false)
                        .addField("Bonus z umiejętności", skill + "%", false)
                        .addField("Liczba klocków bez bonusów", calculateSpeed(base, 0L, 0L, 0L, 0L), false)
                        .addField("Liczba klocków z bonusami", calculateSpeed(base, gun, siatka, family, skill), false)
                        .build())
                .build();
    }

    private String calculateSpeed(long base, long gun, long siatka, long family, long skill){
        return String.valueOf((int) Math.ceil((((78 - (base + gun)) * 0.14D) + 6.08D) / (1 + ((siatka + family + skill) / 100D))));
    }
}
