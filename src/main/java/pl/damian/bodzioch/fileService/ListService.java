package pl.damian.bodzioch.fileService;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.damian.bodzioch.ftp.FtpClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ListService {
    @Autowired
    FtpClient ftpClient;

    public Set<String> updateList(String dir) {
        ftpClient.open();
        FTPFile[] ftpFiles = new FTPFile[0];

        try {
            ftpFiles = ftpClient.client.listFiles(dir);
        } catch (IOException e) {
            ftpClient.close();
        }

        Set<String> listToAssign = Arrays.stream(ftpFiles)
                .map(FTPFile::getName)
                .filter(name -> !name.startsWith(".") || !name.endsWith("jpg"))
                .map(name -> name.replace(".jpg", ""))
                .collect(Collectors.toSet());

        ftpClient.close();

        return listToAssign;
    }
}
