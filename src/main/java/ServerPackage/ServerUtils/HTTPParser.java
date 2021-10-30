package ServerPackage.ServerUtils;

import ServerPackage.ServerThreads.ServerThread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;

public class HTTPParser {

    private static final Logger LOGGER = LogManager.getLogger(HTTPParser.class);

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
        fullRequest = generateFullRequest(inputStream);
        parseFullRequest(fullRequest);
    }

    //did not make this method static on purpose, since we will have different threads accessing it, and might want to add more functionality
    private String generateFullRequest (InputStream inputStream) throws IOException {
        //method takes in a buffered reader, then reads all the input and returns the full string of the http request
        //https://medium.com/@himalee.tailor/reading-a-http-request-29edd181a6c5 to learn how to parse the entire http request
        StringBuilder request = new StringBuilder();
        do {
            request.append((char) inputStream.read());
        } while (inputStream.available() > 0);
        String parsedFullRequest =  request.toString();
        return parsedFullRequest;
    }

    private void parseFullRequest(String fullRequest) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(fullRequest));

        //the first line will always be the request, so we pass the first line to the generateRequest method
        /**Getting the full request**/
        request = generateRequest(reader.readLine());
        //if request is empty, then we do not execute rest of the code, and it sends and HTTP Error 400
        if (request.isEmpty()){
            return;
        }
        LOGGER.info("Received request : " + request);

        //next to process the headers, we keep sending it in line by line till we encounter an empty line
        String headerLine = reader.readLine();
        while (headerLine.length() > 0){
            //generating header
            generateHeader(headerLine, header);
            headerLine = reader.readLine();
        }

        //if header is empty, we do not execute rest of the code, and generate HTTP Error 400
        if (header.isEmpty()){
            /**
             * Incomplete header, will respond back with HTTP Error 400
             */
            return;
        }

        LOGGER.info("Parsed headers, total number is : " + header.size());

        //now we get the body
        String bodyLine = reader.readLine();
        //we do the same as we did for headers, but this time with  a string instead of a hashmap
        while (bodyLine != null){
            messageBody = generateBody(bodyLine, messageBody);
            bodyLine = reader.readLine();
        }

        LOGGER.info("Body is : " + getBody());

        return;
    }

    private HashMap<String, String> generateRequest (String requestToBeParsed) {
        if (requestToBeParsed == null || requestToBeParsed.length() == 0){
            /**
             * Since requestToBeParsed is empty, we just return
             */
            return new HashMap<>();
        }

        String generatedRequest = requestToBeParsed;

        LOGGER.info ("Full request received is : " + generatedRequest);

        String[] brokenDownRequest = generatedRequest.split(" ");
        HashMap<String, String> parsedRequest = new HashMap<>();
        parsedRequest.put("Type", brokenDownRequest[0]);
        parsedRequest.put("Path", brokenDownRequest[1]);
        parsedRequest.put("httpVersion", brokenDownRequest[2]);
        return parsedRequest;
    }

    private void generateHeader (String headerLine, HashMap<String, String> header) {
        String[] headerParts = headerLine.split(":", 2);
        String headerLabel = headerParts[0];
        String headerBody = headerParts[1];

        if (headerLabel.length() == 0 || headerLabel == null){
            /**
             * TODO throw HTTP Error saying that we have gotten invalid headers
             */
            return;
        }

        header.put(headerLabel, headerBody);
        return;
    }

    public String cleanBody (String body){
        String[] splitBody = body.split("=",2);
        String uncleanBody = splitBody[1].strip();
        String[] brokenDownBody = uncleanBody.split("\\+");
        String cleanBody = String.join(" ", brokenDownBody);

        LOGGER.info("Body after cleaning is : " + cleanBody);
        return cleanBody;
    }

    private StringBuffer generateBody (String bodyLine, StringBuffer messageBody) {
        messageBody.append(bodyLine).append("\n\r");
        return messageBody;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public String getBody() {
        //send unfiltered body
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

    public HashMap<String, String> getRequest () {
        return request;
    }


}
