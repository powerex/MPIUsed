/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;

@SuppressWarnings("serial")
public class FuncTreeGraph extends JFrame {
    private String fName = "f.png";

    /**
     * Конструктор окна для показа дерева функции
     *
     * @param ff -- функция, дерево которой будет показываться
     */
    public FuncTreeGraph(F ff, Ring r) {
        super(ff.toString(r));
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // подготавливаем дерево функции для показа
        DefaultMutableTreeNode root = getFuncTree(ff, r);
        JTree tr = new JTree(root);
        getContentPane().add(new JScrollPane(tr), BorderLayout.CENTER);
        // показываем форму
        setVisible(true);
    }

    public FuncTreeGraph(F ff, Ring r, String fileName) {
        super(ff.toString(r));
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // подготавливаем дерево функции для показа
        DefaultMutableTreeNode root = getFuncTree(ff, r);
        JTree tr = new JTree(root);
        getContentPane().add(new JScrollPane(tr), BorderLayout.CENTER);
        // раскрываем все узлы
        for (int i = 0; i < tr.getRowCount(); i++) {
            tr.expandRow(i);
        }
        // показываем форму
        setVisible(true);
        // устанавливаем имя файла для сохранения
        this.fName = fileName;
        // сохраняем изображение с формы
        BufferedImage img = (BufferedImage) this.getContentPane().createImage(this.getWidth(), this.getHeight());
        Graphics2D g2d = img.createGraphics();
        this.paint(g2d);
        try {
            ImageIO.write(img, "png", new File(fName));
        } catch (IOException io) {
            // Who cares
        }
    }

    /**
     * Рекурсивный обход дерева функции и составление основы для показа в JTree
     *
     * @param f -- функция, которую надо обойти
     *
     * @return
     */
    private static DefaultMutableTreeNode getFuncTree(Element f, Ring r) {
        DefaultMutableTreeNode node = null;
        Element[] X = null;
        int start_ind = 0;

        if (f instanceof F) {
            node = new DefaultMutableTreeNode(F.FUNC_NAMES[((F) f).name]);
            X = ((F) f).X;
        } else if (f instanceof Fname) {
            node = new DefaultMutableTreeNode(((Fname) f).name);
            start_ind = 1;
            X = ((Fname) f).X;
            if (X == null) {
                return node;
            }
            if (X[0] != null) {
                node.add(new DefaultMutableTreeNode(X[0].toString(r)));
            }
        }

        DefaultMutableTreeNode child;
        Element curr_arg;

        for (int i = start_ind; i < X.length; i++) {
            curr_arg = X[i];
            if (curr_arg instanceof F || curr_arg instanceof Fname) {
                // F -- go to next level
                child = getFuncTree(curr_arg, r);
            } else {
                // Element -- leaf
                String s;
                if (curr_arg == null) {
                    s = "leaf_null";
                } else {
                    try {
                        s = curr_arg.toString(r);
                    } catch (Throwable t) {
                        s = "leaf_error " + t.getMessage();
                    }
                }
                child = new DefaultMutableTreeNode(s);
            }
            node.add(child);
        }
        return node;
    }

    /**
     * Создает окно, показывающее дерево функции
     *
     * @param f -- фукнция, которую надо показать
     */
    public static void show(final F f, final Ring r) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FuncTreeGraph funcTreeGraph = new FuncTreeGraph(f, r);
            }
        });
    }

    /**
     * Создает окно, показывающее дерево функции и сохраняет его как
     * PNG-изображение
     *
     * @param f -- фукнция, которую надо показать
     * @param fileName -- имя файла для сохранения изображения
     */
    public static void show(final F f, final Ring r, final String fileName) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FuncTreeGraph funcTreeGraph = new FuncTreeGraph(f, r, fileName);
            }
        });
    }
}
