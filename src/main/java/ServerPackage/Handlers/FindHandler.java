package ServerPackage.Handlers;

import ServerPackage.HttpUtils.HttpConstants;
import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.InvertedIndex.ProjectUI;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class FindHandler implements Handler{

    private ProjectUI invertedIndex;
    private static final Logger LOGGER = LogManager.getLogger(FindHandler.class);

    public FindHandler (){
    }

    //method to initialize the invertedIndex in the Handler
    public void initializeIndex (ProjectUI invertedIndex){
        this.invertedIndex = invertedIndex;
    }

    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            String httpMethod = req.getRequestType();
            LOGGER.info("Find handler received http method : " + httpMethod);
            if (httpMethod.equals(HttpConstants.GET)){
                ResponseGenerator responseGenerator = new ResponseGenerator();
                String response = responseGenerator.generateGETResponse("Find", "/find", "Enter ASIN");
                res.writeResponse(response);
            } else if (httpMethod.equals(HttpConstants.POST)){
                String body = req.cleanBody(req.getBody());
                ArrayList<String> results = invertedIndex.getAsin(body);
                LOGGER.info("Got ASIN " + body + " found " + results.size() + " results");

                ResponseGenerator responseGenerator = new ResponseGenerator();
                String response = responseGenerator.generateInvertedIndexResponse("Find", "/find", "Enter ASIN ", results);

                res.writeResponse(response);
            } else {
                /**
                 * If method is neither a GET or a POST, we send HttpError 405 Method Not Supported
                 */
                ResponseGenerator responseGenerator = new ResponseGenerator();
                String response = responseGenerator.generateMETHODNOTALLOWEDResponse();
                res.writeResponse(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
