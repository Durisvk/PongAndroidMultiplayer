package com.main.game.objects.singleplayer;


import com.main.game.GamePanel;
import com.main.game.objects.Actor;

public class Enemy extends Player {

    public static final float MAX_SPEED = 5.0f;

    Ball ball;
    private float dx;

    public Enemy(Ball ball) {
        this.ball = ball;
        setPosition(GamePanel.WIDTH / 2, Player.OFFSET);
    }

    public void update() {
        if(getX() < ball.getX()) {
            dx = (int)(Math.random() * 2 + Math.random() * MAX_SPEED);
        } else dx = (int)-(Math.random() * 2 + Math.random() * MAX_SPEED);

        setPosition(getX() + dx, getY());
    }
}
