package com.company.gameObjects;

import com.company.Loader;
import com.company.Main;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

public class Particle extends GameObject {
    private float rate, acceleration = 5, rectRate, rectSize;
    private int height;
    private double angle, speed;
    private boolean particlesMove, gravity, front, rectangle;
    private BufferedImage particleImg;
    private Color rectColor;
    private double x, y;

    public Particle(int x, int y, float size, double angle, double speed, boolean particlesMove, float maxLifeTime, boolean front) {
        super(0, 0);
        this.x = x;
        this.y = y;
        xScale = size;
        yScale = size;
        this.angle = angle;
        this.front = front;
        this.speed = speed;
        this.particlesMove = particlesMove;
        this.gravity = false;
        rate = 1 / (maxLifeTime * 30f);
        shadow = false;
        particleImg = Main.particles[0];
    }

    public Particle(int x, int y, float size, double angle, double speed, boolean particlesMove, float maxLifeTime, boolean front, boolean rectangle, int rectSize, Color rectColor, float rectRate) {
        super(0, 0);
        this.x = x;
        this.y = y;
        xScale = size;
        yScale = size;
        this.rectSize = rectSize;
        this.angle = angle;
        this.front = front;
        this.rectRate = rectRate;
        this.rectColor = rectColor;
        this.speed = speed;
        this.particlesMove = particlesMove;
        this.rectangle = rectangle;
        this.gravity = false;
        rate = 1 / (maxLifeTime * 30f);
        shadow = false;
        particleImg = Main.particles[0];
    }

    public Particle(int x, int y, float size, double angle, double speed, boolean particlesMove, boolean gravity, boolean shadow, float maxLifeTime, boolean front, BufferedImage img) {
        super(0, 0);
        if (rectangle) {
            this.x = Loader.rotatePoint(x, y, angle, new Point(x + (int)rectSize, y + (int)rectSize)).x;
            this.y = Loader.rotatePoint(x, y, angle, new Point(x + (int)rectSize, y + (int)rectSize)).y;
        } else {
            this.x = x;
            this.y = y;
        }
        xScale = size;
        yScale = size;
        this.angle = angle;
        this.front = front;
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
        if (rectSize > 0)
            rectSize -= rectRate;
        xScale -= rate;
        yScale -= rate;
        shadowMultiplier = xScale;
        if (xScale <= 0) {
            if (front) {
                Main.room.frontObjects.remove(this);
            } else {
                Main.room.objects.remove(this);
            }
        }
    }

    public void render(Graphics2D g) {
        if (rectangle) {
            g.setColor(rectColor);
            g.fill(rectangle());
        } else
        Loader.renderScaledImageCenter(g, g.getTransform(), (int)x, (int)y + height, this, particleImg);
    }

    private Area rectangle() {
        Area a = new Area(new Rectangle((int)x, (int)y, (int)rectSize, (int)rectSize));
        AffineTransform af = new AffineTransform();
        af.rotate(angle, x, y);
        return a.createTransformedArea(af);
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
