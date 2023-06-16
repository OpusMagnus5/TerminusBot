package pl.damian.bodzioch.dao;

import java.io.IOException;

public interface AttachmentDAO {
    void saveBlackListAttachment(String url, String photoId) throws IOException;
    void saveHeroAttachment(String url, String photoId) throws IOException;
    void saveSiatkaAttachment(String url, String photoId) throws IOException;
    void saveWariantAttachment(String url, String photoId) throws IOException;
    void saveSiatkaWariantAttachment(String url, String photoId) throws IOException;
}
