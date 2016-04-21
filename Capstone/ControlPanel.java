import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Write a description of class ControlPanel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ControlPanel extends JPanel
{
    private JButton colorButton;
    private JButton circle;
    private JButton square;
    private Color newColor;
    private JPanel squareColor;
    private DrawingPanel canvas;

    /**
     * Default constructor for objects of class ControlPanel
     */
    public ControlPanel(DrawingPanel canvas)
    {
        this.colorButton = new JButton("Color");
        this.circle = new JButton("Circle");
        this.square = new JButton("Square");
        
        this.add(this.colorButton);
        newColor = canvas.getColor();        
        this.add(this.circle);
        this.add(this.square);
        
        squareColor = new JPanel();
        newColor = canvas.getColor();
        squareColor.setBackground(newColor);
        squareColor.setPreferredSize(new Dimension(10, 10));
        this.add(squareColor);
        
        ClickListener listener = new ClickListener();
        this.colorButton.addActionListener(listener); 
        this.circle.addActionListener(listener); 
        this.square.addActionListener(listener); 
        this.canvas = canvas;                
    }
    
    public class ClickListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if(event.getActionCommand().equals("Color"))
            {
                canvas.pickColor();
                System.out.println("Select Color");
                newColor = canvas.getColor();
                squareColor.setBackground(newColor);
            }
            else if(event.getActionCommand().equals("Circle"))
            {
                canvas.addCircle();
            }
            else if(event.getActionCommand().equals("Square"))
            {
                canvas.addSquare();
            }
            canvas.repaint();
        }
    }
}
