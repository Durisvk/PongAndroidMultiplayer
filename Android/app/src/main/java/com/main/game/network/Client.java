package com.main.game.network;

import com.main.game.network.packets.PacketType;

/**
 * Created by juraj on 29.04.16.
 */
public interface Client {

    void sendMessage(PacketType packetType, String message);

    void start();

    void disconnect();

}
