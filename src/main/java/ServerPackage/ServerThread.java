package ServerPackage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

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
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream writer = socket.getOutputStream();
        ){
            //https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java

            String request = inputStream.readLine();

            LOGGER.info("Received request : \n" + request);
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
