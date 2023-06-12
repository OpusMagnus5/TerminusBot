package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.configuration.Commands;
import pl.damian.bodzioch.dao.HeroDAO;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatInputAutoCompleteEventListener implements EventListener<ChatInputAutoCompleteEvent> {

    @Autowired
    Commands commands;
    @Autowired
    HeroDAO heroDAO;

    @Override
    public Class<ChatInputAutoCompleteEvent> getEventType() {
        return ChatInputAutoCompleteEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ChatInputAutoCompleteEvent event) {
        if (event.getCommandName().equals(commands.HERO_COMMAND)) {
            String typing = event.getFocusedOption().getValue()
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElse("");
            return event.respondWithSuggestions(getFewChoicesOfHero(typing)).onErrorResume(this::handleError);
        }
        return Mono.empty();
    }

    @Override
    public Mono<Void> handleError(Throwable error) {
        return Mono.empty();
    }

    private List<ApplicationCommandOptionChoiceData> getFewChoicesOfHero(String userInput) {
        List<String> options = heroDAO.getFiveHeroByPattern(userInput);
        List<ApplicationCommandOptionChoiceData> suggestions = new ArrayList<>();
        for (String option : options) {
            suggestions.add(ApplicationCommandOptionChoiceData.builder().name(option).value(option).build());
        }
        return suggestions;
    }
}
