package pl.damian.bodzioch.fileService;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ListService {
    public static final String RESOURCE_DIR = "/home/ubuntu/TerminusBot/resources/";
    public static final String HERO_DIR = "hero/";
    public static final String SIATKA_DIR = "siatka/";
    public static final String WARIANT_DIR = "wariant/";
    public static final String SIATKA_WARIANTU_DIR = "siatka_wariantu/";
    public static final String CALENDAR_DIR = "kalendarz/";
    public static final String JPG_FILE_EXTENSION = ".jpg";

    public List<String> updateList(String dir) {
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
}
