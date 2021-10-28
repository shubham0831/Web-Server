package ServerPackage;

import ServerPackage.Mapping.PathHandlerMap;
import ServerPackage.ServerUtils.HTTPParser;
import ServerPackage.ServerUtils.HttpWriter;
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
    private PathHandlerMap map;
    private static final Logger LOGGER = LogManager.getLogger(ServerThread.class);

    public ServerThread (Socket socket, PathHandlerMap map){
        this.socket = socket;
        this.map = map;
    }

    @Override
    public void run() {
        try (
                InputStream inputStream = socket.getInputStream();
                HttpWriter response = new HttpWriter(socket);
        ){
            //https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
            HTTPParser httpParser = new HTTPParser(inputStream);


            LOGGER.info("Server -> request type is : " + httpParser.getRequestType());
            LOGGER.info("Server -> request path is : " + httpParser.getRequestPath().strip());
            LOGGER.info("Server -> request httpVersion is : " + httpParser.getRequestHttpVersion());

            String path = httpParser.getRequestPath().strip();

            if (!map.contains(path)){
                /**
                 * TODO send HTTP response saying that the path is invalid
                 */
                LOGGER.info("Map does not contain the path : " + path);

            } else{
                LOGGER.info("Map contains the path : " + path);
                map.getObject(path).handle(httpParser, response);
            }

//            OutputStream writer = socket.getOutputStream();
//
//            String tempHTML = "<html><head><title>Test App</title></head><body><h1>Test successful</h1></body></html>";
//            String CRLF = "\n\r";
//            String response0 =
//                    "HTTP/1.1 200 OK" + CRLF + //this is the status line of the response
//                            "Content-Length: " +tempHTML.getBytes().length + CRLF + //the header, currently we only have the length
//                            CRLF +
//                            tempHTML + //actual html content
//                            CRLF + CRLF;
//
//            writer.write(response0.getBytes());

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
