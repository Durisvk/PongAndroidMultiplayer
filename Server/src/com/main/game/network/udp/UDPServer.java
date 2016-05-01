package com.main.game.network.udp;

import com.main.game.network.OnAuthorizedMessageReceived;
import com.main.game.network.Server;
import com.main.game.network.packets.PacketType;
import com.sun.xml.internal.ws.api.message.Packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class UDPServer extends Thread implements Server {

    private UDPClient player1;
    private UDPClient player2;

    private int port = -1;

    DatagramSocket serverSocket;

    private int numberOfConnections = 0;

    private OnAuthorizedMessageReceived callback;

    private boolean running;

    public UDPServer(OnAuthorizedMessageReceived callback) {
        this.callback = callback;
    }

    public UDPServer(OnAuthorizedMessageReceived callback, int port) {
        this.port = port;
        this.callback = callback;
    }

    @Override
    public void run() {
        super.run();

        running = true;

        try {

            if(port != -1)
                serverSocket = new DatagramSocket(port);
            else
                serverSocket = new DatagramSocket(Server.SERVER_PORT);

            System.out.println("Serving at port " + (port == -1 ? Server.SERVER_PORT : port));

            while (running) {

                byte[] data = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                serverSocket.receive(receivePacket);
                String message = new String(receivePacket.getData());
                PacketType packetType = PacketType.parse(message);
                if(player1 == null && packetType == PacketType.CONNECT) {
                    player1 = new UDPClient(receivePacket.getAddress(), receivePacket.getPort());
                    numberOfConnections++;
                    System.out.println("S: Client 1 connected.");
                    continue;
                } else if(player2 == null && packetType == PacketType.CONNECT) {
                    player2 = new UDPClient(receivePacket.getAddress(), receivePacket.getPort());
                    numberOfConnections++;
                    System.out.println("S: Client 2 connected.");
                    continue;
                }

                int playernum = receivePacket.getAddress().toString().equalsIgnoreCase(player1.getIp())
                                ? 1 : receivePacket.getAddress().toString().equalsIgnoreCase(player2.getIp()) ? 2 : null;

                if(player1 != null && packetType == PacketType.DISCONNECT && playernum == 1) {
                    System.out.println("S: Client 1 disconnected.");
                    player1 = null;
                    numberOfConnections--;
                } else if(player2 != null && packetType == PacketType.DISCONNECT && playernum == 2) {
                    System.out.println("S: Client 2 disconnected.");
                    player2 = null;
                    numberOfConnections--;
                }

                callback.onAuthorizedMessageReceived(playernum, message);
            }
        } catch(Exception e) { e.printStackTrace(); stopServer(); }

    }

    @Override
    public void sendAuthorizedMessage(int playernum, PacketType packetType, String message) {
        byte[] sendData = PacketType.include(message, packetType).getBytes();
        try {
            if (playernum == 1) {
                DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, player1.getInetAddress(), player1.getPort());
                serverSocket.send(datagramPacket);
            } else if (playernum == 2) {
                DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, player2.getInetAddress(), player2.getPort());
                serverSocket.send(datagramPacket);
            }
        } catch(Exception e) {}
    }

    @Override
    public void stopServer() {
        running = false;
        player1 = null;
        player2 = null;
        serverSocket.close();
    }

    @Override
    public int getNumberOfConnections() {
        return numberOfConnections;
    }
}
