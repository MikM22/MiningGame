package com.company.rendering;

import com.company.Loader;
import com.company.Main;
import com.company.gameObjects.entities.Player;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PlayerAnimation {
    private List<Animation> animations;
    private Animation current;
    private int particleTime;
    private Player player;
    private BufferedImage flippedHead;
    private final int animSpeed = 12;
    private Point dir = new Point(1, 0);

    public PlayerAnimation(Player player) {
        ArrayList<Animation> animations = new ArrayList<>();
        animations.add(new Animation(0, new BufferedImage[]{player.imgs[1]}, true));
        animations.add(new Animation(animSpeed, new BufferedImage[]{player.imgs[2], player.imgs[3]}, true));
        this.animations = animations;
        flippedHead = Loader.flipped(player.imgs[0], true);
        this.player = player;
        current = animations.get(0);
    }

    public void repeatAnimation() {
        final int maxParticleTime = 15;
        double angle = Math.atan2(Display.mousePos.x - (player.getX() + player.getBounds().width / 2f), Display.mousePos.y - (player.getY() + player.getBounds().height / 2f));
        if (player.getxVel() > 0 || angle > 0 && player.isAttacking()) {
            animations.get(1).setFlipped(false);
        } else if (player.getxVel() < 0 || angle <= 0 && player.isAttacking()) {
            animations.get(1).setFlipped(true);
        }
        if (player.getyVel() == 0 && player.getxVel() == 0) {
            animations.get(0).setFlipped(animations.get(1).isFlipped());
            current = animations.get(0);
            particleTime = 0;
        } else {
            current = animations.get(1);
            if (particleTime > maxParticleTime) {
                particleTime = 0;
                Main.room.addParticle(player.getCx(), player.getCy() + player.getBounds().height / 2, 3, 5, .3f, .6f, 0, 0, 4, false, false, false, 0, 0);
            }
            particleTime++;
        }
        current.repeatAnimation();
    }

    public boolean isFlipped() {
        return animations.get(1).isFlipped();
    }

    public void drawAnimation(Graphics2D g, int x, int y, int offset) {
        g.drawImage(animations.get(1).isFlipped() ? flippedHead : player.imgs[0], x, y, null);
        current.drawAnimation(g, x, y, offset);
    }
}
