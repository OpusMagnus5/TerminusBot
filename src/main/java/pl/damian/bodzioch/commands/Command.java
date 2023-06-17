package pl.damian.bodzioch.commands;

import discord4j.discordjson.json.ApplicationCommandRequest;

public interface Command {
    ApplicationCommandRequest setCommand();
}
