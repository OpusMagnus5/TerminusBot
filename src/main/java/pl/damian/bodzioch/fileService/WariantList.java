package pl.damian.bodzioch.fileService;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.damian.bodzioch.ftp.FtpClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WariantList {
    public static Set<String> HERO_WARIANT_LIST;

    @Autowired
    FtpClient ftpClient;

    public void updateWariantList() {
        ftpClient.open();
        FTPFile[] ftpFiles = new FTPFile[0];

        try {
            ftpFiles = ftpClient.client.listFiles(ftpClient.WARIANT_DIR);
        } catch (IOException e) {
            ftpClient.close();
        }

        HERO_WARIANT_LIST = Arrays.stream(ftpFiles)
                .map(FTPFile::getName)
                .filter(name -> !name.startsWith(".") || !name.endsWith("jpg"))
                .collect(Collectors.toSet());

        ftpClient.close();
    }
}
