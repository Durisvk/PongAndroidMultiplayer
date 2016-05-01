package com.main.game.objects;


public abstract class Actor {

    private float x,y;

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }

    public float getY() { return y; }

}
