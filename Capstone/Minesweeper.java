import java.awt.*; 
import java.awt.event.*;     
import javax.swing.*;     

///I used to wrote a multiple classes program but when I check up online they are all in one class, so I decided to do so too  
/** 
 * 
 * 
 */  
public class Minesweeper extends JFrame implements ActionListener, Runnable, MouseListener 
{    
    private final int none = 0;  
    private final int mine = 1;  
    private final int check = 2;  
    private final int ttlmine = 10;     
    private final int grid = 50;     
    private final int size = 10;     
    private final int xpt = 20;    
    private final int ypt = 50;     
   
    private JButton[][] button;  
    private JLabel label;  
    private JLabel time;  
    private int[][] map;  
    
    //check if surrounding has mine
    private int[][] mv = { { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 }, { 1, 0 },  
            { 1, -1 }, { 0, -1 }, { -1, -1 } };  
  
    /** 
     * create mine 
     */  
    public void makeMine() 
    {  
        int i = 0, x, y;  
        for (; i < ttlmine;) 
        {  
            x = (int) (Math.random() * size);  
            y = (int) (Math.random() * size);  
            if (map[x][y] == none) 
            {  
                map[x][y] = mine;  
                i++; 
            }  
        }  
    }  
  
    /** 
     * make botton in frame 
     */  
    public void makeButton() 
    {  
        for (int i = 0; i < size; i++) 
        {  
            for (int j = 0; j < size; j++) 
            {  
                button[i][j] = new JButton();   
  
                button[i][j].addActionListener(this);  
                button[i][j].addMouseListener(this);  
  
                button[i][j].setName(i + "_" + j); 
                button[i][j].setBounds(j * grid + xpt, i * grid + ypt, grid, grid);  
                this.add(button[i][j]);  
            }  
        }  
    }  
  
    public void init() 
    {    
        time.setText("Time：0 sec");  
        time.setBounds(400, 20, 100, 30);  
        this.add(time);  
  
        makeMine();  
        makeButton();  
        this.setSize(550, 600);  
        this.setLocation(700, 100);  
 
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  
        this.setVisible(true);   
    }  
  
    public Minesweeper(String title) 
    {  
        super(title);    
        this.setLayout(null);              
        button = new JButton[size][size];  
        label = new JLabel();  
        time = new JLabel();  
        map = new int[size][size]; 
    }  
  
    public static void main(String[] args) 
    {  
        Minesweeper test = new Minesweeper("Minesweeper");  
        test.init();  
        test.run();  
    } 

    public void actionPerformed(ActionEvent e) 
    {    
        Object obj = e.getSource();  
        int x, y;  
        
        String[] tmp_str = ((JButton) obj).getName().split("_");  
        x = Integer.parseInt(tmp_str[0]);  
        y = Integer.parseInt(tmp_str[1]);  
  
        if (map[x][y] == mine) 
        {  
            System.out.println("You died");  
            Mine();  
            return;  
        }  
        click(x, y, 0);  
        checkSuccess();  
    }  
  
    /** 
     * determine if mines are found  
     */  
    private void checkSuccess() 
    {  
        int cnt = 0;  
        for (int i = 0; i < size; i++) 
        {  
            for (int j = 0; j < size; j++) 
            {  
                if (button[i][j].isEnabled())
                {  
                    cnt++;  
                }  
            }  
        }  
        if (cnt == ttlmine) 
        {  
            String tmp_str = time.getText();  
            tmp_str = tmp_str.replaceAll("[^0-9]", "");
            System.out.println("Congrats, you WIN! Time used: " + tmp_str + "secs");  
            Mine();  
        }  
    }  
  
    private int click(int x, int y, int z)
    {  
        map[x][y] = check;  
        int i, tx, ty, cnt = 0;  
        for (i = 0; i < 8; i++) 
        {  
            tx = x + mv[i][0];  
            ty = y + mv[i][1];  
            if (tx >= 0 && tx < size && ty >= 0 && ty < size) 
            {  
                if (map[tx][ty] == mine) 
                {  
                    cnt++;  
                } 
                else if (map[tx][ty] == none) 
                {  
                    ;  
                } 
                else if (map[tx][ty] == check) 
                {  
                    ;  
                }  
            }  
        }  
        if (cnt == 0) 
        {  
            for (i = 0; i < 8; i++)
            {  
                tx = x + mv[i][0];  
                ty = y + mv[i][1];  
                if (tx >= 0 && tx < size && ty >= 0 && ty < size && map[tx][ty] != check) 
                {  
                    click(tx, ty, z + 1);  
                }  
            }  
        } 
        else 
        {  
            button[x][y].setText(cnt + "");  
        }  
        button[x][y].setEnabled(false);  
        return cnt;  
    }  
  
   public void run() 
   {  
        int t = 0;  
        while (true) 
        {  
            try 
            {  
                Thread.sleep(1000);  
            } 
            catch (InterruptedException e) 
            {  
                e.printStackTrace();  
            }  
            t++;  
            time.setText("Time：" + t + " sec");  
        }  
        // showMine();  
    }  
  
    private void Mine() 
    {  
        for (int i = 0; i < size; i++) 
        {  
            for (int j = 0; j < size; j++) 
            {  
                if (map[i][j] == mine)
                {  
                    button[i][j].setText("#");  ;  
                }  
            }  
        }  
    }  
  
    @Override  
    public void mouseClicked(MouseEvent event) 
    { 
    }    
    @Override  
    public void mousePressed(MouseEvent event) {}    
    @Override  
    public void mouseReleased(MouseEvent event) {}    
    @Override  
    public void mouseEntered(MouseEvent event) {}    
    @Override  
    public void mouseExited(MouseEvent event) {}  
    
//   from stackoverflow but doesnt really work
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                   System.out.println("ActionListener invoked");
//                }
//             });
//             button.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mousePressed(MouseEvent e) {
//                   if (e.getButton() == MouseEvent.BUTTON3) {
//                      System.out.println("Right Button Pressed");
//                   }
//                }
//             });
// 
//             JPanel panel = new JPanel();
//             panel.add(button);
//             JOptionPane.showMessageDialog(null, panel);
//          }
//       });
//    }
// }
}  