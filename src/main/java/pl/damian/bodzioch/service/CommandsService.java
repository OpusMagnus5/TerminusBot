package pl.damian.bodzioch.service;

import discord4j.core.GatewayDiscordClient;
import discord4j.discordjson.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.commands.*;
import pl.damian.bodzioch.configuration.BotConfiguration;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommandsService {

    public static final String BLACKLIST_FORM_PLAYER_INPUT = "blackListFormPlayerInput";
    public static final String BLACKLIST_FORM_REASON_INPUT = "blackListFormReasonInput";
    public static final String BLACKLIST_FORM_PLAYER_LEVEL_INPUT = "blackListFormPlayerLevelInput";
    public static final String BLACKLIST_FORM_REPORTING_PERSON_INPUT = "blackListFormReportingPersonInput";

    @Autowired
    HeroCommand heroCommand;
    @Autowired
    CalendarCommand calendarCommand;
    @Autowired
    SpeedCommand speedCommand;
    @Autowired
    AddToBlackListCommand addToBlackListCommand;
    @Autowired
    CheckBlackListCommand checkBlackListCommand;
    @Autowired
    HeroAddCommand heroAddCommand;
    @Autowired
    SiatkaAddCommand siatkaAddCommand;
    @Autowired
    WariantAddCommand wariantAddCommand;
    @Autowired
    SiatkaWariantuAddCommand siatkaWariantuAddCommand;

    public void setAllCommands(GatewayDiscordClient client) {
        List<ApplicationCommandRequest> commands = new ArrayList<>(List.of(heroCommand.setHeroCommand(), calendarCommand.setKalendarzCommand(),
                speedCommand.setSpeedCommand(), addToBlackListCommand.setAddPlayerToBlackList(), checkBlackListCommand.setCheckBlackListCommand(),
                heroAddCommand.setHeroAddCommand(), siatkaAddCommand.setSiatkaAddCommand(), wariantAddCommand.setWariantAddCommand(),
                siatkaWariantuAddCommand.setSiatkaWariantuAddCommand()));

        activateCommand(commands, client);
        /*activateTestCommand(commands,client);*/
    }

    private void activateCommand(List<ApplicationCommandRequest> commands, GatewayDiscordClient client) {
        client.getRestClient().getApplicationService()
                .bulkOverwriteGlobalApplicationCommand(BotConfiguration.applicationId,  commands)
                .subscribe();
    }

    private void activateTestCommand(List<ApplicationCommandRequest> commands, GatewayDiscordClient client) {
        client.getRestClient().getApplicationService()
                .bulkOverwriteGuildApplicationCommand(BotConfiguration.applicationId, 1084093399453421681L,  commands)
                .subscribe();
    }

    //TODO jak dodachą OAuth2 dopsisać ustawianie uprawnien dla komend
}
