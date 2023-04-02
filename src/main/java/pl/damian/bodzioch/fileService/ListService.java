package pl.damian.bodzioch.fileService;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Lazy(value = false)
@Component
public class ListService {
    public static final String RESOURCE_DIR = "/home/ubuntu/TerminusBot/resources/";
    public static final String HERO_DIR = "hero/";
    public static final String SIATKA_DIR = "siatka/";
    public static final String WARIANT_DIR = "wariant/";
    public static final String SIATKA_WARIANTU_DIR = "siatka_wariantu/";
    public static final String CALENDAR_DIR = "kalendarz/";
    public static final String JPG_FILE_EXTENSION = ".jpg";

    public void updateAllLists() {
        DataInLists.HERO_NAMES = ListService.updateList(ListService.HERO_DIR);
        DataInLists.HERO_SIATKA_LIST = ListService.updateList(ListService.SIATKA_DIR);
        DataInLists.HERO_WARIANT_LIST = ListService.updateList(ListService.WARIANT_DIR);
        DataInLists.SIATKI_WARIANTOW_LIST = ListService.updateList(ListService.SIATKA_WARIANTU_DIR);
    }

    private static List<String> updateList(String dir) {
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
