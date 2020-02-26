package com.company;

import java.awt.image.BufferedImage;

public class Weapon {
    public BufferedImage img, imgFlipped;
    public float xRangeScale, yRangeScale;
    public int attackTime, damage, KBMultiplier;

    public Weapon(BufferedImage img, float xRangeScale, float yRangeScale, int attackTime, int damage, int KBMultiplier) {
        this.img = img;
        imgFlipped = Loader.flipped(img, true);
        this.xRangeScale = xRangeScale;
        this.yRangeScale = yRangeScale;
        this.attackTime = attackTime;
        this.damage = damage;
        this.KBMultiplier = KBMultiplier;
    }
}
