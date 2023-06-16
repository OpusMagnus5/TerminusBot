package pl.damian.bodzioch.dao;

import pl.damian.bodzioch.dto.BlackListPlayerDTO;

import java.io.IOException;
import java.util.Set;

public interface BlackListDAO {

    void savePlayer(String data) throws IOException;

    Set<BlackListPlayerDTO> getPlayersByName(String playerName) throws IOException;
}
