package echoclient;

import java.io.*;
import java.net.*;

public class EchoClient {
    private Socket socket;
    private InputStream input; 
    private OutputStream output;
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        input = socket.getInputStream();
        output = socket.getOutputStream();
    }

    public void start() throws IOException {
        // Read from System.in and send to server
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = System.in.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
            output.flush();
            
            // Read response from server
            bytesRead = input.read(buffer);
            if (bytesRead != -1) {
                System.out.write(buffer, 0, bytesRead);
                System.out.flush();
            }
        }
        
        // Signal end of client output
        socket.shutdownOutput();
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 6013;
        
        EchoClient client = new EchoClient(host, port);
        try {
            client.connect();
            client.start();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }
}