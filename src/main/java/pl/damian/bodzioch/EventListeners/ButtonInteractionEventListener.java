package pl.damian.bodzioch.EventListeners;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ComponentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ButtonInteractionEventListener implements EventListener<ButtonInteractionEvent>{

    @Override
    public Class<ButtonInteractionEvent> getEventType() {
        return ButtonInteractionEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ButtonInteractionEvent event) {
        String buttonId = event.getInteraction().getData().data().get().customId().get();
        String buttonType = getButtonType(buttonId);
        switch (buttonType){
            case "siatka":
                return event.reply(buildSiatkaButtonResponse(buttonId.replace("siatka", "")));
            default:
                return Mono.empty();
        }
    }

    private InteractionApplicationCommandCallbackSpec buildSiatkaButtonResponse(String heroName) {
        return InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .image("https://combatpolska.prv.pl/" + "siatka/" + heroName + ".jpg")
                        .build())
                .build();
    }
    
    private String getButtonType(String buttonName) {
        if (buttonName.contains("siatka")){
            return "siatka";
        }
        return "";
    }
}
