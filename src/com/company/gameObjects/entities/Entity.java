package com.company.gameObjects.entities;

import com.company.Main;
import com.company.gameArt.Door;
import com.company.gameArt.Tile;
import com.company.gameObjects.GameObject;

import java.awt.*;

public abstract class Entity extends GameObject {
    public Entity(int x, int y) {
        super(x, y);
    }

    public abstract Rectangle getOffsetBoundsH();
    public abstract Rectangle getOffsetBoundsV();

    public void checkCollision() {
        for (Tile obj : Main.room.walls) {
            if (getOffsetBoundsH().intersects(obj.getBounds())) {
                if (xVel > 0) {
                    x = obj.x - getBounds().width;
                } else if (xVel < 0) {
                    x = obj.x + obj.getBounds().width;
                }
                xVel = 0;
            }
            if (getOffsetBoundsV().intersects(obj.getBounds())) {
                if (yVel > 0) {
                    y = obj.y - getOffsetBoundsV().height;
                } else if (yVel < 0) {
                    y = obj.y + obj.getBounds().height;
                }
                yVel = 0;
            }
        }
        for (Door obj : Main.room.doors) {
            if (getOffsetBoundsH().intersects(obj.getBounds())) {
                if (xVel > 0) {
                    x = obj.x - getBounds().width;
                } else if (xVel < 0) {
                    x = obj.x + obj.getBounds().width;
                }
                xVel = 0;
            }
            if (getOffsetBoundsV().intersects(obj.getBounds())) {
                if (yVel > 0) {
                    y = obj.y - getOffsetBoundsV().height;
                } else if (yVel < 0) {
                    y = obj.y + obj.getBounds().height;
                }
                yVel = 0;
            }
        }
        if (getOffsetBoundsH().x < 0) {
            x = 0;
            xVel = 0;
        }
        if (getOffsetBoundsH().x > Main.room.mapX) {
            x = Main.room.mapX;
            xVel = 0;
        }

        if (getOffsetBoundsV().y < 0) {
            y = 0;
            yVel = 0;
        }
        if (getOffsetBoundsV().y > Main.room.mapY) {
            y = Main.room.mapY;
            yVel = 0;
        }
    }
}
