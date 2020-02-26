package com.company.gameArt;

import java.awt.*;

import static com.company.Room.tw;

public class Door extends GameArt {
    public Door(int x, int y) {
        super(x * tw, y * tw);
    }

    public void render(Graphics g) {

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, tw, tw);
    }
}
