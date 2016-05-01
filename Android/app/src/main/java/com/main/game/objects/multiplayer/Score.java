package com.main.game.objects.multiplayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.main.game.objects.Actor;

public class Score extends Actor {

    public static final int X = 50;
    public static final int Y = 50;

    private int you;
    private int enemy;

    public void setScore(int you, int enemy) {
        this.you = you;
        this.enemy = enemy;
    }

    public int getScoreForYou() { return you; }
    public int getScoreForEnemy() { return enemy; }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(20f);
        paint.setColor(Color.WHITE);
        canvas.drawText(you + " : " + enemy, X, Y, paint);
        paint = null;
    }
}
