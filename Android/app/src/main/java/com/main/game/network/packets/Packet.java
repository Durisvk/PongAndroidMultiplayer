package com.main.game.network.packets;


import com.main.game.MultiPlayerGamePanel;

public interface Packet {

    public void process(MultiPlayerGamePanel multiPlayerGamePanel, String message);

}
