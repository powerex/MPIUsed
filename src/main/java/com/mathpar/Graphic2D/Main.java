/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D;

import javax.swing.*;
import java.awt.EventQueue;

/**
 *Главный выполняющий класс проекта
 *
 */
public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {

                ToolBar frame = new ToolBar();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);


            }
        });
    }
}







