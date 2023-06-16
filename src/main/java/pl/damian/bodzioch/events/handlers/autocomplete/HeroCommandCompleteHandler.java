package pl.damian.bodzioch.events.handlers.autocomplete;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.dao.HeroDAO;
import pl.damian.bodzioch.events.handlers.EventHandler;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class HeroCommandCompleteHandler implements EventHandler<ChatInputAutoCompleteEvent> {

    @Autowired
    HeroDAO heroDAO;

    @Override
    public Mono<Void> handleEvent(ChatInputAutoCompleteEvent event, String param) {
        String userInput = event.getFocusedOption().getValue()
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");
        List<String> options = heroDAO.getFiveHeroByPattern(userInput);
        List<ApplicationCommandOptionChoiceData> suggestions = new ArrayList<>();
        for (String option : options) {
            suggestions.add(ApplicationCommandOptionChoiceData.builder().name(option).value(option).build());
        }
        return event.respondWithSuggestions(suggestions);
    }
}
