package pl.damian.bodzioch.events.handlers.chatinput;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.commands.SpeedCommand;
import pl.damian.bodzioch.events.handlers.EventHandler;
import reactor.core.publisher.Mono;

@Service
public class SpeedCommandHandler implements EventHandler<ChatInputInteractionEvent> {

    @Autowired
    Logger logger;

    @Override
    public Mono<Void> handleEvent(ChatInputInteractionEvent event, String param) {
        logger.info("Handling SpeedCommand event");
        long base = event.getOption(SpeedCommand.BASE_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long gun = event.getOption(SpeedCommand.GUN_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long siatka = event.getOption(SpeedCommand.SIATKA_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long family = event.getOption(SpeedCommand.FAMILY_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        long skill = event.getOption(SpeedCommand.SKILL_OPTION_SPEED_COMMAND).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);

        return event.reply(InteractionApplicationCommandCallbackSpec.builder()
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
                .build());
    }

    private String calculateSpeed(long base, long gun, long siatka, long family, long skill){
        return String.valueOf((int) Math.ceil((((78 - (base + gun)) * 0.14D) + 6.08D) / (1 + ((siatka + family + skill) / 100D))));
    }
}
