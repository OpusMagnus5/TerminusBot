package pl.damian.bodzioch.fileService;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.damian.bodzioch.dao.DataInLists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Lazy(value = false)
@Component
public class ListServiceImpl implements ListService{

    public void updateAllLists() {
        DataInLists.HERO_NAMES = updateList(HERO_DIR);
        DataInLists.HERO_SIATKA_LIST = updateList(SIATKA_DIR);
        DataInLists.HERO_WARIANT_LIST = updateList(WARIANT_DIR);
        DataInLists.SIATKI_WARIANTOW_LIST = updateList(SIATKA_WARIANTU_DIR);
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

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledUpdateAllLists(){
        updateAllLists();
    }
}
