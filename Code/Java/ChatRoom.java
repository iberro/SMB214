package chatroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoom {

    private ServerSocket server;
    private ArrayList<Client> clients;

    public ChatRoom(int port) {
        try {
            server = new ServerSocket(port);
            clients = new ArrayList();
        } catch (Exception err) {
            System.out.println(err);
        }
    }

    public void serve() {
        try {
            while (true) {
                System.out.println("Wating new connection ...");
                Socket clientSocket = server.accept();
                System.out.println("New Connection established.");
                Client client = new Client(clientSocket, clients);
                clients.add(client);
                client.start();
            }
        } catch (Exception err) {
            System.err.println(err);
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting server ...");
        ChatRoom chatRoom = new ChatRoom(12345);
        chatRoom.serve();
        
    }

}
