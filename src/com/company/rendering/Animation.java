package com.company.rendering;
import com.company.Loader;
import com.company.Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {
    private int speed;
    private int frame;
    private int time;
    private BufferedImage[] img, flipped;
    private BufferedImage currentImg;
    private float xScale = 1f, yScale = 1f;
    private boolean done = true, flip = false, teleport;

    public Animation(int speed, BufferedImage[] img, boolean flipHorizontally){
        this.speed = speed;
        this.img = img;
        flipped = Loader.flipped(img, flipHorizontally);
    }

    public Animation(int speed, BufferedImage[] img, boolean flipHorizontally, boolean teleport){
        this.teleport = teleport;
        this.speed = speed;
        this.img = img;
        flipped = Loader.flipped(img, flipHorizontally);
    }

    public void restart() {
        done = false;
        frame = 0;
    }

    public void flip() {
        flip = !flip;
    }

    public void setScale(float xScale, float yScale) {
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public void setFlipped(boolean flip) {this.flip = flip;}

    public boolean isFlipped() {
        return flip;
    }

    public void repeatAnimation(){
        time++;
        if (time > speed) {
            frame++;
            time -= speed;
        }
        if (frame == img.length) {
            frame = 0;
        }
        currentImg = (flip ? flipped : img)[frame];
    }

    public void runAnimation(){
        if (!done) {
            time++;
            if (time > speed) {
                frame++;
                time -= speed;
            }
            if (teleport && frame == 20) {
                Main.player.showPlayer = true;
                Main.player.shadow = true;
            }
            if (frame == img.length) {
                done = true;
                currentImg = null;
                return;
            }
            currentImg = (flip ? flipped : img)[frame];
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getxScale() {
        return xScale;
    }

    public float getyScale() {
        return yScale;
    }

    public boolean isRunning() {
        return !done;
    }

    public BufferedImage getFirstImage() {
        return img[0];
    }

    public void drawAnimation(Graphics g, double x, double y, int offset){
        g.drawImage(currentImg, (int)x - offset, (int)y, null);
    }
}
