package ServerPackage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{
    private int port;
    private ServerSocket server;
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private volatile boolean running;

    public Server (int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(port);
        this.running = true;
    }

    @Override
    public void run() {

        try {
            while (running) {
                Socket listenerSocket = server.accept();
                LOGGER.info("Connection accepted : " + listenerSocket.getInetAddress());
                ServerThread serverThread = new ServerThread(listenerSocket);
                serverThread.start();
            }
        } catch (IOException e) {
            LOGGER.error("Error in setting up the socket : \n" + e);
        }finally {
            try {
                server.close();
            } catch (IOException e) {
                LOGGER.error("Error in closing server socket : \n" + e);
                e.printStackTrace();
            }
        }

    }
}
