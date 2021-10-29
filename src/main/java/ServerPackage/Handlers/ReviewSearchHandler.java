package ServerPackage.Handlers;

import ServerPackage.HttpUtils.ResponseGenerator;
import ServerPackage.InvertedIndex.ProjectUI;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ReviewSearchHandler implements Handler{

    private ProjectUI invertedIndex;
    private static final Logger LOGGER = LogManager.getLogger(ReviewSearchHandler.class);

    public ReviewSearchHandler(){

    }

    //method to initialize the invertedIndex in the Handler
    public void initializeIndex (ProjectUI invertedIndex){
        this.invertedIndex = invertedIndex;
    }

    @Override
    public void handle(HTTPParser req, HttpWriter res) {
        try {
            ResponseGenerator responseGenerator = new ResponseGenerator();
            String response = responseGenerator.generateGETResponse("Review Search", "/reviewsearch", "Enter Search Item");

            res.writeResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
