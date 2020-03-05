package com.company.gameObjects;

import com.company.Main;
import com.company.Room;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ZOrderTile extends GameObject {
    private boolean drawInFront;

    public ZOrderTile(int x, int y, BufferedImage img) {
        super(x * Room.tw, y * Room.tw);
        this.img = img;
    }

    public void tick() {
        if (Main.player.y > y) {
            drawInFront = false;
        } else {
            drawInFront = true;
        }
    }

    public boolean drawInFront() {
        return drawInFront;
    }

    public void render(Graphics2D g) {
        g.drawImage(img, x, y, null);
    }

    public Rectangle getBounds() {
        return null;
    }
}
