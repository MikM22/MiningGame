package com.company.gameArt;

import java.awt.*;
import java.awt.image.BufferedImage;
import static com.company.Room.*;

public class Tile extends GameArt {

    public Tile(int x, int y, BufferedImage img) {
        super(x * tw, y * tw);
        this.img = img;
    }

    public void render(Graphics g) {
        g.drawImage(img, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, img.getWidth(), img.getHeight());
    }
}
