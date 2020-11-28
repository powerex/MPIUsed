/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

import javax.swing.JFrame;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author
 * @version 1.0
 */
public class RenderWindow extends JFrame {

    private ViewPanel panel;

    public RenderWindow(RayTracer rayTracer) {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(RayTracer.width + 11, RayTracer.height + 31);
        setVisible(true);

        panel = new ViewPanel(rayTracer);
        add(panel);
    }

    public void windowClosing() {
        dispose();
        System.exit(0);
    }

    void update() {
        panel.update();
    }
}
