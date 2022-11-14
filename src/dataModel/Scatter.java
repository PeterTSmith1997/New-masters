package dataModel;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Scatter extends JFrame {
 private List<Float> points = new ArrayList();

    public Scatter() {
        super("Scatterplot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        points.add(new Point2D.Float(1, 4));
        points.add(new Point2D.Float(2, 10));
        points.add(new Point2D.Float(3, 12));
        //points.add(new Point2D.Float(3, 10));
       // points.add(new Point2D.Float(4, 12));

        JPanel panel = new JPanel() {  
            public void paintComponent(Graphics g) {
                //g.translate(0, 0);
                for(Iterator i=points.iterator(); i.hasNext(); ) {
                    Point2D.Float pt = (Point2D.Float)i.next();
                    g.drawString(".", (int)pt.x, (int)pt.y);
                }

            }
        };

        setContentPane(panel);
        setBounds(10, 10, 200, 200);

        setVisible(true);       
    }
    public static void main(String[] args) {
        new Scatter();
    }
           
}