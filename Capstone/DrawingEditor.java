import javax.swing.*;
import java.awt.*;

  
/**
 *
 */
public class DrawingEditor extends JFrame
{
  private ControlPanel control;
  private DrawingPanel canvas;
  
  private int WINDOW_HEIGHT = 1000;
  private int WINDOW_LENGTH = 1000;
  /**
   * 
   */
  public DrawingEditor()
  {
      this.setTitle("Drawing Editor");
      this.setLayout(new BorderLayout());
      this.setSize(WINDOW_LENGTH, WINDOW_HEIGHT);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      canvas = new DrawingPanel();
      control = new ControlPanel(canvas);
        
      this.add(canvas, BorderLayout.CENTER);
      this.add(control, BorderLayout.SOUTH);
        
      this.setVisible(true);
  }
  
  public static void main(String[] args)
  {
      DrawingEditor editor = new DrawingEditor();
  }
}
