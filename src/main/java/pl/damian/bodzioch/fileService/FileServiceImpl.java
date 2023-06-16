package pl.damian.bodzioch.fileService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.dao.BlackListDAO;
import pl.damian.bodzioch.dao.database.DataInLists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Lazy(value = false)
@Service
public class FileServiceImpl implements FileService {

    public static final String RESOURCE_DIR = "/home/ubuntu/TerminusBot/resources/";
    public static final String HERO_DIR = "hero/";
    public static final String SIATKA_DIR = "siatka/";
    public static final String WARIANT_DIR = "wariant/";
    public static final String SIATKA_WARIANTU_DIR = "siatka_wariantu/";
    public static final String CALENDAR_DIR = "kalendarz/";
    public static final String BLACKLIST_DIR = "black_list/";
    public static final String JPG_FILE_EXTENSION = ".jpg";

    @Autowired
    BlackListDAO blackListDAO;
    @Autowired
    Logger logger;

    public void updateAllLists() {
        DataInLists.HERO_NAMES = updateList(HERO_DIR);
        DataInLists.HERO_SIATKA_LIST = updateList(SIATKA_DIR);
        DataInLists.HERO_WARIANT_LIST = updateList(WARIANT_DIR);
        DataInLists.SIATKI_WARIANTOW_LIST = updateList(SIATKA_WARIANTU_DIR);
        try {
            DataInLists.BLACKLIST = blackListDAO.getAllBlackList();
        } catch (IOException e) {
            logger.error("Error during update BlackList", e);
        }
    }

    private List<String> updateList(String dir) {
        try (Stream<Path> stream = Files.list(Paths.get(RESOURCE_DIR + dir))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(name -> name.replace(JPG_FILE_EXTENSION, ""))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

/*    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledUpdateAllLists(){
        updateAllLists();
    }*/
}
