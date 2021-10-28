package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;

import java.io.IOException;

public class FindHandler implements Handler{

    public FindHandler (){
    }

    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            ResponseGenerator responseGenerator = new ResponseGenerator();
            String response = responseGenerator.generateGETResponse("Find", "/find", "Enter ASIN");
            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
