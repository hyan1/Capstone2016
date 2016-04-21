import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.geom.*;

public class DrawingPanel extends JPanel
{
    private Color color;
    private Dimension dimension;
    private ArrayList<Shape> shapeList;
    private Shape currentShape;
    
    private Point2D.Double startPoint = new Point2D.Double(250,250);
    private double startRadius = 60;
    

    public DrawingPanel()
    {
        this.setBackground(Color.WHITE);
        this.dimension = new Dimension(800, 800);
        this.setSize(dimension);
        
        this.color = Color.BLACK;
        
        this.addMouseListener(new TheMouseListener());
        this.addMouseMotionListener(new TheMouseListener());
        
        shapeList = new ArrayList<Shape>();
    }    
    
    class TheMouseListener implements MouseMotionListener, MouseListener
    {
        private boolean inside;
        public void mouseDragged(MouseEvent event)
        {
            currentShape.move(event.getX(), event.getY());
            repaint();
        }
        public void mouseMoved(MouseEvent event){}
        
        
        public void mousePressed(MouseEvent event)
        {
           inside = false;

            for(Shape shp: shapeList)
            {
                if (shp.isInside(new Point2D.Double(event.getX(), event.getY())))
                {
                    inside = true;
                    currentShape = shp;
                }            
            }   
            if(inside==false)
            {
                currentShape = null;
            }            
            repaint();
        }

        public void mouseReleased(MouseEvent event) {}
        public void mouseClicked(MouseEvent event) {}
        public void mouseEntered(MouseEvent event) {}
        public void mouseExited(MouseEvent event) {}
    }
        
        
    
    public Color getColor()
    {
        return this.color;
    }
    public Dimension getPreferredSize()
    {
        return dimension;
    }
    public void pickColor()
    {
        color = JColorChooser.showDialog(this, "Pick Color", color);
    }
    public void addCircle()
    {
        shapeList.add(new Circle(startPoint, startRadius, color));
    }
    
    public void addSquare()
    {
        shapeList.add(new Square(startPoint, startRadius, color));
    }
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        for(Shape shp: shapeList)
        {
            shp.draw(g2, currentShape == null? true: (!(currentShape == shp)));
        }
    }
}