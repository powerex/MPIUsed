/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import javax.swing.JPanel;

/**
 *
 * @author yuri
 */
public class ViewPanel extends JPanel {

    boolean DEBUG = true;

    private enum TransformationMode {

        none, rotating, moving, shaping
    };
    private TransformationMode transformationMode;
    private RayTracer rayTracer;
    private MemoryImageSource mis;
    Image canvasImage;

    public ViewPanel(RayTracer rayTracer) {
        super(true);
        setIgnoreRepaint(true);
        this.rayTracer = rayTracer;
        int width = RayTracer.width;
        int height = RayTracer.height;
        setSize(width, height);

        mis = new MemoryImageSource(width, height, new DirectColorModel(32, 0x00FF0000, 0x0000FF00, 0x000000FF),
                rayTracer.getImageArray(), 0, width);
        mis.setAnimated(true);
        mis.setFullBufferUpdates(true);
        canvasImage = createImage(mis);

            MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter()  {
              double oldX, oldY;
              @Override
            public void mouseDragged(MouseEvent e) {
                if (transformationMode != TransformationMode.none) {
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
                    transformationMode = TransformationMode.moving;
                } else if (e.isShiftDown()) {
                    transformationMode = TransformationMode.shaping;
                } else {
                    transformationMode = TransformationMode.rotating;
                }

                oldX = e.getX();
                oldY = e.getY();
                draw(0, 0);
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                transformationMode = TransformationMode.none;
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseMotionAdapter);
    }

    public void draw(double dx, double dy) {
        switch (transformationMode) {
            case rotating:
                double speed = 200;
                rayTracer.rotate(dy / speed, -dx / speed, 0);
                break;
            case moving:
                rayTracer.move(dx, dy, 0);
                break;
            case shaping:
                rayTracer.zoom(1 + (-dy / 100));
                break;
        }

        update();
    }
    private int frameCount = 0;
    private long t1;
    private double fps;
    private Graphics graphics;

    public void update() {
        if (graphics == null) {
            graphics = getGraphics();
            t1 = System.nanoTime();
        }

        if (graphics != null && canvasImage != null) {
            graphics.drawImage(canvasImage, 0, 0, null);
            if (DEBUG) {
                if (frameCount == 10) {
                    fps = (double) frameCount / (System.nanoTime() - t1) * 1e9;
                    frameCount = 0;
                    t1 = System.nanoTime();
                }
                frameCount++;            // FPS counter
                //FPS draw
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, 200, 12);
                graphics.setColor(Color.RED);
                graphics.drawString("fps: " + fps, 0, 10);
            }
            //Getting next frame;
            rayTracer.nextFrame();
            mis.newPixels();
        }
    }
}
