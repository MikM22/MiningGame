package com.company.gameObjects;

import com.company.Loader;
import com.company.Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Particle extends GameObject {
    private float rate, acceleration = 5;
    private int height;
    private double angle, speed;
    private boolean particlesMove, gravity;
    private BufferedImage particleImg;
    private double x, y;

    public Particle(int x, int y, float size, double angle, double speed, boolean particlesMove, float maxLifeTime) {
        super(0, 0);
        this.x = x;
        this.y = y;
        xScale = size;
        yScale = size;
        this.angle = angle;
        this.speed = speed;
        this.particlesMove = particlesMove;
        this.gravity = false;
        rate = 1 / (maxLifeTime * 30f);
        shadow = false;
        particleImg = Main.particles[0];
    }

    public Particle(int x, int y, float size, double angle, double speed, boolean particlesMove, boolean gravity, boolean shadow, float maxLifeTime, BufferedImage img) {
        super(0, 0);
        this.x = x;
        this.y = y;
        xScale = size;
        yScale = size;
        this.angle = angle;
        this.speed = speed;
        this.particlesMove = particlesMove;
        this.gravity = gravity;
        rate = 1 / (maxLifeTime * 30f);
        this.shadow = shadow;
        particleImg = img;
    }

    public void tick() {
        if (particlesMove) {
            x += speed * Math.sin(angle);
            y += speed * Math.cos(angle);
        }
        if (gravity) {
            height -= acceleration;
            acceleration -= .3f;
            if (height > 6 && acceleration < 0) {
                acceleration = 0;
                speed *= .95f;
            }
        }
        xScale -= rate;
        yScale -= rate;
        shadowMultiplier = xScale;
        if (xScale <= 0) {
            Main.room.objects.remove(this);
        }
    }

    public void render(Graphics2D g) {
        Loader.renderScaledImageCenter(g, g.getTransform(), (int)x, (int)y + height, this, particleImg);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, particleImg.getWidth(), particleImg.getHeight());
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }
}
