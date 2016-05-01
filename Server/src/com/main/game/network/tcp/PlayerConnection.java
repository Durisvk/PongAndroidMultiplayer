package com.main.game.network.tcp;

import com.main.game.network.OnDisconnect;
import com.main.game.network.OnMessageReceived;
import com.main.game.network.packets.PacketType;

import java.io.*;
import java.net.Socket;

public class PlayerConnection extends Thread {

    private OnMessageReceived callback;
    private OnDisconnect disconnectCallback;

    private Socket socket;
    private boolean running = false;
    private TCPServer server;

    private int id;

    private PrintWriter out;
    private BufferedReader in;

    public PlayerConnection(TCPServer server, Socket socket, OnMessageReceived callback, OnDisconnect disconnect) {
        this.server = server;
        this.socket = socket;
        this.callback = callback;
    }

    public void setId(int id) { this.id = id; }

    public int getIdentificator() { return id; };

    public void stopConnection() {
        running = false;
    }

    public void sendMessage(String message) {
        if(out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    @Override
    public void run() {
        running = true;

        try {

            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (running) {

                String message = in.readLine();

                if(message != null && callback != null) {
                    if(PacketType.parse(message) == PacketType.CONNECT) {
                        System.out.println("Client sent a connection packet.");
                        continue;
                    }
                    callback.onMessageReceived(message);
                    if(PacketType.parse(message) == PacketType.DISCONNECT) {
                        stopConnection();
                        join();
                        disconnectCallback.onDisconnect(getIdentificator());
                    }
                }

                message = null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            server.setNumberOfConnections(server.getNumberOfConnections() - 1);

            if(server.getNumberOfConnections() == 0)
                server.restart();
        }
    }

    public boolean isRunning() { return running; }

}
