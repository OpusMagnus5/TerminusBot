package pl.damian.bodzioch;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChatInputInteractionEventListener implements EventListener<ChatInputInteractionEvent>{

    @Override
    public Class<ChatInputInteractionEvent> getEventType() {
        return ChatInputInteractionEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ChatInputInteractionEvent event) {
        if (event.getCommandName().equals("hero")) {
            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .addEmbed(EmbedCreateSpec.builder()
                            .image("https://combatpolska.prv.pl/hero/" +
                                    event.getOption("bohater")
                                            .flatMap(ApplicationCommandInteractionOption::getValue)
                                            .map(ApplicationCommandInteractionOptionValue::asString).get() + ".jpg")
                            .build())
                    .build());
        }
        return Mono.empty();
    }


}
