package pl.damian.bodzioch.fileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static pl.damian.bodzioch.fileService.FileConstans.PROJECT_PATH;
import static pl.damian.bodzioch.fileService.FileConstans.RESOURCES_PATH;

public class SiatkaFile {

    private static final String SIATKA_LIST_FILE = "/siatka_list.txt";

    public static final List<String> heroSiatkaList = getHeroSiatkaList();
    private static final Logger logger = LoggerFactory.getLogger(SiatkaFile.class);

    private static List<String> getHeroSiatkaList() {
        ArrayList<String> heroNames = new ArrayList<>();
        String filePath = PROJECT_PATH + RESOURCES_PATH + SIATKA_LIST_FILE;
        try {
            Scanner scanner = new Scanner(new File(filePath));
            scanner.useDelimiter(";");
            while (scanner.hasNext()){
                heroNames.add(scanner.next());
            }
            return heroNames;
        }catch (FileNotFoundException exception) {
            logger.error("Nie udało się otworzyć pliku " + SIATKA_LIST_FILE);
        }

        return Collections.emptyList();
    }
}
