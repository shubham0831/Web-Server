package ServerPackage;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class TestMain {

    private static final Logger LOGGER = LogManager.getLogger(TestMain.class);

    public static void main (String[] args){
        BasicConfigurator.configure();
        LOGGER.info("Started server ...");
        int port = 8080;
        LOGGER.info("Using port : " + port);
        try {
            Server s = new Server(8080);
            s.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
