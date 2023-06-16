package pl.damian.bodzioch.events.handlers.chatinput;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.commands.HeroCommand;
import pl.damian.bodzioch.dao.HeroDAO;
import pl.damian.bodzioch.dao.HeroWariantDAO;
import pl.damian.bodzioch.dao.SiatkaDAO;
import pl.damian.bodzioch.events.handlers.EventHandler;
import pl.damian.bodzioch.fileService.FileServiceImpl;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeroCommandHandler implements EventHandler<ChatInputInteractionEvent> {

    public static final String SIATKA_BUTTON_ID = "siatka";
    public static final String WARIANT_BUTTON_ID = "wariant";
    public static final String BUTTON_DELIMITER = "999";

    @Autowired
    Logger logger;
    @Autowired
    HeroDAO heroDAO;
    @Autowired
    SiatkaDAO siatkaDAO;
    @Autowired
    HeroWariantDAO heroWariantDAO;

    @Override
    public Mono<Void> handleEvent(ChatInputInteractionEvent event, String param) {
        logger.info("Handling HeroCommand event");
        String heroName = event.getOption(HeroCommand.BOHATER_HERO_COMMAND_OPTION)
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString).orElse("");
        if (heroDAO.getHeroByName(heroName).isEmpty()) {
            return event.reply("Nie znaleziono bohatera o nazwie: " + heroName);
        }

        InteractionApplicationCommandCallbackSpec response;
        try {
            String fileDir = FileServiceImpl.RESOURCE_DIR + FileServiceImpl.HERO_DIR + heroName + FileServiceImpl.JPG_FILE_EXTENSION;
            response = InteractionApplicationCommandCallbackSpec.builder()
                    .addFile(fileDir, new FileInputStream(fileDir))
                    .build();
        } catch (FileNotFoundException e) {
            logger.warn("File not found", e);
            return event.reply("Coś poszło nie tak.");
        }
        List<Button> buttonsList = new ArrayList<>();
        if (isHaveSiatkaForHero(heroName)) {
            buttonsList.add(Button.primary(SIATKA_BUTTON_ID + BUTTON_DELIMITER + heroName, "Pokaż siatkę"));
        }
        if (isHeroHaveWariant(heroName)) {
            buttonsList.add(Button.primary(WARIANT_BUTTON_ID + BUTTON_DELIMITER + heroName, "Pokaż wariant bohatera"));
        }
        if (buttonsList.isEmpty()){
            return event.reply(response);
        }
        return event.reply(response.withComponents(ActionRow.of(buttonsList)));
    }

    private boolean isHaveSiatkaForHero(String heroName) {
        return siatkaDAO.getSiatkaByHeroName(heroName).isPresent();
    }

    private boolean isHeroHaveWariant(String heroName) {
        return heroWariantDAO.getHeroWariantByName(heroName).isPresent();
    }
}
