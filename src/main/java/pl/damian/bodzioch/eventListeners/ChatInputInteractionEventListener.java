package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.spec.InteractionPresentModalSpec;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.configuration.Commands;
import pl.damian.bodzioch.dao.HeroDAO;
import pl.damian.bodzioch.dao.HeroWariantDAO;
import pl.damian.bodzioch.dao.SiatkaDAO;
import pl.damian.bodzioch.fileService.ListService;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatInputInteractionEventListener implements EventListener<ChatInputInteractionEvent> {

    @Autowired
    Logger logger;
    @Autowired
    ListService listService;
    @Autowired
    Commands commands;
    @Autowired
    HeroDAO heroDAO;
    @Autowired
    SiatkaDAO siatkaDAO;
    @Autowired
    HeroWariantDAO heroWariantDAO;

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
            case "blacklist-add" :
                return event.presentModal(buildRespondForAddPlayerToBlackListCommand()).onErrorResume(this::handleError);
            default:
                return Mono.empty();
        }
    }

    @Override
    public Mono<Void> handleError(Throwable error) {
        logger.info("Error during process ChatInputInteractionEvent", error);
        return Mono.empty();
    }

    private InteractionApplicationCommandCallbackSpec buildRespondForHeroCommand(ChatInputInteractionEvent event) {
        logger.info("Handling HeroCommand event");
        String heroName = event.getOption(commands.BOHATER_HERO_COMMAND_OPTION)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString).orElse("");
        if (heroDAO.getHeroByName(heroName).isEmpty()) {
            return InteractionApplicationCommandCallbackSpec.builder()
                    .content("Nie znaleziono bohatera o nazwie: " + heroName)
                    .build();
        }

        InteractionApplicationCommandCallbackSpec response;
        try {
            String fileDir = listService.RESOURCE_DIR + listService.HERO_DIR + heroName + listService.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            logger.warn("File not found", e);
            return InteractionApplicationCommandCallbackSpec.builder().content("Coś poszło nie tak.").build();
        }
        List<Button> buttonsList = new ArrayList<>();
        if (isHaveSiatkaForHero(heroName)) {
            buttonsList.add(Button.primary(ButtonInteractionEventListener.SIATKA_TYPE + heroName, "Pokaż siatkę"));
        }
        if (isHeroHaveWariant(heroName)) {
            buttonsList.add(Button.primary(ButtonInteractionEventListener.WARIANT_TYPE + heroName, "Pokaż wariant bohatera"));
        }
        if (buttonsList.isEmpty()){
            return response;
        }
        return response.withComponents(ActionRow.of(buttonsList));
    }

    private boolean isHaveSiatkaForHero(String heroName) {
        return siatkaDAO.getSiatkaByHeroName(heroName).isPresent();
    }

    private boolean isHeroHaveWariant(String heroName) {
        return heroWariantDAO.getHeroWariantByName(heroName).isPresent();
    }

    private InteractionApplicationCommandCallbackSpec buildRespondForKalendarzCommand() {
        logger.info("Handling KalendarzCommand event");
        InteractionApplicationCommandCallbackSpec response;
        try {
            String fileDir = listService.RESOURCE_DIR + listService.CALENDAR_DIR + "kalendarz" + listService.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            logger.warn("File not found", e);
            return InteractionApplicationCommandCallbackSpec.builder().content("Coś poszło nie tak.").build();
        }
        return response;
    }

    private InteractionApplicationCommandCallbackSpec buildRespondForSpeedCommand(ChatInputInteractionEvent event) {
        logger.info("Handling SpeedCommand event");
        long base = event.getOption(commands.BASE_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long gun = event.getOption(commands.GUN_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long siatka = event.getOption(commands.SIATKA_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long family = event.getOption(commands.FAMILY_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long skill = event.getOption(commands.SKILL_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
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

    private InteractionPresentModalSpec buildRespondForAddPlayerToBlackListCommand() {

        ActionRow playerName = ActionRow.of(TextInput.small(commands.BLACKLIST_FORM_PLAYER_INPUT, "Nazwa gracza", "Podaj nazwę gracza").required(true));
        ActionRow reason = ActionRow.of(TextInput.paragraph(commands.BLACKLIST_FORM_REASON_INPUT, "Powód dodania", "Podaj powód").required(true));
        ActionRow reportingPerson = ActionRow.of(TextInput.small(commands.BLACKLIST_FORM_REPORTING_PERSON_INPUT, "Nazwa osoby/sojuszu zgłaszającego gracza", "Podaj nazwę osoby/sojuszu").required(false));
        ActionRow level = ActionRow.of(TextInput.small(commands.BLACKLIST_FORM_PLAYER_LEVEL_INPUT, "Poziom gracza", "Podaj poziom gracza").required(false));
        ActionRow otherNames = ActionRow.of(TextInput.paragraph(commands.BLACKLIST_FORM_OTHER_NAMES_INPUT, "Podaj inne nicki gracza - jeśli znane", "Podaj nicki gracza").required(false));

        List<LayoutComponent> formFields = List.of(playerName, reason, reportingPerson, level, otherNames);

        return InteractionPresentModalSpec.builder()
                .title("Dodawanie gracza do czarnej listy")
                .customId(commands.BLACKLIST_FORM_MODAL)
                .addAllComponents(formFields)
                .build();
    }
}
