package ServerPackage;

import ServerPackage.ServerUtils.HTTPParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

/**
 * Since we want multi-threading support, ie. every instance to run independent of one another we use this class,
 * to abstract out the work, and run the work on a separate thread
 */
public class ServerThread extends Thread {
    private Socket socket;
    private static final Logger LOGGER = LogManager.getLogger(ServerThread.class);

    public ServerThread (Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                InputStream inputStream = socket.getInputStream();
                OutputStream writer = socket.getOutputStream();
        ){
            //https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
            HTTPParser httpParser = new HTTPParser(inputStream);

            String request = httpParser.getRequest();
            HashMap<String, String> headers = httpParser.getHeader();
            String body = httpParser.getBody();

            LOGGER.info("Server thread got request " + request);
            LOGGER.info("Server -> request type is : " + httpParser.getRequestType());
            LOGGER.info("Server -> request path is : " + httpParser.getRequestPath());
            LOGGER.info("Server -> request httpVersion is : " + httpParser.getRequestHttpVersion());

            httpParser.getRequestType();

            String tempHTML = "<html><head><title>Test App</title></head><body><h1>Test successful</h1></body></html>";
            String CRLF = "\n\r";
            String response =
                    "HTTP/1.1 200 OK" + CRLF + //this is the status line of the response
                            "Content-Length: " +tempHTML.getBytes().length + CRLF + //the header, currently we only have the length
                            CRLF +
                            tempHTML + //actual html content
                            CRLF + CRLF;

            writer.write(response.getBytes());
        } catch (IOException e){
            LOGGER.error("Error in closing the reader or writer");
        } finally {
            try {
                socket.close();
                LOGGER.info("Socket closed ");
            } catch (IOException e) {
                LOGGER.error("Error in closing the socket");
                e.printStackTrace();
            }

        }
    }
}
