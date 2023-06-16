package pl.damian.bodzioch.dao;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;

public interface AttachmentDAO {
    void saveBlackListAttachment(ChatInputInteractionEvent event, String url, String photoId);
}
