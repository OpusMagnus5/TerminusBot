package pl.damian.bodzioch.fileService;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.damian.bodzioch.eventListeners.ListenersConstans;
import pl.damian.bodzioch.ftp.FtpClient;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HeroList {
    public static Set<String> HERO_NAMES;

    @Autowired
    FtpClient ftpClient;

    public void updateHeroList() {
        ftpClient.open();
        FTPFile[] ftpFiles = new FTPFile[0];

        try {
            ftpFiles = ftpClient.client.listFiles(ftpClient.HERO_DIR);
        } catch (IOException e) {
            ftpClient.close();
        }

        HERO_NAMES = Arrays.stream(ftpFiles)
                .map(FTPFile::getName)
                .filter(name -> !name.startsWith(".") || !name.endsWith("jpg"))
                .map(name -> name.replace(ListenersConstans.JPG_FILE_EXTENSION, ""))
                .collect(Collectors.toSet());

        ftpClient.close();
    }
}
