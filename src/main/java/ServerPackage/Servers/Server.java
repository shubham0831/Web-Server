package ServerPackage.Servers;

import ServerPackage.Handlers.Handler;
import ServerPackage.Mapping.PathHandlerMap;
import ServerPackage.ServerThreads.ServerThread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{
    protected int port;
    protected ServerSocket server;
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    protected PathHandlerMap map;
    protected volatile boolean running;

    public Server (int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(port);
        this.map = new PathHandlerMap();
        this.running = true;
    }

    public void addMapping (String path, Handler object){
        map.addMapping(path, object);
    }

    @Override
    public void run() {
        try {
            while (running) {
                Socket listenerSocket = server.accept();
                LOGGER.info("Connection accepted : " + listenerSocket.getInetAddress());
                ServerThread serverThread = new ServerThread(listenerSocket, map);
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
