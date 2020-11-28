/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Core implements Runnable {

    private boolean running = true;
    private boolean rotating = false;
    private boolean moving = false;
    private boolean shaping = false;
    private RenderWindow renderWindow;

    public Core() {
        init();
    }

    public void init() {
        int width = 600, height = 600;
        renderWindow = new RenderWindow(width, height);
        MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {
            double oldX, oldY;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (rotating || moving || shaping) {
                    draw(e.getX() - oldX, e.getY() - oldY);
                    oldX = e.getX();
                    oldY = e.getY();
                }
            }
        };

        MouseAdapter mouseAdapter = new MouseAdapter() {
            double oldX, oldY;

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isControlDown()) {
                    moving = true;
                } else if (e.isShiftDown()) {
                    shaping = true;
                } else {
                    rotating = true;
                }

                oldX = e.getX();
                oldY = e.getY();
                draw(0, 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                rotating = false;
                moving = false;
                shaping = false;
            }
        };
        renderWindow.panel.addMouseListener(mouseAdapter);
        renderWindow.panel.addMouseMotionListener(mouseMotionAdapter);
        renderWindow.setNewLevelParams();
        run();
    }

    public void run() {
        renderWindow.createBufferStrategy(1);
        int i = 5;
        while (running) {
            renderWindow.update();
            running = false;
            i--;
            running = i > 0;
        }
    }

    public void draw(double dx, double dy) {
        if (rotating) {
            double speed = 200;
            renderWindow.render.s.rotateAndMove(dy / speed, -dx / speed, 0, 0, 0, 0);
        } else if (moving) {
            renderWindow.render.s.rotateAndMove(0, 0, 0, dx, dy, 0);
        } else if (shaping) {
            renderWindow.render.s.rotateAndMove(0, 0, 0, 0, 0, 0, -dy / 100);
        }

        renderWindow.update();
    }
}
