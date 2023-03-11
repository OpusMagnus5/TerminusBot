package pl.damian.bodzioch.eventListeners;

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.configuration.Commands;
import pl.damian.bodzioch.fileService.DataInLists;
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
        if (event.getCommandName().equals(Commands.HERO_COMMAND)) {
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
        List<String> options = DataInLists.HERO_NAMES.stream()
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
