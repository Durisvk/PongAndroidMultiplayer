package com.main.game.network;

import com.main.game.network.packets.PacketType;

public interface Server {

    public static final int SERVER_PORT = 4444;

    void sendAuthorizedMessage(int playernum, PacketType packetType, String message);

    void start();

    void stopServer();

    int getNumberOfConnections();
}
