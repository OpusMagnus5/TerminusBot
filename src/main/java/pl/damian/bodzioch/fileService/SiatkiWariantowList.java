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
public class SiatkiWariantowList {

    public static Set<String> SIATKI_WARIANTOW_LIST;
    @Autowired
    FtpClient ftpClient;

    public void updateSiatkiWariantowList() {
        ftpClient.open();
        FTPFile[] ftpFiles = new FTPFile[0];

        try {
            ftpFiles = ftpClient.client.listFiles(ftpClient.SIATKI_WARIANTOW_DIR);
        } catch (IOException e) {
            ftpClient.close();
        }

        SIATKI_WARIANTOW_LIST = Arrays.stream(ftpFiles)
                .map(FTPFile::getName)
                .filter(name -> !name.startsWith(".") || !name.endsWith("jpg"))
                .collect(Collectors.toSet());

        ftpClient.close();
    }
}
