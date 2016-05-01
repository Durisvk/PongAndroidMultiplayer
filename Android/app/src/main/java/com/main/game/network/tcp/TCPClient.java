package com.main.game.network.tcp;

import android.util.Log;

import com.main.game.network.Client;
import com.main.game.network.OnMessageReceived;
import com.main.game.network.packets.PacketType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class TCPClient extends Thread implements Client {

    public static final String SERVER_IP = "192.168.0.11";
    public static final int SERVER_PORT = 4444;

    private static final String TAG = "TCP Client";

    private String serverIp = "";
    private int serverPort = -1;

    private boolean running = false;
    private String message = null;
    private OnMessageReceived callback;

    private Socket clientSocket;

    private PrintWriter out;
    private BufferedReader in;

    public TCPClient(String ipAddress, int serverPort, OnMessageReceived callback) {
        this.serverIp = ipAddress;
        this.serverPort = serverPort;
        this.callback = callback;
    }

    public TCPClient(OnMessageReceived callback) {
        this.callback = callback;
    }

    public void sendMessage(PacketType packetType, String message) {
        if(out != null && !out.checkError()) {
            out.println(PacketType.include(message, packetType));
            out.flush();
        }
    }

    public void stopClient() {
        running = false;
    }

    public void disconnect() {
        running = false;
        sendMessage(PacketType.DISCONNECT, "");
        try {
            clientSocket.close();
        } catch(Exception e) {}
    }

    public void run() {
        running = true;

        try {

            InetAddress serverAddr;
            if(serverIp == "")
                serverAddr = InetAddress.getByName(SERVER_IP);
            else serverAddr = InetAddress.getByName(serverIp);


            if(serverPort == -1)
                clientSocket = new Socket(serverAddr, SERVER_PORT);
            else clientSocket = new Socket(serverAddr, serverPort);

            try {

                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                sendMessage(PacketType.CONNECT, "");

                while(running) {
                    message = in.readLine();

                    if(message != null && callback != null) {
                        callback.messageReceived(message);
                    }

                    message = null;
                }

            } catch(Exception e) {}

        } catch(Exception e) {}
    }

}