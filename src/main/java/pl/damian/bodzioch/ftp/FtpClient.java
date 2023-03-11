package pl.damian.bodzioch.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FtpClient {
    private final String URL = "combatpolska.prv.pl";
    private final int PORT = 21;
    private final String USER_NAME = "combatpolska@prv.pl";
    private final String PASSWORD = "!4RBMP9GzTbSa9q";
    public final String SIATKA_DIR = "/siatka/";
    public final String HERO_DIR = "/hero/";
    public final String WARIANT_DIR = "/warianty/";
    public final String SIATKI_WARIANTOW_DIR = "/siatki_wariantow/";
    public FTPClient client;

    public void open() {
        this.client = new FTPClient();
        try {
            this.client.connect(this.URL, this.PORT);
            this.client.login(this.USER_NAME, this.PASSWORD);
        } catch (IOException e) {
            System.out.println("Exception in connecting to FTP Server");
            close();
        }
    }

    public void close() {
        try {
            client.disconnect();
        } catch (IOException e) {
            System.out.println("Exception in disconnecting FTP Server");
        }

    }
}
