package com.company.gameArt;

import com.company.Weapon;

import java.awt.*;

import static com.company.Room.tw;

public class ItemSpot extends GameArt {
    public Weapon weapon;

    public ItemSpot(int x, int y, Weapon weapon) {
        super(x * tw, y * tw);
        this.weapon = weapon;
    }

    public void render(Graphics g) {
        if (weapon != null) {
            g.drawImage(weapon.img, x, y, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y + tw, tw, tw);
    }
}
