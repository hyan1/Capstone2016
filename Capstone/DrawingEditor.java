import javax.swing.*;
import java.awt.*;
  
/**
 *
 */
public class DrawingEditor extends JPanel
{  
  private JButton squares[][];
  /**
   * 
   */
  public DrawingEditor()
  {
      this.setSize(400,400);
      this.setLayout(new GridLayout(10,10));
      squares = new JButton[10][10];
      buildButtons();      
  }
  
  private void buildButtons()
  {
      for(int i = 0; i < 10; i++)
      {
          for(int j = 0; j < 10; j++)
          {
              squares[i][j] = new JButton();
              squares[i][j].setSize(400,400);
              this.add(squares[i][j]);
          }
      }
  }
  
  public static void main(String[] args)
  {
      DrawingEditor d = new DrawingEditor();
      JFrame frame = new JFrame("Minesweeper");
      frame.add(d);
      frame.setSize(400,400);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
  }
}
