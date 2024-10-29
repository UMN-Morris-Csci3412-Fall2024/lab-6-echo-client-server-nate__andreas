package echoserver;
import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java EchoClient <server-hostname>");
            return;
        }

        String serverHostname = args[0];
        int port = 12345; // Make sure this matches the server's port number

        try {
            // Adding a slight delay to ensure server is ready
            Thread.sleep(1000);

            try (Socket socket = new Socket(serverHostname, port);
                 InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream()) {

                // Use BufferedInputStream and BufferedOutputStream for handling of binary data
                BufferedInputStream bufferedInput = new BufferedInputStream(System.in);
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
                BufferedInputStream socketInput = new BufferedInputStream(input);

                byte[] buffer = new byte[4096]; // Increased buffer size
                int bytesRead;
                while ((bytesRead = bufferedInput.read(buffer)) != -1) {
                    bufferedOutput.write(buffer, 0, bytesRead);
                    bufferedOutput.flush();

                    int serverBytesRead = socketInput.read(buffer);
                    if (serverBytesRead != -1) {
                        System.out.write(buffer, 0, serverBytesRead);
                        System.out.flush();
                    }
                }

                System.out.println("Connection closed.");
            }
        } catch (InterruptedException e) {
            System.err.println("Client startup interrupted: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}