package com.main.game.objects.singleplayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.main.game.objects.Actor;


public class Player extends Actor {

    public static final int OFFSET = 20;
    public static final int WIDTH = 200;
    public static final int HEIGHT = 20;

    private int color = Color.WHITE;

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(getX() - WIDTH/2, getY() - HEIGHT/2,
                getX() + WIDTH/2, getY() + HEIGHT/2, paint);
        paint = null;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
