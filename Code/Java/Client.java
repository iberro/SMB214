package chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread{
    private Socket socket;
    private String username;
    ArrayList<Client> clients;
    private BufferedReader read;
    private PrintWriter write;
    
    public Client(Socket socket, ArrayList<Client> clients) throws Exception{
        this.socket = socket;
        this.clients = clients;
        read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        write = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public void run() {
        try {  
            write.println("Username :");
            username = read.readLine();
            for (Client client : clients) {
                client.SendMessage(username + " is online.");
            }
            write.println("Welcome to the SMB Chat Room.  Type 'bye' to close.");
            String line;
            do {
                line = read.readLine();
                if (line != null) {
                    for (Client client : clients) {
                        client.SendMessage(username + ": " + line);
                    }
                }
            }while (!line.trim().equals("bye"));
            socket.close();
        } catch (IOException ex) {
            System.out.println("Connection lost.");
        }
    }
    
    public void SendMessage(String message){
        write.println(message);
    }
}
