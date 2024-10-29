package echoserver;
import java.io.*;
import java.net.*;

public class EchoServer {
    private static final int PORT = 12345; // Make sure this port matches the client's

    public static void main(String[] args) {
        System.out.println("Echo Server is starting...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo Server is running and ready to accept connections on port " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     InputStream input = clientSocket.getInputStream();
                     OutputStream output = clientSocket.getOutputStream()) {

                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    byte[] buffer = new byte[4096]; // Increased buffer size
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                        output.flush();
                    }

                    System.out.println("Client disconnected.");
                } catch (IOException e) {
                    System.err.println("Connection error: " + e.getMessage());
                }
            }
        } catch (BindException e) {
            System.err.println("Port " + PORT + " is already in use. Please choose another port.");
        } catch (IOException e) {
            System.err.println("Could not start server on port " + PORT + ": " + e.getMessage());
        }
    }
}