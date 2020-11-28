/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


// Заливка фигур происходит следующим образом. 
// Мы обращаемся к main методам классов каждой из фигур (Circle, Polygon, Elipse). 
// Метод main обращается к методу того же класса print, который в свою очередь обращается к методу create 
// в методе create мы вызываем метод класса DrawElement addPolygon/Elipse/Circle
// При добавлении фигур автоматически вызывается метод paintComponent класса DrawElement, который мы унааследовали от
// от класса Java - JComponent
// В DrawElement создается объект класса FillElement. Применяем к нему метод buttonCreate. В нем вызываем метод paint.
 


package com.mathpar.Graphic2D;

import java.awt.Paint;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import static com.mathpar.Graphic2D.DrawElement.g2;
import static com.mathpar.Graphic2D.DrawElement.pPoly;
import static com.mathpar.Graphic2D.DrawElement.polygon;


/**
 *
 * @author sasha
 */
public class FillElement extends JFrame{
    public static Graphics2D g2d;
    
      public FillElement() {
          super("Test frame");
     }
      

        public JButton btnCreate(){
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());
 
            JButton button1 = new JButton("Fill Polygon");
            
            //Graphics2D graph = (Graphics2D) g;
            //Polygon poly = (Polygon) p;
          
//            button1.addActionListener(new ActionListener() {
//                
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    System.out.print("Hello"+poly);
//                 paint(graph,poly, color); 
//                }
//            });
            panel.add(button1);
            getContentPane().add(panel);
            setPreferredSize(new Dimension(320, 100));
            return button1;
            
      }
        public void paint(Graphics2D g, Polygon p, Paint color){
   
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        g.setPaint(Color.gray);
                 g.setPaint(color);
                           System.out.println(g.toString());
                                g.fill(p);
                                 System.out.print("Bye"+p.toString());
                                //return shape;
        }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
         
     }
      
    
}
