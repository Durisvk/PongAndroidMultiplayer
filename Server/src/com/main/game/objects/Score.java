package com.main.game.objects;

/**
 * Created by juraj on 29.04.16.
 */
public class Score extends Actor {

    private int player1 = 0;
    private int player2 = 0;

    public void setScore(int player1, int player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public int getScoreForPlayer1() { return player1; }
    public int getScoreForPlayer2() { return player2; }

}
