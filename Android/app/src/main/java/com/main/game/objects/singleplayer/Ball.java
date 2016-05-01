package com.main.game.objects.singleplayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import com.main.game.GamePanel;
import com.main.game.objects.Actor;
import com.main.game.objects.singleplayer.Player;

public class Ball extends Actor {

    public static final int RADIUS = 10;

    public static final int MINIMUM_DX = -4;
    public static final int MAXIMUM_DX = 4;
    public static final int MINIMUM_DY = -9;
    public static final int MAXIMUM_DY = 9;


    private boolean roll;
    private float dx, dy;

    public Ball() {
        setPosition(GamePanel.WIDTH/2, GamePanel.HEIGHT/2);
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
        if(getX() > GamePanel.WIDTH || getX() < 0)
            return false;
        return true;
    }

    public boolean isInVerticalBounds() {
        if(getY() > GamePanel.HEIGHT || getY() < 0)
            return false;
        return true;
    }

    public boolean handleCollisionWithPlayer(Player player, boolean transformed) {
        if(!transformed) {
            if (getY() >= GamePanel.HEIGHT - Player.OFFSET - Player.HEIGHT) {
                if (getX() <= player.getX() + Player.WIDTH / 2 && getX() >= player.getX() - Player.WIDTH / 2) {
                    changeDY();
                    setPosition(getX(), GamePanel.HEIGHT - Player.OFFSET - Player.HEIGHT - 5);
                }
            }
        } else {
            if(getY() <= Player.OFFSET + Player.HEIGHT) {
                if(getX() <= player.getX() + Player.WIDTH/2 && getX() >= player.getX() - Player.WIDTH/2 ) {
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


    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        canvas.drawCircle(getX(), getY(), RADIUS, paint);

        paint = null;
    }

}
