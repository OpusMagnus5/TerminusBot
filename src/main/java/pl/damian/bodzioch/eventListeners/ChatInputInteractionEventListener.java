package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.configuration.BotConfiguration;
import pl.damian.bodzioch.fileService.SiatkaFile;
import reactor.core.publisher.Mono;

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
                return event.reply(buildRespondForHeroCommand(event));
            default:
                return Mono.empty();
        }
    }

    private InteractionApplicationCommandCallbackSpec buildRespondForHeroCommand(ChatInputInteractionEvent event) {
        String heroName = event.getOption("bohater")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString).orElse("");


        InteractionApplicationCommandCallbackSpec response = InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .image("https://combatpolska.prv.pl/" + "hero/" +
                                heroName + ".jpg")
                        .build())
                .build();
        if (isHaveSiatkaForHero(heroName)) {
            return response.withComponents(ActionRow.of(Button.primary("siatka" + heroName, "Pokaż siatkę")));
        }
        return response;
    }

    private boolean isHaveSiatkaForHero(String heroName) {
        return SiatkaFile.heroSiatkaList.contains(heroName);
    }
}
