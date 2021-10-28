package ServerPackage;

import ServerPackage.Handlers.FindHandler;
import ServerPackage.Handlers.HomePageHandler;
import ServerPackage.Handlers.ReviewSearchHandler;
import ServerPackage.Handlers.SlackBotHandler;
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
            Server server = new Server(8080);
            server.addMapping("/", new HomePageHandler());
            server.addMapping("/find", new FindHandler());
            server.addMapping("/reviewsearch", new ReviewSearchHandler());
            server.addMapping("/slackbot", new SlackBotHandler());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
