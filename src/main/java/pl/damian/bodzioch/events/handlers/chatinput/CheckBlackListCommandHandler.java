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
import pl.damian.bodzioch.commands.CheckBlackListCommand;
import pl.damian.bodzioch.dao.BlackListDAO;
import pl.damian.bodzioch.dto.BlackListPlayerDTO;
import pl.damian.bodzioch.events.handlers.EventHandler;
import pl.damian.bodzioch.fileService.FileServiceImpl;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

@Service
public class CheckBlackListCommandHandler implements EventHandler<ChatInputInteractionEvent> {

    @Autowired
    Logger logger;
    @Autowired
    BlackListDAO blackListDAO;

    @Override
    public Mono<Void> handleEvent(ChatInputInteractionEvent event, String param) {
        String playerName = getPlayerName(event);
        logger.info("Handling CheckBlackList event. Player name: {}", playerName);

        Set<BlackListPlayerDTO> players = blackListDAO.getPlayersByName(playerName);
        logger.info("Players: " + players);
        if (players.isEmpty()) {
            return event.reply("Nie znaleziono gracza " + playerName + " na czarnej liście");
        }

        List<EmbedCreateSpec> embeds = players.stream()
                .map(this::mapToEmbed)
                .toList();

        try {
            String fileDir = FileServiceImpl.RESOURCE_DIR + FileServiceImpl.BLACKLIST_DIR + playerName + FileServiceImpl.JPG_FILE_EXTENSION;
            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .addAllEmbeds(embeds)
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build());
        } catch (FileNotFoundException e) {
            logger.warn("Nie znaleziono zdjęcia!", e);
            return event.reply("Ups! Nie znaleziono zdjęcia!");
        }
    }

    private String getPlayerName(ChatInputInteractionEvent event) {
        return event.getOption(CheckBlackListCommand.PLAYER_NAME_OPTION)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElseThrow(() -> new IllegalStateException("Player name should be exists"));
    }

    private EmbedCreateSpec mapToEmbed(BlackListPlayerDTO player) {
        return EmbedCreateSpec.builder()
                .color(Color.GREEN)
                .addField("Nazwa gracza", player.getName(), true)
                .addField("Poziom gracza", player.getLvl(), true)
                .addField("Powód", player.getReason(), false)
                .addField("Zgłaszjący", player.getReportingPerson(), false)
                .build();
    }
}
