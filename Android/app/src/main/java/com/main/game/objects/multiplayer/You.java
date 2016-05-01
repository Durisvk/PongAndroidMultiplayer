package com.main.game.objects.multiplayer;

import com.main.game.GamePanel;

public class You extends Player {

    public You() {
        setPosition(GamePanel.WIDTH / 2, GamePanel.HEIGHT - OFFSET);
    }

}
