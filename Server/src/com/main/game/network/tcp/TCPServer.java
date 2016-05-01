package com.main.game.network.tcp;

import com.main.game.network.OnAuthorizedMessageReceived;
import com.main.game.network.OnDisconnect;
import com.main.game.network.OnMessageReceived;
import com.main.game.network.Server;
import com.main.game.network.packets.PacketType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread implements Server, OnDisconnect {

    private boolean running = false;

    private PlayerConnection player1;
    private PlayerConnection player2;

    private int numOfConnections = 0;

    private OnAuthorizedMessageReceived callback;

    private ServerSocket serverSocket;

    private int port = -1;


    public TCPServer(OnAuthorizedMessageReceived callback) { this.callback = callback; }

    public TCPServer(OnAuthorizedMessageReceived callback, int port) {
        this.callback = callback;
        this.port = port;
    }

    @Override
    public void stopServer() {
        running = false;
        player1.stopConnection();
        player2.stopConnection();
        try {
            serverSocket.close();
        } catch(Exception e) { e.printStackTrace(); }
    }

    @Override
    public void sendAuthorizedMessage(int playernum, PacketType packetType, String message) {
        String messageWithPacketType = PacketType.include(message, packetType);
        switch(playernum) {
            case 1: player1.sendMessage(messageWithPacketType);
                break;
            case 2: player2.sendMessage(messageWithPacketType);
                break;
        }
    }

    @Override
    public void run() {
        super.run();

        running = true;

        try {

            if(port != -1)
                serverSocket = new ServerSocket(port);
            else serverSocket = new ServerSocket(Server.SERVER_PORT);

            System.out.println("Serving at port " + (port == -1 ? Server.SERVER_PORT : port));

            while(running) {
                // Player 1 connection
                Socket client1Socket = serverSocket.accept();
                System.out.println("S: Client 1 connected. Running his thread.");

                player1 = new PlayerConnection(this, client1Socket, new OnMessageReceived() {
                    @Override
                    public void onMessageReceived(String message) {
                        callback.onAuthorizedMessageReceived(1, message);
                    }
                }, this);
                numOfConnections++;
                player1.setId(numOfConnections);
                if(player1.isRunning())
                    player1.stopConnection();
                player1.start();

                // Player 2 connection
                Socket client2Socket = serverSocket.accept();
                System.out.println("S: Client 2 connected. Running his thread.");
                player2 = new PlayerConnection(this, client2Socket, new OnMessageReceived() {
                    @Override
                    public void onMessageReceived(String message) {
                        callback.onAuthorizedMessageReceived(2, message);
                    }
                }, this);
                numOfConnections++;
                player2.setId(numOfConnections);
                if(player2.isRunning())
                    player2.stopConnection();
                player2.start();

            }

        } catch(Exception e) {
            numOfConnections = 0;
            e.printStackTrace();
        }

    }

    @Override
    public void onDisconnect(int id) {
        try {
            if (id == 1) {
                System.out.println("S: Client 1 disconnected.");
                player1.join();
                // Player 1 connection
                Socket client1Socket = serverSocket.accept();
                System.out.println("S: Client 1 connected. Running his thread.");

                player1 = new PlayerConnection(this, client1Socket, new OnMessageReceived() {
                    @Override
                    public void onMessageReceived(String message) {
                        callback.onAuthorizedMessageReceived(1, message);
                    }
                }, this);
                numOfConnections++;
                player1.setId(numOfConnections);
                if(player1.isRunning())
                    player1.stopConnection();
                player1.start();
            } else if (id == 2) {
                System.out.println("S: Client 2 disconnected.");
                player1.join();
                // Player 2 connection
                Socket client2Socket = serverSocket.accept();
                System.out.println("S: Client 2 connected. Running his thread.");
                player2 = new PlayerConnection(this, client2Socket, new OnMessageReceived() {
                    @Override
                    public void onMessageReceived(String message) {
                        callback.onAuthorizedMessageReceived(2, message);
                    }
                }, this);
                numOfConnections++;
                player2.setId(numOfConnections);
                if (player2.isRunning())
                    player2.stopConnection();
                player2.start();
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    public int getNumberOfConnections() {
        return numOfConnections;
    }

    public void setNumberOfConnections(int num) {
        numOfConnections = num;
    }

    public void restart() {
        System.out.println("RESTARTING SERVER");
        stopServer();
        stop();
        start();
    }

}
