package pl.damian.bodzioch.commands;

import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;

@Component
public class CalendarCommand implements Command{
    public static final String KALENDARZ_COMMAND = "kalendarz";

    @Override
    public ApplicationCommandRequest setCommand() {
        return ApplicationCommandRequest.builder()
                .name(KALENDARZ_COMMAND)
                .description("Wyświetla aktualny kalendarz.")
                .build();
    }
}
