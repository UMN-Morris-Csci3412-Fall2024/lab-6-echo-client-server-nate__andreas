// EchoClient.java
package echoserver;

import java.io.*;
import java.net.*;

public class EchoClient {
    // Socket for client-server communication
    private Socket socket;
    // Stream for receiving data from server
    private InputStream input; 
    // Stream for sending data to server
    private OutputStream output;
    // Server hostname to connect to
    private final String host;
    // Server port number to connect to
    private final int port;

    /**
     * Constructor - initializes client with server details
     * @param host Server hostname
     * @param port Server port number
     */
    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Establishes connection to server and initializes I/O streams
     * @throws IOException If connection fails
     */
    public void connect() throws IOException {
        socket = new Socket(host, port);
        input = socket.getInputStream();
        output = socket.getOutputStream();
    }

    /**
     * Main client loop - reads from stdin, sends to server, gets response
     * @throws IOException If I/O error occurs
     */
    public void start() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        
        // First read all input and send to server
        while ((bytesRead = System.in.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
            output.flush();
        }
        
        // Signal end of transmission
        socket.shutdownOutput();
        
        // Read server response
        while ((bytesRead = input.read(buffer)) != -1) {
            System.out.write(buffer, 0, bytesRead);
            System.out.flush();
        }
    }

    /**
     * Closes socket and releases resources
     */
    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }

    /**
     * Entry point - creates and runs client
     * @param args Command line arguments [host, port]
     */
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