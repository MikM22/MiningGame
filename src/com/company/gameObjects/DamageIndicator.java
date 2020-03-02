package com.company.gameObjects;

import com.company.Loader;
import com.company.Main;
import com.company.Room;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DamageIndicator extends GameObject {
    private static BufferedImage goldText = Loader.loadImage("goldText", Room.imageMult), img;
    private int time;
    private float y, acceleration = 5;

    public DamageIndicator(int x, int y, boolean gold) {
        super(x, y);
        this.y = y;
        if (gold) {
            img = goldText;
            this.x -= 18;
        }
        shadow = false;
    }

    public void tick() {
        time++;
        y -= acceleration;
        if (acceleration >= 0) {
            acceleration -= .3f;
        }
        if (time > 30) {
            Main.room.objects.remove(this);
        }
    }

    public void render(Graphics2D g) {
        g.drawImage(img, x, (int)y, null);
    }

    public Rectangle getBounds() {
        return null;
    }
}
