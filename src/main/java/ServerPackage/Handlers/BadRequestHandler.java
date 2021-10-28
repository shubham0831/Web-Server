package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;

import java.io.IOException;

public class BadRequestHandler implements Handler{
    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            ResponseGenerator responseGenerator = new ResponseGenerator();
            String response = responseGenerator.generateBADREQUESTResponse();

            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
