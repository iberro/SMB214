package smb214.chatroom;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Chat extends ActionBarActivity {

    private Socket socket;
    private int port ;
    private String ip;
    private String username;

    private BufferedReader read;
    private PrintWriter write;

    private TextView chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        chats = (TextView)findViewById(R.id.chat);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Connect(View view){
        EditText textIP = (EditText)findViewById(R.id.ip);
        EditText textPort = (EditText)findViewById(R.id.port);
        EditText textUsername = (EditText)findViewById(R.id.username);

        ip = textIP.getText().toString();
        port = Integer.parseInt(textPort.getText().toString());
        username = textUsername.getText().toString();
        try {
            socket = new Socket(ip, port);
            read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            write = new PrintWriter(socket.getOutputStream(), true);
            String strStart = read.readLine();
            write.println(username);
            ClientThread clientThread = new ClientThread();
            clientThread.start();

            textIP.setEnabled(false);
            textPort.setEnabled(false);
            textUsername.setEnabled(false);
            Button button = (Button)findViewById(R.id.connect);
            button.setEnabled(false);
        }catch (Exception ex){
            Log.d("Error", "Socket Error");
            ex.printStackTrace();
        }
    }

    public void send(View view){
        EditText textMsg = (EditText)findViewById(R.id.message);
        String message = textMsg.getText().toString();
        textMsg.setText("");
        write.println(message);
    }


    class ClientThread extends Thread {

        public ClientThread(){

        }

        @Override
        public void run() {
            while(true){
                try{
                    String message = read.readLine();
                    Log.d("msg", chats.getText().toString() + "\n" + message);
                    chats.setText(chats.getText().toString() + "\n" + message);
                }catch (Exception ex){

                }
            }


        }
    }
}
