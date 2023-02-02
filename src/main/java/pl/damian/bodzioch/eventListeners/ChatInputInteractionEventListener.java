package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.configuration.Commands;
import pl.damian.bodzioch.fileService.FileConstans;
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
                return event.reply(buildRespondForHeroCommand(event)).onErrorResume(this::handleError);
            default:
                return Mono.empty();
        }
    }

    @Override
    public Mono<Void> handleError(Throwable error) {
        return Mono.empty();
    }

    private InteractionApplicationCommandCallbackSpec buildRespondForHeroCommand(ChatInputInteractionEvent event) {
        String heroName = event.getOption(Commands.BOHATER_COMMAND_OPTION)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString).orElse("");


        InteractionApplicationCommandCallbackSpec response = InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .image(ListenersConstans.PHOTOS_URL + ListenersConstans.HERO_PATH +
                                heroName + FileConstans.JPG_FILE_EXTENSION)
                        .build())
                .build();
        if (isHaveSiatkaForHero(heroName)) {
            return response.withComponents(ActionRow.of(
                    Button.primary(ButtonInteractionEventListener.SIATKA_TYPE + heroName, "Pokaż siatkę")));
        }
        return response;
    }

    private boolean isHaveSiatkaForHero(String heroName) {
        return SiatkaFile.heroSiatkaList.contains(heroName);
    }
}
