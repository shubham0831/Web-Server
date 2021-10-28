package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;

import java.io.IOException;

public class ReviewSearchHandler implements Handler{
    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            ResponseGenerator responseGenerator = new ResponseGenerator();
            String response = responseGenerator.generateResponse("IN REVIEW SEARCH");

            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}