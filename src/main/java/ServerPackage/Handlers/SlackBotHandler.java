package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class SlackBotHandler implements Handler{
    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class);

    public SlackBotHandler () {

    }
    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            ResponseGenerator responseGenerator = new ResponseGenerator();
            String response = responseGenerator.generateGETResponse("ChatBot", "/slackbot", "Enter Message");

            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
