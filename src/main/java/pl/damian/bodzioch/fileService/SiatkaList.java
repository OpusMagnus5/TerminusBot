package pl.damian.bodzioch.fileService;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.damian.bodzioch.ftp.FtpClient;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SiatkaList {

    public static Set<String> HERO_SIATKA_LIST;

    @Autowired
    FtpClient ftpClient;

    public void updateSiatkaList() {
        ftpClient.open();
        FTPFile[] ftpFiles = new FTPFile[0];

        try {
            ftpFiles = ftpClient.client.listFiles(ftpClient.SIATKA_DIR);
        } catch (IOException e) {
            ftpClient.close();
        }

        HERO_SIATKA_LIST = Arrays.stream(ftpFiles)
                .map(FTPFile::getName)
                .filter(name -> !name.startsWith(".") || !name.endsWith("jpg"))
                .collect(Collectors.toSet());

        ftpClient.close();
    }
}
