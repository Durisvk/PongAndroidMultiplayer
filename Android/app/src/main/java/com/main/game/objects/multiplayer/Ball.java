package com.main.game.objects.multiplayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.main.game.GamePanel;
import com.main.game.objects.Actor;

public class Ball extends Actor {

    public static final int RADIUS = 10;

    public Ball() {
        setPosition(GamePanel.WIDTH/2, GamePanel.HEIGHT/2);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        canvas.drawCircle(getX(), getY(), RADIUS, paint);

        paint = null;
    }

}
