package com.company.gameArt;

import com.company.Room;
import com.company.gameObjects.Particle;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DestructibleTile extends GameArt {
    private BufferedImage particleImg;
    private Tile tile;

    public DestructibleTile(int x, int y, BufferedImage img, BufferedImage particleImg) {
        super(x * Room.tw, y * Room.tw);
        this.img = img;
        this.particleImg = particleImg;
        tile = new Tile(x, y, img);
    }

    public void render(Graphics g) {
        g.drawImage(img, x, y, null);
    }

    public Tile getTile() {
        return tile;
    }

    public BufferedImage getParticleImg() {
        return particleImg;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, img.getWidth(), img.getHeight());
    }
}
