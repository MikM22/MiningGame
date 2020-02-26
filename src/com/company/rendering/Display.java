package com.company.rendering;

import com.company.Main;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

public class Display {
    public static int width, height;
    public static boolean mouseDown;
    public static Point mousePos = new Point();
    public JFrame frame;
    public boolean transitioning;

    public Display(int w, int h, Main game) {
        frame = new JFrame("Top Down");
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                width = e.getComponent().getSize().width;
                height = e.getComponent().getSize().height;
            }
        });
        game.setFocusable(true);
        frame.add(game);
        frame.setPreferredSize(new Dimension(w,h));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setFullscreen(boolean fullscreen) {
        transitioning = true;
        if (fullscreen) {
//            frame.dispose();
//            frame.setUndecorated(true);
//            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//            frame.setVisible(true);
            width = 1920;
            height = 1080;
        } else {
//            frame.dispose();
//            frame.setUndecorated(false);
//            frame.pack();
//            frame.setExtendedState(JFrame.NORMAL);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
            width = 1000;
            height = 800;
        }
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setLocationRelativeTo(null);
        transitioning = false;
    }

    public static float horizontalAxis() {
        return (Main.keysDown.contains(KeyEvent.VK_D) ? 1 : 0) + (Main.keysDown.contains(KeyEvent.VK_A) ? -1 : 0);
    }

    public static float verticalAxis() {
        return (Main.keysDown.contains(KeyEvent.VK_S) ? 1 : 0) + (Main.keysDown.contains(KeyEvent.VK_W) ? -1 : 0);
    }
}
