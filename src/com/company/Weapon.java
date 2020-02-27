package com.company;

import java.awt.image.BufferedImage;

public class Weapon {
    public BufferedImage img, imgFlipped, projectileImg;
    public float xRangeScale, yRangeScale;
    public int attackTime, damage, KBMultiplier;

    public boolean melee;
    public int range, speed;

    public Weapon(BufferedImage img, float xRangeScale, float yRangeScale, int attackTime, int damage, int KBMultiplier) {
        melee = true;
        this.img = img;
        imgFlipped = Loader.flipped(img, true);
        this.xRangeScale = xRangeScale;
        this.yRangeScale = yRangeScale;
        this.attackTime = attackTime;
        this.damage = damage;
        this.KBMultiplier = KBMultiplier;
    }

    public Weapon(BufferedImage img, BufferedImage projectileImg, int attackTime, int damage, int KBMultiplier, int range, int speed) {
        melee = false;
        this.img = img;
        imgFlipped = Loader.flipped(img, true);
        this.projectileImg = projectileImg;
        this.attackTime = attackTime;
        this.damage = damage;
        this.KBMultiplier = KBMultiplier;
        this.range = range;
        this.speed = speed;
    }
}
