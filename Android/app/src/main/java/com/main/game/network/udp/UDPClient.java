package com.main.game.network.udp;

import android.util.Log;

import com.main.game.network.Client;
import com.main.game.network.OnMessageReceived;
import com.main.game.network.packets.PacketType;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by juraj on 29.04.16.
 */
public class UDPClient extends Thread implements Client {

    public static final String SERVER_IP = "192.168.0.11";
    public static final int SERVER_PORT = 4444;

    private static final String TAG = "UDP_Client";

    private boolean running = false;
    private String message = null;

    private int port = -1;
    private String ip = "";
    private OnMessageReceived callback;

    private DatagramSocket clientSocket;

    public UDPClient(String ip, int port, OnMessageReceived callback) {
        this.ip = ip;
        this.port = port;
        this.callback = callback;
    }

    public void sendMessage(PacketType packetType, String message) {
        byte[] data = PacketType.include(message, packetType).getBytes();
        Log.v(TAG, "SENDING: " + message);
        try {
            DatagramPacket packet;
            if(ip != "" && port != -1)
                packet = new DatagramPacket(data, data.length, InetAddress.getByName(ip), port);
            else packet = new DatagramPacket(data, data.length, InetAddress.getByName(SERVER_IP), SERVER_PORT);
            clientSocket.send(packet);
        } catch(Exception e) {}
    }

    @Override
    public void run() {
        running = true;

        try {

            clientSocket = new DatagramSocket();

            sendMessage(PacketType.CONNECT, "");

            while(running) {
                byte[] data = new byte[256];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                clientSocket.receive(packet);
                callback.messageReceived(new String(packet.getData()));
            }

        } catch(Exception e) {
            Log.e(TAG, "Exception", e);
        }


    }

    @Override
    public void disconnect() {
        running = false;
        sendMessage(PacketType.DISCONNECT, "");
        clientSocket.close();
    }
}
