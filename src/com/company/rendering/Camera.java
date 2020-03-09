package com.company.rendering;

import com.company.Main;
import com.company.Room;

public class Camera {
    public int x,y;
    public static double zoom = 1;

    public Camera() {
        x = Main.player.getX() - Display.width / 2 + Main.player.getBounds().width / 2;
        y = Main.player.getY() - Display.height / 2 + Main.player.getBounds().height / 2;
    }

    public void tick() {
        x += (int)(((Main.player.getX() - x) - Display.width / 2 + Main.player.getBounds().width / 2) * 0.1f);
        y += (int)(((Main.player.getY() - y) - Display.height / 2 + Main.player.getBounds().height / 2) * 0.1f);


//        if (Main.room.mapX > Display.width) {
//            x += (int)(((Main.player.getX() - x) - Display.width / 2 + Main.player.getBounds().width / 2) * 0.08f);
//            if (x < 0) {
//                x = 0;
//            }
//            if (x > xMax) {
//                x = xMax;
//            }
//        }
//        if (Main.room.mapY > Display.height) {
//            int yMax = (Main.room.yTiles + 1) * Room.tw - Display.height + 39;
//            y += (int)(((Main.player.getY() - y) - Display.height / 2 + Main.player.getBounds().height / 2) * 0.08f);
//            if (y < 0) {
//                y = 0;
//            }
//            if (y > yMax) {
//                y = yMax;
//            }
//        }
    }
}
