// EchoServer.java
package echoserver;

import java.io.*;
import java.net.*;

public class EchoServer {
    // Server socket that listens for client connections
    private ServerSocket serverSocket;
    // Port number to listen on
    private final int port;

    /**
     * Constructor - initializes server with port
     * @param port Port number to listen on
     */
    public EchoServer(int port) {
        this.port = port;
    }

    /**
     * Starts server and handles client connections
     * @throws IOException If server fails to start
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);
        
        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                 InputStream input = clientSocket.getInputStream();
                 OutputStream output = clientSocket.getOutputStream()) {
                
                // Echo loop - read from client and write back
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                    output.flush();
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }
    }

    /**
     * Stops the server and releases resources
     */
    public void stop() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    /**
     * Entry point - creates and runs server
     * @param args Command line arguments [port]
     */
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 6013;
        EchoServer server = new EchoServer(port);
        
        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            server.stop();
        }
    }
}