/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFileChooser;

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

    JButton openButton, saveButton;
    JFileChooser fc;
    public JPanel panel = new JPanel();
    public JSlider slider[];
    public JCheckBox check[];
    public JLabel label[];
    public JButton changeColor[];
    private int sliderCount = 5;
    public GM_RenderZBuffer render;
    public GM_MarchingCubes mcubes;
    public GM_Spline1D sp1dLinear, sp1dAkima, sp1dCubic;
    public GM_Spline2D sp2dBiLinear, sp2dBiCubic;
    public GM_Spline3Das2D sp3d;

    public RenderWindow(int width, int height) {
        setIgnoreRepaint(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        panel.setMinimumSize(new Dimension(400, 400));
        container.add(panel);

        Container rvContainer = new Container();
        rvContainer.setLayout(new BoxLayout(rvContainer, BoxLayout.Y_AXIS));
        container.add(rvContainer);

        Container rvsContainer = new Container();
        rvsContainer.setLayout(new BoxLayout(rvsContainer, BoxLayout.X_AXIS));
        rvContainer.add(rvsContainer);

        Container rvsContainerG = new Container();
        rvsContainerG.setLayout(new BoxLayout(rvsContainerG, BoxLayout.X_AXIS));
        rvContainer.add(rvsContainerG);

        slider = new JSlider[2 * sliderCount];
        check = new JCheckBox[2 * sliderCount];
        label = new JLabel[2 * sliderCount];
        changeColor = new JButton[2 * sliderCount];
        for (int i = 0; i < sliderCount; i++) {
            Container rvsvContainer = new Container();
            rvsvContainer.setLayout(new BoxLayout(rvsvContainer, BoxLayout.Y_AXIS));
            rvsContainer.add(rvsvContainer);
            slider[i] = new JSlider(JSlider.VERTICAL, 0, 255, i * (255 / (sliderCount - 1)));
            check[i] = new JCheckBox();
            check[i].setSelected(i < 1);
            check[i].addActionListener(new ButtonEventListener());
            label[i] = new JLabel(String.valueOf(slider[i].getValue()));
            slider[i].setAlignmentX(JComponent.LEFT_ALIGNMENT);
            check[i].setAlignmentX(JComponent.LEFT_ALIGNMENT);
            slider[i].setMinorTickSpacing(5);
            slider[i].setMajorTickSpacing(50);
            slider[i].setPaintTicks(true);
            slider[i].setPaintLabels(true);
            rvsvContainer.add(slider[i]);
            rvsvContainer.add(check[i]);
            rvsvContainer.add(label[i]);
            final JLabel curLabel = label[i];
            slider[i].addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    curLabel.setText(String.valueOf(((JSlider) e.getSource()).getValue()));
                }
            });

            final JButton curChangeColor = changeColor[i] = new JButton();
            curChangeColor.setBackground(Color.WHITE);
            curChangeColor.addActionListener(
                    new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            Color color = curChangeColor.getBackground();
                            color = JColorChooser.showDialog(RenderWindow.this, "Выберете цвет", color);
                            if (color == null) {
                                color = Color.lightGray;
                            }
                            curChangeColor.setBackground(color);
                            setNewLevelParams();
                        }
                    });
            rvsvContainer.add(curChangeColor);
        }

        for (int i = sliderCount; i < 2 * sliderCount; i++) {
            Container rvsvContainer = new Container();
            rvsvContainer.setLayout(new BoxLayout(rvsvContainer, BoxLayout.Y_AXIS));
            rvsContainerG.add(rvsvContainer);
            slider[i] = new JSlider(JSlider.VERTICAL, 0, 255, (i - sliderCount) * (255 / (sliderCount - 1)));
            check[i] = new JCheckBox();
            check[i].setSelected(false);
            check[i].addActionListener(new ButtonEventListener());
            label[i] = new JLabel(String.valueOf(slider[i].getValue()));
            slider[i].setAlignmentX(JComponent.LEFT_ALIGNMENT);
            check[i].setAlignmentX(JComponent.LEFT_ALIGNMENT);
            slider[i].setMinorTickSpacing(5);
            slider[i].setMajorTickSpacing(50);
            slider[i].setPaintTicks(true);
            slider[i].setPaintLabels(true);
            rvsvContainer.add(slider[i]);
            rvsvContainer.add(check[i]);
            rvsvContainer.add(label[i]);
            final JLabel curLabel = label[i];
            slider[i].addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    curLabel.setText(String.valueOf(((JSlider) e.getSource()).getValue()));
                }
            });
            final JButton curChangeColor = changeColor[i] = new JButton();
            curChangeColor.setBackground(Color.WHITE);
            curChangeColor.addActionListener(
                    new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            Color color = curChangeColor.getBackground();
                            color = JColorChooser.showDialog(RenderWindow.this, "Выберете цвет", color);
                            if (color == null) {
                                color = Color.lightGray;
                            }
                            curChangeColor.setBackground(color);
                            setNewLevelParams();
                        }
                    });
            rvsvContainer.add(curChangeColor);
        }

        Container rvbContainerG = new Container();
        rvbContainerG.setLayout(new BoxLayout(rvbContainerG, BoxLayout.X_AXIS));
        rvContainer.add(rvbContainerG);
        ButtonEventListener bl = new ButtonEventListener();
        //Create a file chooser
        fc = new JFileChooser();


        openButton = new JButton("Открыть вид");
        openButton.addActionListener(bl);
        rvbContainerG.add(openButton);

        saveButton = new JButton("Сохранить вид");
        saveButton.addActionListener(bl);
        rvbContainerG.add(saveButton);

        JSlider extra = new JSlider(JSlider.VERTICAL, 0, sliderCount * 2, 0);
        container.add(extra);
        extra.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                setNewLevelParams();
                int val = ((JSlider) e.getSource()).getValue();
                if (val > 0 && check[val - 1].isSelected()) {
                    mcubes.setLayerLevel(slider[val - 1].getValue(), new GM_Color(255, 0, 0, 0));
                }
                update();
            }
        });

        JSlider cutter = new JSlider(JSlider.VERTICAL, -300, 300, -300);
        container.add(cutter);
        cutter.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int val = ((JSlider) e.getSource()).getValue();
                mcubes.setCutLevel(val);
                update();
            }
        });

        setSize(width + 300, height);
        panel.setSize(width, height);
        render = new GM_RenderZBuffer(width, height);
        mcubes = new GM_MarchingCubes(render.s, GM_Figure.triangles);

        //Добавляем сплайны
        {
            double y[] = {-0, -1, -1.5, 1, -7, -1, -1, -1, -1, -5.3, 1.1};
//            sp1dLinear = new GM_Spline1D(render.s, GM_Figure.triangles, new GM_Color(Color.GREEN), y, GM_Spline1D.splineType.Linear);
//            sp1dAkima = new GM_Spline1D(render.s, GM_Figure.triangles, new GM_Color(Color.RED ), y, GM_Spline1D.splineType.Akima);
//            sp1dCubic = new GM_Spline1D(render.s, GM_Figure.triangles, new GM_Color(Color.BLUE), y, GM_Spline1D.splineType.Cubic);
        }
        {
            double f[][] = {
                {-00, -00, -00, 00, -7, -10, -1},
                {-00, 0.3, -00, 00, -7, -1.8, -1},
                {-00, -00, -00, 00, -4, -1, -1},
                {-00, -00, -00, 00, -7, -1, -1},
                {-00, -0.1, 0.5, 1, -0.2, -0.5, -1},
                {-00, -1, -1.5, 1, -7, -1, -1}
            };
//            sp2dBiLinear = new GM_Spline2D(render.s, GM_Figure.triangles, new GM_Color(Color.yellow), f, GM_Spline2D.spline2DType.BiLinear);
//            sp2dBiCubic = new GM_Spline2D(render.s, GM_Figure.triangles, new GM_Color(Color.RED), f, GM_Spline2D.spline2DType.BiCubic);
        }
        {
            double f[][][] = {{
                    {-00, -00, -00, 00, -7, -10, -1},
                    {-00, 0.3, -00, 00, -7, -1.8, -1},
                    {-00, -00, -00, 00, -4, -1, -1},
                    {-00, -00, -00, 00, -7, -1, -1},
                    {-00, -0.1, 0.5, 1, -0.2, -0.5, -1},
                    {-00, -1, -1.5, 1, -7, -1, -1}
                },
                {
                    {-00, -00, -00, 00, -7, -10, -1},
                    {-00, 10.3, -00, 00, -7, -1.8, -1},
                    {-00, -00, -00, 00, -4, -1, -1},
                    {-00, -00, -00, 00, -7, -1, -1},
                    {-00, -0.1, 0.5, 1, -0.2, -0.5, -1},
                    {-00, -1, -1.5, 1, -7, -1, -1}
                },
                {
                    {-00, -00, -00, 00, -7, -10, -1},
                    {-00, 0.3, -00, 00, -7, -1.8, -1},
                    {-00, -00, -00, 00, -4, -1, -1},
                    {-00, -00, -00, 00, -7, -1, -1},
                    {-00, -0.1, 0.5, 1, -0.2, -0.5, -1},
                    {-00, -1, -1.5, 1, -7, -1, -1}
                },
                {
                    {-00, -00, -00, 00, -7, -10, -1},
                    {-00, -10.3, -00, 00, -7, -1.8, -1},
                    {-00, -00, -00, 00, -4, -1, -1},
                    {-00, -00, -00, 00, -7, -1, -1},
                    {-00, -0.1, 0.5, 1, -0.2, -0.5, -1},
                    {-00, -1, -1.5, 1, -7, -1, -1}
                }};
//            sp3d = new GM_Spline3Das2D(render.s, GM_Figure.triangles, new GM_Color(Color.BLUE), f, 2.3);
        }

        //render.s.rotateAndMove(0, 0, 0, -5, 2, 0, 40);
        display();
    }

    class ButtonEventListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //Handle open button action.
            if (e.getSource() == openButton) {
                int returnVal = fc.showOpenDialog(RenderWindow.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        RandomAccessFile raf = new RandomAccessFile(file, "r");
                        for (int i = 0; i < sliderCount; i++) {
                            slider[i].setValue(raf.readInt());
                            check[i].setSelected(raf.readBoolean());
                            changeColor[i].setBackground(new Color(raf.readInt()));
                        }
                        for (int i = 0; i < 4; i++) {
                            for (int j = 0; j < 4; j++) {
                                mcubes.m[i][j] = raf.readDouble();
                                mcubes.am[i][j] = raf.readDouble();
                            }
                        }
                        mcubes.setMyMatrixesToChildren();
                        setNewLevelParams();
                    } catch (Exception ex) {
                    }
                }
                //Handle save button action.
            } else if (e.getSource() == saveButton) {
                int returnVal = fc.showSaveDialog(RenderWindow.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        RandomAccessFile raf = new RandomAccessFile(file, "rw");
                        for (int i = 0; i < sliderCount; i++) {
                            raf.writeInt(slider[i].getValue());
                            raf.writeBoolean(check[i].isSelected());
                            raf.writeInt(changeColor[i].getBackground().getRGB());
                        }
                        for (int i = 0; i < 4; i++) {
                            for (int j = 0; j < 4; j++) {
                                raf.writeDouble(mcubes.m[i][j]);
                                raf.writeDouble(mcubes.am[i][j]);
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            } else {
                setNewLevelParams();
            }
        }
    }

    public void setNewLevelParams() {
        for (GM_Figure tmp : mcubes.Children) {
            tmp.visible = false;
        }
        for (int i = 0; i < sliderCount; i++) {
            if (check[i].isSelected()) {
                mcubes.setLayerLevel(slider[i].getValue(), new GM_Color(changeColor[i].getBackground()));
            }
            if (check[i + sliderCount].isSelected()) {
                mcubes.setLayerLevel(slider[i + sliderCount].getValue() + 256, new GM_Color(changeColor[i + sliderCount].getBackground()));
            }
        }
        for (int i = 0; i < mcubes.Children.size(); i++) {
            if (!mcubes.Children.elementAt(i).visible) {
                mcubes.Children.remove(i);
            }
        }

        update();
    }

    public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
    }

    public final void display() {
        setVisible(true);
    }

    public void update() {
        for (int i = 0; i < sliderCount * 2; i++) {
            slider[i].repaint();
            check[i].repaint();
            label[i].repaint();
        }
        Graphics g2d = panel.getGraphics();
        render.draw(g2d);
        BufferStrategy bs = getBufferStrategy();
        if (bs != null && !bs.contentsLost()) {
            bs.show();
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public Graphics2D getDrawGraphics() {
        return (Graphics2D) getBufferStrategy().getDrawGraphics();
    }
}
