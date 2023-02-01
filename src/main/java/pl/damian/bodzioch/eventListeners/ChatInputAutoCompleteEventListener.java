package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.configuration.BotConfiguration;
import pl.damian.bodzioch.fileService.HeroFile;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatInputAutoCompleteEventListener implements EventListener<ChatInputAutoCompleteEvent> {
    @Override
    public Class<ChatInputAutoCompleteEvent> getEventType() {
        return ChatInputAutoCompleteEvent.class;
    }

    @Override
    public Mono<Void> processCommand(ChatInputAutoCompleteEvent event) {
        if (event.getCommandName().equals("hero")) {
            String typing = event.getFocusedOption().getValue()
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElse("");
            return event.respondWithSuggestions(getFewChoicesOfHero(typing));
        }
        return Mono.empty();
    }

    private List<ApplicationCommandOptionChoiceData> getFewChoicesOfHero(String userInput) {
        List<String> options = HeroFile.heroNames.stream()
                .filter(name -> name.contains(userInput))
                .map(String::toLowerCase)
                .toList();

        if (options.size() > 5) {
            options = options.subList(0, 4);
        }

        List<ApplicationCommandOptionChoiceData> suggestions = new ArrayList<>();

        for (String option : options) {
            suggestions.add(ApplicationCommandOptionChoiceData.builder().name(option).value(option).build());
        }
        return suggestions;
    }
}
