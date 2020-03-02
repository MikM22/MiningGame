package com.company.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;

class HpAnimation {
    private int speed;
    private int frame;
    private int time;
    private BufferedImage[][] img;
    private BufferedImage[] currentImg, test;

    HpAnimation(int speed, BufferedImage[] img){
        this.speed = speed;
        this.test = img;
        this.img = new BufferedImage[img.length][80];
        for (int x = 0; x < img.length; x++) {
            for (int y = 0; y < 80; y++) {
                this.img[x][y] = img[x].getSubimage(0, 0, 48, y * 3 + 3);
            }
        }
        currentImg = null;
    }

    void repeatAnimation(){
        time++;
        if (time > speed) {
            frame++;
            time -= speed;
        }
        if (frame == 3) {
            frame = 0;
        }
        currentImg = img[frame];
    }

    void drawCroppedAnimation(Graphics g, double x, double y, int h) {
        if (currentImg != null) {
            g.drawImage(currentImg[h], (int) x, (int)y - h * 3, null);
        }
    }
}
