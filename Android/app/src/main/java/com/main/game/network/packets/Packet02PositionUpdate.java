package com.main.game.network.packets;


import com.main.game.MultiPlayerGamePanel;

public class Packet02PositionUpdate implements Packet {

    @Override
    public void process(MultiPlayerGamePanel multiPlayerGamePanel, String message) {
        String[] segments = message.split(";");
        String[] ballSegment = segments[0].split(":");
        String[] enemySegment = segments[1].split(":");
        String[] youSegment = segments[2].split(":");
        multiPlayerGamePanel.getBall().setPosition(Float.parseFloat(ballSegment[0]), Float.parseFloat(ballSegment[1]));
        multiPlayerGamePanel.getEnemy().setPosition(Float.parseFloat(enemySegment[0]), multiPlayerGamePanel.getEnemy().getY());
        multiPlayerGamePanel.getYou().setPosition(Float.parseFloat(youSegment[0]), multiPlayerGamePanel.getYou().getY());

    }
}
