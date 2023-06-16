package pl.damian.bodzioch.dao.Impl;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.damian.bodzioch.dao.BlackListDAO;
import pl.damian.bodzioch.dao.database.DataInLists;
import pl.damian.bodzioch.dto.BlackListPlayerDTO;
import pl.damian.bodzioch.events.handlers.modalsubmit.AddToBlackListFormHandler;
import pl.damian.bodzioch.fileService.FileServiceImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class BlackListDAOImpl implements BlackListDAO {

    public static final String BLACKLIST_FILE = "blacklist.txt";

    @Autowired
    Logger logger;

    @Override
    public void savePlayer(String data) throws IOException {
        logger.info("Rozpoczynam zapis danych do pliku. Dane: " + data);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(FileServiceImpl.RESOURCE_DIR + FileServiceImpl.BLACKLIST_DIR + BLACKLIST_FILE, true),
                        StandardCharsets.UTF_8));
        writer.append(data).append("\n");
        writer.close();
        logger.info("Zakonczono zapis danych. Zapis do listy rozpoczęty.");
        DataInLists.BLACKLIST.add(mapToBlackListDTO(data));
    }

    @Override
    public Set<BlackListPlayerDTO> getPlayersByName(String playerName) {
        return DataInLists.BLACKLIST.stream()
                .filter(player -> player.getName().equals(playerName))
                .collect(Collectors.toSet());
    }

    public List<BlackListPlayerDTO> getAllBlackList() throws IOException {
        List<BlackListPlayerDTO> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(FileServiceImpl.RESOURCE_DIR + FileServiceImpl.BLACKLIST_DIR + BLACKLIST_FILE));
        String line = reader.readLine();
        while(!StringUtils.isEmpty(line)) {
            result.add(mapToBlackListDTO(line));
            line = reader.readLine();
        }
        return result;
    }

    private BlackListPlayerDTO mapToBlackListDTO(String line) {
        String[] data = line.split(";");
        BlackListPlayerDTO player = new BlackListPlayerDTO();
        player.setName(data[0]);
        player.setReason(data[1]);
        for (int i = 2; data.length > i; i++) {
            if (data[i].startsWith(AddToBlackListFormHandler.OPTION_3)) {
                player.setReportingPerson(data[i].substring(1));
            }
            if (data[i].startsWith(AddToBlackListFormHandler.OPTION_4)) {
                player.setLvl(data[i].substring(1));
            }
        }
        logger.info("Player: " + player);
        return player;
    }

    // stare pobieranie z pliku
/*    @Override
    public Set<BlackListPlayerDTO> getPlayersByName(String playerName) throws IOException {
        List<BlackListPlayerDTO> result = getAllBlackList();

        return result.stream()
                .filter(player -> player.getName().equals(playerName))
                .collect(Collectors.toSet());
    }*/
}
