package echoserver;

import java.io.*;
import java.net.*;

public class EchoServer {
    private ServerSocket serverSocket;
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);
        
        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                 InputStream input = clientSocket.getInputStream();
                 OutputStream output = clientSocket.getOutputStream()) {
                
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

    public void stop() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

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