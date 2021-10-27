package ServerPackage.ServerUtils;

import ServerPackage.ServerThread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.Buffer;
import java.util.HashMap;

public class HTTPParser {

    private static final Logger LOGGER = LogManager.getLogger(ServerThread.class);

    private InputStream inputStream;
    private String fullRequest;
    private HashMap <String, String> header;
    private StringBuffer messageBody;
    private HashMap<String, String> request;

    public HTTPParser (InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.header = new HashMap<>();
        this.messageBody = new StringBuffer();
        this.request = new HashMap<>();
        generateFullRequest();
//        LOGGER.info("Full message is : " + fullRequest);
        parseFullRequest();
    }

    //did not make this method static on purpose, since we will have different threads accessing it, and might want to add more functionality
    private void generateFullRequest () throws IOException {
        //method takes in a buffered reader, then reads all the input and returns the full string of the http reuqest
        //https://medium.com/@himalee.tailor/reading-a-http-request-29edd181a6c5 to learn how to parse the entire http request
        StringBuilder request = new StringBuilder();
        do {
            request.append((char) inputStream.read());
        } while (inputStream.available() > 0);
        fullRequest =  request.toString();
    }

    private void parseFullRequest() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(fullRequest));

        //the first line will always be the request, so we pass the first line to the generateRequest method
        generateRequest(reader.readLine());
        LOGGER.info("Received request : " + request);

        //next to process the headers, we keep sending it in line by line till we encounter an empty line
        String headerLine = reader.readLine();
        while (headerLine.length() > 0){
            generateHeader(headerLine);
            headerLine = reader.readLine();
        }

        LOGGER.info("Parsed headers, total number is : " + header.size());

        //now we get the body
        String bodyLine = reader.readLine();
        //we do the same as we did for headers, but this time with  a string instead of a hashmap
        while (bodyLine != null){
            generateBody(bodyLine);
            bodyLine = reader.readLine();
        }

        LOGGER.info("Body is : " + getBody());

    }

    private void generateRequest (String requestToBeParsed) {
        if (requestToBeParsed == null || requestToBeParsed.length() == 0){
            /**
             * TODO We have to throw an HTTP Error saying that the request is invalid
             */
        }

        String generatedRequest = requestToBeParsed;

        LOGGER.info ("Full request received is : " + generatedRequest);

        String[] brokenDownRequest = generatedRequest.split(" ");
        request.put("Type", brokenDownRequest[0]);
        request.put("Path", brokenDownRequest[1]);
        request.put("httpVersion", brokenDownRequest[2]);
    }

    private void generateHeader (String headerLine) {
        String[] headerParts = headerLine.split(":", 2);
        String headerLabel = headerParts[0];
        String headerBody = headerParts[1];

        if (headerLabel.length() == 0 || headerLabel == null){
            /**
             * TODO throw HTTP Error saying that we have gotten invalid headers
             */
        }

        header.put(headerLabel, headerBody);
    }

    private void generateBody (String bodyLine) {
        messageBody.append(bodyLine).append("\n\r");
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public String getBody() {
        return messageBody.toString();
    }

    public String getRequestType () {
        return request.get("Type");
    }

    public String getRequestPath () {
        return request.get("Path");
    }

    public String getRequestHttpVersion () {
        return request.get("httpVersion");
    }

}