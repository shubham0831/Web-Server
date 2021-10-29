package ServerPackage.Servers;

import ServerPackage.InvertedIndex.ProjectUI;
import ServerPackage.InvertedIndex.QAList;
import ServerPackage.InvertedIndex.ReviewList;
import ServerPackage.ServerThreads.ServerThread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class InvertedIndexServer extends Server{

    private ProjectUI invertedIndex;
    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    public InvertedIndexServer(int port) throws IOException {
        super(port);

        /**
         * Initializing the inverted index, since this is a server meant for a specific purpose
         * the file paths have been hardcoded and not passed as a parameter.
         * This ensures that the server is indeed a general purpose server
         *
         * Every server (on different ports will get one inverted index of their own)
         */

        String reviewFile = "/home/shubham/IdeaProjects/project3-shubham0831/Cell_Phones_and_Accessories_5.json";
        String qaFile = "/home/shubham/IdeaProjects/project3-shubham0831/qa_Cell_Phones_and_Accessories.json";

        ReviewList reviewList = new ReviewList("ISO-8859-1"); //creating ReviewList
        QAList qaList = new QAList("ISO-8859-1"); //creating QAList
        this.invertedIndex = new ProjectUI(reviewList, reviewFile, qaList, qaFile);
        LOGGER.info("Inverted index is initialized and server is up and ready...");
    }

    @Override
    public void run() {
        try {
            while (running) {
                Socket listenerSocket = server.accept();
                LOGGER.info("Connection accepted : " + listenerSocket.getInetAddress());
                ServerThread serverThread = new ServerThread(listenerSocket, map);
                serverThread.setInvertedIndex(invertedIndex);
                serverThread.start();
            }
        } catch (IOException e) {
            LOGGER.error("Error in setting up the socket : \n" + e);
        }finally {
            try {
                server.close();
            } catch (IOException e) {
                LOGGER.error("Error in closing server socket : \n" + e);
                e.printStackTrace();
            }
        }
    }
}
