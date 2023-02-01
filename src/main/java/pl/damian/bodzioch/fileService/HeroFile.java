package pl.damian.bodzioch.fileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.damian.bodzioch.configuration.BotConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static pl.damian.bodzioch.fileService.FileConstans.PROJECT_PATH;
import static pl.damian.bodzioch.fileService.FileConstans.RESOURCES_PATH;

public class HeroFile {
    private static final String HERO_NAMES_FILE = "/hero_names.txt";
    public static final List<String> heroNames = getHeroNames();
    private static final Logger logger = LoggerFactory.getLogger(HeroFile.class);

    private static List<String> getHeroNames(){
        ArrayList<String> heroNames = new ArrayList<>();
        String filePath = PROJECT_PATH + RESOURCES_PATH + HERO_NAMES_FILE;
        try {
            Scanner scanner = new Scanner(new File(filePath));
            scanner.useDelimiter(";");
            while (scanner.hasNext()){
                heroNames.add(scanner.next());
            }
            heroNames.sort(String::compareTo);
            return heroNames;
        }catch (FileNotFoundException exception) {
            logger.error("Nie udało się otworzyć pliku " + HERO_NAMES_FILE);
        }

        return Collections.emptyList();
    }
}
