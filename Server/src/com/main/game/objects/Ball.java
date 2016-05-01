package com.main.game.objects;


import com.main.game.Game;

import java.util.Random;

public class Ball extends Actor {

    public static final int MINIMUM_DX = -4;
    public static final int MAXIMUM_DX = 4;
    public static final int MINIMUM_DY = -9;
    public static final int MAXIMUM_DY = 9;


    private boolean roll;
    private float dx, dy;

    public Ball() {
        setPosition(Game.WIDTH/2, Game.HEIGHT/2);
    }

    public void roll() {
        roll = true;
        generateRandomMovement();
    }

    public void stop() {
        roll = false;
    }

    public void update() {
        setPosition(getX() + dx, getY() + dy);
        if(!isInHorizontalBounds())
            changeDX();

    }

    public boolean isRolling() {
        return roll;
    }

    public void changeDY() {
        dy = -dy;
    }

    public void changeDX() {
        dx = -dx;
    }

    public boolean isInHorizontalBounds() {
        if(getX() > Game.WIDTH || getX() < 0)
            return false;
        return true;
    }

    public boolean isInVerticalBounds() {
        if(getY() > Game.HEIGHT || getY() < 0)
            return false;
        return true;
    }

    public boolean handleCollisionWithPlayer(Player player, boolean transformed) {
        if(!transformed) {
            if (getY() >= Game.HEIGHT - Player.OFFSET - Player.HEIGHT) {
                if (getX() <= player.getX() + Player.WIDTH / 2 && getX() >= player.getX() - Player.WIDTH / 2) {
                    changeDY();
                    setPosition(getX(), Game.HEIGHT - Player.OFFSET - Player.HEIGHT - 5);
                }
            }
        } else {
            if(getTransformedY() >= Game.HEIGHT - Player.OFFSET - Player.HEIGHT) {
                if(getTransformedX() <= player.getX() + Player.WIDTH/2 && getTransformedX() >= player.getX() - Player.WIDTH/2 ) {
                    changeDY();
                    setPosition(getX(), Player.OFFSET + Player.HEIGHT + 5);
                }
            }
        }
        return false;
    }

    public void generateRandomMovement() {
        do {
            if(Math.random() > 0.5)
                dy = MINIMUM_DY;
            else dy = MAXIMUM_DY;


            if(Math.random() > 0.5)
                dx = MINIMUM_DX;
            else dx = MAXIMUM_DX;
        } while(dx == 0 || dy == 0);
    }

    public float getTransformedX() {
        float dis;

        if(getX() >= Game.WIDTH/2) {
            dis = getX() - Game.WIDTH/2;
            return Game.WIDTH/2 - dis;
        } else {
            dis = Game.WIDTH/2 - getX();
            return dis + Game.WIDTH/2;
        }

    }

    public float getTransformedY() {
        float dis;

        if(getY() >= Game.HEIGHT/2) {
            dis = getY() - Game.HEIGHT/2;
            return Game.HEIGHT/2 - dis;
        } else {
            dis = Game.HEIGHT/2 - getY();
            return dis + Game.HEIGHT/2;
        }
    }

}
