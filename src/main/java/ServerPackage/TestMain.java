package ServerPackage;

import ServerPackage.Handlers.FindHandler;
import ServerPackage.Handlers.HomePageHandler;
import ServerPackage.Handlers.ReviewSearchHandler;
import ServerPackage.Handlers.SlackBotHandler;
import ServerPackage.InvertedIndex.ProjectUI;
import ServerPackage.InvertedIndex.QAList;
import ServerPackage.InvertedIndex.ReviewList;
import ServerPackage.Servers.InvertedIndexServer;
import ServerPackage.Servers.Server;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class TestMain {

    private static final Logger LOGGER = LogManager.getLogger(TestMain.class);

    public static void main (String[] args){

        BasicConfigurator.configure();
        int invertedIndexPort = 8080;
        int slackBotPort = 9090;

        LOGGER.info("Inverted Index Server Starting at port : " + invertedIndexPort);
        LOGGER.info("Slack Bot Server Starting at port : " + slackBotPort);

        Thread slackBotStartThread = new Thread(() -> {
            Server server = null;
            try {
                server = new Server(slackBotPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.addMapping("/", new HomePageHandler());
            server.addMapping("/slackbot", new SlackBotHandler());
            server.start();
        });

        Thread invertedIndexStartThread = new Thread(() -> {
            InvertedIndexServer invertedIndexServer = null;
            try {
                invertedIndexServer = new InvertedIndexServer(invertedIndexPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            invertedIndexServer.addMapping("/", new HomePageHandler());
            invertedIndexServer.addMapping("/find", new FindHandler());
            invertedIndexServer.addMapping("/reviewsearch", new ReviewSearchHandler());

            invertedIndexServer.start();
        });

        slackBotStartThread.start();
        invertedIndexStartThread.start();


    }
}
