package com.main.game.network.packets;

import com.main.game.MultiPlayerGamePanel;

/**
 * Created by juraj on 29.04.16.
 */
public class Packet03ScoreUpdate implements Packet {

    @Override
    public void process(MultiPlayerGamePanel multiPlayerGamePanel, String message) {
        String[] score = message.split(":");
        multiPlayerGamePanel.getScore().setScore(Integer.parseInt(score[0]), Integer.parseInt(score[1]));
    }
}
