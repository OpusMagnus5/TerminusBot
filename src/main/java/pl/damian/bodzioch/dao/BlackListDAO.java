package pl.damian.bodzioch.dao;

import pl.damian.bodzioch.dto.BlackListPlayerDTO;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface BlackListDAO {

    void savePlayer(String data) throws IOException;
    Set<BlackListPlayerDTO> getPlayersByName(String playerName);
    List<BlackListPlayerDTO> getAllBlackList() throws IOException;
}
