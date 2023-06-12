package pl.damian.bodzioch.fileService;

public interface ListService {
    String RESOURCE_DIR = "/home/ubuntu/TerminusBot/resources/";
    String HERO_DIR = "hero/";
    String SIATKA_DIR = "siatka/";
    String WARIANT_DIR = "wariant/";
    String SIATKA_WARIANTU_DIR = "siatka_wariantu/";
    String CALENDAR_DIR = "kalendarz/";
    String JPG_FILE_EXTENSION = ".jpg";
    void updateAllLists();
    void scheduledUpdateAllLists();
}
