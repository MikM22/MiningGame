package com.company;

import com.company.gameObjects.GameObject;
import com.company.rendering.Animation;
import com.company.rendering.Display;
import com.company.rendering.PlayerAnimation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Loader {
    private static Random r = new Random();

    public static BufferedImage loadImage(String path, float mult) {
        try {
            BufferedImage img = ImageIO.read(Loader.class.getResource("assets/images/" + path + ".png"));
            return resizeImage(img, (int) (img.getWidth() * mult), (int) (img.getHeight() * mult));
        } catch (IOException e) {
            return null;
        }
    }

    public static BufferedImage[] cutSpriteSheet(String path, int width, int height, int mult, int spriteWidth, int spriteHeight) {
        BufferedImage sheet = loadImage(path, 1);
        assert sheet != null;
        BufferedImage[] sprites = new BufferedImage[width * height];
        int z = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sprites[z] = resizeImage(sheet.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight), spriteWidth * mult, spriteHeight * mult);
                z++;
            }
        }
        return sprites;
    }

    public static float lerp(float start, float end, float t) {
        return start * (1 - t) + end * t;
    }

    public static int randomInt(int min, int max) {
        //returns number between min (inclusive) and max (inclusive)
        return r.nextInt((max - min) + 1) + min;
    }

    public static double randomDouble(double min, double max) {
        return min + (max - min) * r.nextDouble();
    }

    public static boolean randomBoolean() {
        return r.nextBoolean();
    }

    public static float randomFloat(float min, float max) {
        return min + r.nextFloat() * (max - min);
    }

    public static void renderScaledAnimationTree(Graphics2D g, AffineTransform old, GameObject obj, PlayerAnimation tree) {
        g.translate(-obj.getBounds().width * (obj.getxScale() - 1) / 2f, -obj.getBounds().height * (obj.getyScale() - 1) / 2f);
        g.scale(obj.getxScale(), obj.getyScale());
        tree.drawAnimation(g, (int) (obj.getX() / obj.getxScale()), (int) (obj.getY() / obj.getyScale() - obj.getBounds().height * (obj.getyScale() - 1)), 0);
        g.setTransform(old);
    }

    public static void renderScaledImage(Graphics2D g, AffineTransform old, int x, int y, GameObject obj, BufferedImage img) {
        g.translate(-obj.getBounds().width * (obj.getxScale() - 1) / 2f, -obj.getBounds().height * (obj.getyScale() - 1) / 2f);
        g.scale(obj.getxScale(), obj.getyScale());
        g.drawImage(img, (int)(x / obj.getxScale()), (int) (y / obj.getyScale() - obj.getBounds().height * (obj.getyScale() - 1)), null);
        g.setTransform(old);
    }

    public static void renderScaledImageCenter(Graphics2D g, AffineTransform old, int x, int y, GameObject obj, BufferedImage img) {
        g.translate(-obj.getBounds().width * (obj.getxScale() - 1) / 2f, -obj.getBounds().height * (obj.getyScale() - 1) / 2f);
        g.scale(obj.getxScale(), obj.getyScale());
        g.drawImage(img, (int)(x / obj.getxScale()), (int) (y / obj.getyScale()), null);
        g.setTransform(old);
    }

    public static void renderRotatedScaledImage(Graphics2D g, AffineTransform old, int x, int y, GameObject obj, BufferedImage img, double angle, int rx, int ry) {
        g.rotate(angle, rx, ry);
        g.translate(-obj.getBounds().width * (obj.getxScale() - 1) / 2f, -obj.getBounds().height * (obj.getyScale() - 1) / 2f);
        g.scale(obj.getxScale(), obj.getyScale());
        g.drawImage(img, (int)(x / obj.getxScale()), (int) (y / obj.getyScale() - obj.getBounds().height * (obj.getyScale() - 1)), null);
        g.setTransform(old);
    }

    public static void renderRotatedScaledImageCenter(Graphics2D g, AffineTransform old, int x, int y, GameObject obj, BufferedImage img, double angle, int rx, int ry) {
        g.rotate(angle, rx, ry);
        g.translate(-obj.getBounds().width * (obj.getxScale() - 1) / 2f, -obj.getBounds().height * (obj.getyScale() - 1) / 2f);
        g.scale(obj.getxScale(), obj.getyScale());
        g.drawImage(img, (int)(x / obj.getxScale()), (int) (y / obj.getyScale()), null);
        g.setTransform(old);
    }

    public static void renderRotatedAnimation(Graphics2D g, AffineTransform old, double angle, int x, int y, Animation anim, int imgX, int imgY) {
        g.rotate(angle, x, y);
        anim.drawAnimation(g, imgX, imgY, 0);
        g.setTransform(old);
    }

    public static void renderRotatedScaledAnimation(Graphics2D g, AffineTransform old, double angle, int x, int y, Animation anim, int imgX, int imgY) {
        Rectangle getBounds = new Rectangle(x, y, 32 * 5,32 * 5);
        g.rotate(angle, x, y);
        g.translate(-getBounds.width * (anim.getxScale() - 1) / 2f, -getBounds.height * (anim.getyScale() - 1) / 2f);
        g.scale(anim.getxScale(), anim.getyScale());
        anim.drawAnimation(g, anim.getxScale() <= 1 ? (int)(imgX / anim.getxScale() + getBounds.width * (anim.getxScale() - 1)) : (int)(imgX / anim.getxScale() + getBounds.width * Math.cbrt(anim.getxScale() - 1) / 4f), anim.getyScale() <= 1 ? (int)(imgY / anim.getyScale() + getBounds.height * (anim.getyScale() - 1)) : (int)(imgY / anim.getyScale() + getBounds.height * Math.cbrt(anim.getyScale() - 1) / 4f), 0);
        g.setTransform(old);
    }

    public static void renderRotatedImage(Graphics2D g, AffineTransform old, double angle, int x, int y, BufferedImage img, int imgX, int imgY) {
        g.rotate(angle, x, y);
        g.drawImage(img, imgX, imgY, null);
        g.setTransform(old);
    }

    public static BufferedImage colorImage(BufferedImage img, Color color) {
        final BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = newImg.createGraphics();
        calculations = img;
        g.setColor(color);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (colorAt(x, y)) {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
        return newImg;
    }

    private static BufferedImage resizeImage(final BufferedImage image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }

    public static BufferedImage rotateImage(BufferedImage image, int angle) {
        final double rads = Math.toRadians(angle);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2f, h / 2f);
        at.rotate(rads,0, 0);
        at.translate(-image.getWidth() / 2f, -image.getHeight() / 2f);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return rotateOp.filter(image,rotatedImage);
    }

    public static BufferedImage flipped(BufferedImage image, boolean horizontal) {
        AffineTransform tx;
        if (horizontal) {
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(null), 0);
        } else {
            tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -image.getHeight(null));
        }
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    public static BufferedImage[] flipped(BufferedImage[] imgs, boolean horizontal) {
        BufferedImage[] toReturn = new BufferedImage[imgs.length];
        for (int i = 0; i < imgs.length; i++) {
            toReturn[i] = flipped(imgs[i], horizontal);
        }
        return toReturn;
    }

    private static BufferedImage calculations;
    private static boolean colorAt(int x, int y) {
        if (x >= calculations.getWidth() || y >= calculations.getHeight()) {
            return false;
        }
        return ((calculations.getRGB(x, y) >> 24) & 255) == 255;
    }
    public static BufferedImage outline(BufferedImage img) {
        BufferedImage outlined = new BufferedImage(img.getWidth() + 2, img.getHeight() + 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g3 = outlined.createGraphics();
        g3.drawImage(img, 1, 1, null);
        calculations = new BufferedImage(img.getWidth() + 2, img.getHeight() + 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = calculations.createGraphics();
        g2.drawImage(img, 1, 1, null);
        g2.dispose();
        Graphics2D g = outlined.createGraphics();
        g.setColor(Color.black);
        for (int x = 0; x < outlined.getWidth(); x++) {
            for (int y = 0; y < outlined.getHeight(); y++) {
                if (colorAt(x,y)) {
                    if (!colorAt(x, y + 1)) {
                        g.fillRect(x, y + 1, 1, 1);
                    }
                    if (!colorAt(x + 1, y)) {
                        g.fillRect(x + 1, y, 1, 1);
                    }
                    if (!colorAt(x, y - 1)) {
                        g.fillRect(x, y - 1, 1, 1);
                    }
                    if (!colorAt(x - 1, y)) {
                        g.fillRect(x - 1, y, 1, 1);
                    }
                    if (!colorAt(x + 1, y + 1)) {
                        g.fillRect(x + 1, y + 1, 1, 1);
                    }
                    if (!colorAt(x + 1, y - 1)) {
                        g.fillRect(x + 1, y - 1, 1, 1);
                    }
                    if (!colorAt(x - 1, y + 1)) {
                        g.fillRect(x - 1, y + 1, 1, 1);
                    }
                    if (!colorAt(x - 1, y - 1)) {
                        g.fillRect(x - 1, y - 1, 1, 1);
                    }
                }
            }
        }
        g.dispose();
        return outlined;
    }
}
