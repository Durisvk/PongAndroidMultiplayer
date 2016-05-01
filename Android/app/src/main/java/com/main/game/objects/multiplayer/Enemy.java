package com.main.game.objects.multiplayer;

import com.main.game.GamePanel;


public class Enemy extends Player {

    public Enemy() {
        setPosition(GamePanel.WIDTH / 2, Player.OFFSET);
    }


}
