import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

/**
 * This is the main class to run the Minesweeper program
 * It creates GUI( buttons, text areas, timer and mine counter.
 * @author Marcin Sznips
 * @version 1.0
 */
public class Minesweeper extends JFrame implements MouseListener, ActionListener
{
	private int rows = 10;
	private int columns = 10;
	private int mines = 10;
	private JLabel txtMinesLeft;
	private JLabel txtTime;
	private JTextArea txtTest;
	private JButton btnStart;
	private Square [][]buttons = new Square[rows][columns];
	private boolean started = false;
	private boolean finished = false;
	private int minesLeft = mines;
	private int fieldsLeft = rows * columns - minesLeft;
	private int currentTime = 0;
	private javax.swing.Timer timer;
	private JMenuItem itemNewGame;
	private JMenuItem itemFastest;
	private JMenuItem itemQuit;
	private JMenuItem itemHelp;
	private JMenuItem itemAbout;
	private int bestScore;
	private JPanel field;

	/**
	 * Constructor, where all data is initiated and all widgets
	 * are placed on the form.
	 */
	Minesweeper()
	{
		Container contPane = getContentPane();
		getContentPane().setLayout( new BorderLayout() );
		this.setTitle( "Minesweeper" );

		// create a panel with time and mines left counter and a start button
		// and put it into a GridLayout
		JPanel scoresPane = new JPanel( new GridLayout( 2, 3, 5, 3 ) );

			// JLabel "Mines Left"
			JLabel lblMinesLeft = new JLabel("Mines Left");
			lblMinesLeft.setHorizontalAlignment( JLabel.CENTER );
			scoresPane.add( lblMinesLeft );

			// JButton with smiley faces
			btnStart = new JButton( new ImageIcon("images/lach003.gif") );
			btnStart.setPreferredSize( new Dimension( 25, 25 ) );
			btnStart.addMouseListener( this );
			scoresPane.add( btnStart );

			// JLabel "Time"
			JLabel lblTime = new JLabel( "Time" );
			lblTime.setHorizontalAlignment( JLabel.CENTER );
			scoresPane.add( lblTime, 2 );
			
			// JLabel that shows number of mines left on a field
			txtMinesLeft = new JLabel( "" + minesLeft );
			txtMinesLeft.setHorizontalAlignment( JLabel.CENTER );
			txtMinesLeft.setForeground( Color.red );
			txtMinesLeft.setFont( new Font( "DialogInput", Font.BOLD, 18 ) );
			scoresPane.add( txtMinesLeft );

			scoresPane.add( new JLabel("") );

			// JLabel that shows elapsed time
			txtTime = new JLabel( "000" );
			txtTime.setHorizontalAlignment( JLabel.CENTER );
			txtTime.setForeground( Color.red );
			txtTime.setFont( new Font( "DialogInput", Font.BOLD, 18 ) );
			scoresPane.add( txtTime );

		contPane.add( scoresPane, BorderLayout.NORTH );
		CreateField();

		// create timer object to measure time
		timer = new javax.swing.Timer( 1000, this );

		// read the file with the highest score
		readFile();
	}

	/** 
	 * This method creates a minefield composed of buttons of
	 * type Square
	 * @return void
	 */
	private void CreateField()
	{
		super.setSize( (int)(21 * columns) - 11 , (int)(21.5 * rows) + 83 );

		field = new JPanel( new GridLayout( rows, columns, 1, 1 ) );
		int i, j;
		
		// double loop that crates a minefield made of buttons of type Square
		for( i = 0 ; i < rows ; i++ )
			for( j = 0 ; j < columns ; j++)
			{
				buttons[i][j] = new Square( i, j );
				buttons[i][j].addMouseListener( this );
				field.add( buttons[i][j] );
			}		

		getContentPane().add( field, BorderLayout.CENTER );
	}

	/**
	 * This method resets the minefield every time a new game is started
	 */
	private void resetField()
	{
		/*field.removeAll();
		field.setLayout( new GridLayout( rows, columns, 1, 1 ) );	*/

		for( int i = 0 ; i < rows ; i++ )
			for( int j = 0 ; j < columns ; j++)
			{
				/*buttons[i][j] = new Square( i, j );
				buttons[i][j].addMouseListener( this );
				field.add( buttons[i][j] );*/
				buttons[i][j].reset();
			}		
		
		// reset all the flags and the timer
		started = false;
		finished = false;
		currentTime = 0;
		minesLeft = mines;
		fieldsLeft = rows * columns - minesLeft;
		txtMinesLeft.setText( "" + minesLeft );
		txtTime.setText( "00" + currentTime );
		btnStart.setIcon( new ImageIcon("images/lach003.gif") );

	}


	/** 
	 * This method generates mines each time a game is started
	 * and marks for each square number of neighboring mines
	 */
	private void GenerateMines()
	{
		Random rInt = new Random();
		int rCol, rRow;
		int i, j;

		// place mines in random order
		for( i = 0 ; i < mines ; i++ )
		{
			rCol = rInt.nextInt( rows );
			rRow = rInt.nextInt( columns );
			// don't put more than one mine on one square
			if( !buttons[ rCol ][ rRow ].isMine() && !buttons[ rCol ][ rRow ].isSelected() )
				buttons[ rCol ][ rRow ].setMine();
			else
				i--;
		}

		// mark number of neighboring mines on each square
		for( i = 0 ; i < rows ; i++ )
			for( j = 0 ; j < columns ; j++)
			{
				if( buttons[i][j].isMine() )
				{
					if( i - 1 >= 0 && j - 1 >= 0 )	// upper left square
						buttons[i - 1][j - 1].addNeighbors( 1 );
					if( i - 1 >= 0 && j >= 0 )	// upper middle square
						buttons[i - 1][j].addNeighbors( 1 );
					if( i - 1 >= 0 && j + 1 < columns )	// upper right square
						buttons[i - 1][j + 1].addNeighbors( 1 );
					if( i >= 0 && j - 1 >= 0 )	// middle left square
						buttons[i][j - 1].addNeighbors( 1 );
					if( i >= 0 && j + 1 < columns )	// middle right square
						buttons[i][j + 1].addNeighbors( 1 );
					if( i + 1 < rows && j - 1 >= 0 )	// lower left square
						buttons[i + 1][j - 1].addNeighbors( 1 );
					if( i + 1 < rows && j >= 0 )	// lower middle square
						buttons[i + 1][j].addNeighbors( 1 );
					if( i + 1 < rows && j + 1 < columns )	// lower left square
						buttons[i + 1][j + 1].addNeighbors( 1 );
				}
			}
  	}

	
	/** Creates a Menu Bar with JMenu and JMenuItems
	 *  @return JMenuBar
	 */
	private JMenuBar myMenu()
	{
		JMenuBar menuBar = new JMenuBar();

		JMenu menuGame = new JMenu( "Game" );
			itemNewGame = new JMenuItem( "New Game" );
			itemNewGame.addActionListener( this );
			itemFastest = new JMenuItem( "Fastest Game" );
			itemFastest.addActionListener( this );
			itemQuit = new JMenuItem( "Quit" );
			itemQuit.addActionListener( this );
			menuGame.add( itemNewGame );
			menuGame.add( itemFastest );
			menuGame.add( itemQuit );			

		JMenu menuHelp = new JMenu( "Help" );
			itemHelp = new JMenuItem( "Instructions" );
			itemHelp.addActionListener( this );
			itemAbout = new JMenuItem( "About" );
			itemAbout.addActionListener( this );
			menuHelp.add( itemHelp );
			menuHelp.add( itemAbout );

		menuBar.add( menuGame );		
		menuBar.add( menuHelp );

		return menuBar;
	}


	/**
	 * Write the highest score to a file
	 */
	private void writeFile()
	{
		String timeTxt = "" + currentTime;
		try
		{
			// create a file if it does not exist
			File myFile = new File( ".", "scores.txt" );
			PrintWriter output = new PrintWriter( new FileWriter( myFile ) );

			// write the highest score to the file
			output.write( timeTxt );
			output.close();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Reads the highest score from the file
	 * @return boolean
	 */
	private boolean readFile()
	{
		String text;
		Integer iText = new Integer(0);

		try
		{
			File myFile = new File( ".", "scores.txt" );

			// check if the file exist
			if( !myFile.exists() )
			{
				bestScore = -1;
				return false;
			}

			// if it exists read the highest score from it
			BufferedReader in = new BufferedReader( new FileReader( myFile ) );
			text = in.readLine();
			bestScore = iText.parseInt( text );
			in.close();
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
		}

		return true;
	}

	/**
	 * This method is invoked when the game is finished whether the player
	 * lost or won. The squares on the minefield are adequately updated
	 * showing where the mines were and where the player 'flagged' the 
	 * square incorrectly.
	 */
	private void endGame( boolean lost )
	{
		// stop the time counter
		ImageIcon winIcon = new ImageIcon( "images/flag.gif" );
		ImageIcon lostIcon = new ImageIcon( "images/mine_bad.gif" );
		finished = true;
		timer.stop();
		
		if( lost )
			btnStart.setIcon( new ImageIcon( "images/traurig007.gif" ) );

		// show all mines and disable all buttons
		for( int i = 0 ; i < rows ; i++ )
			for( int j = 0 ; j < columns ; j++ )
			{
				// when the game is lost
				if( lost )
				{
					// show incorectly marked squares
					if( buttons[i][j].getFlag() == 1 )
					{
						buttons[i][j].setIcon( new ImageIcon( "images/mine_bad.gif" ) );
						buttons[i][j].setDisabledIcon( new ImageIcon("images/mine_bad.gif") );
					}

					// show all mines
					if( buttons[i][j].isMine() )
					{
						buttons[i][j].setIcon( new ImageIcon("images/mine.gif") );
						buttons[i][j].setDisabledIcon( new ImageIcon("images/mine.gif") );
						buttons[i][j].setEnabled( false );
					}
					else
						buttons[i][j].setEnabled( false );
				}

				// if the game is won
				else
				{
					// mark all squares with mine with a flag
					if( buttons[i][j].isMine() )
					{
						buttons[i][j].setIcon( winIcon );
						buttons[i][j].setDisabledIcon( winIcon );
						buttons[i][j].setEnabled( false );
					}
					else
						buttons[i][j].setEnabled( false );
				}
			}
	}

	/**
	 * This method is used to show all adjecent fields that have
	 * zero number of neighbors
	 */
	private void showZeros( Square theSquare )
	{
		int posX = theSquare.getXpos();
		int posY = theSquare.getYpos();

		checkForZeros( posX, posY );
			return;
	}

	/**
	 * Recursively goes through all squares having zero neighbors
	 * and marks them as pressed
	 */
	private void checkForZeros( int x, int y )
	{
		if( x < rows && y < columns && x >= 0 && y >= 0 && !buttons[x][y].isPressed() )
		{
			buttons[x][y].leftClick();
			fieldsLeft--;

			if( buttons[x][y].getNeighors() == 0 )
			{
				checkForZeros( x - 1, y - 1 );
				checkForZeros( x	, y - 1 );
				checkForZeros( x + 1, y - 1 );
				checkForZeros( x - 1, y		);
				checkForZeros( x + 1, y		);
				checkForZeros( x - 1, y + 1 );
				checkForZeros( x	, y + 1 );
				checkForZeros( x + 1, y + 1 );
			}
		}
	}


	/**
	 * Event handler; changes an icon on the start button 
	 * when the left mouse button is pressed
	 */
	public void mousePressed(MouseEvent e)
	{
		btnStart.setIcon( new ImageIcon("images/staun009.gif") );
	}

	/**
	 * Event handler; changes an icon on the start button 
	 * when the left mouse button is released
	 */
	public void mouseReleased(MouseEvent e)
	{
		btnStart.setIcon( new ImageIcon("images/lach003.gif") );
	}

	public void mouseEntered(MouseEvent e)
	{}

	public void mouseExited(MouseEvent e)
	{}

	/**
	 * Event handler; Handles left and right clicks of a mouse
	 */
	public void mouseClicked(MouseEvent e)
	{
		int button = e.getButton();

		// if start button was clicked stop the timer and reset the minefield
		if( e.getSource() == (JButton)btnStart )
		{
			timer.stop();
			rows = 10;
			columns = 10;
			resetField();
			return;
		}

		Square sq = (Square)e.getSource();

		// if left mouse button was clicked
		if( button == 1 && !finished )
		{
			// if in the clicked field is mine and it is not flagged end game
			if( sq.isMine() && sq.getFlag() != 1 )
			{
				sq.setSelected( false );
				endGame( true );				
				return;
			}

			// if we click the first square after starting the game
			if( !started )
			{
				GenerateMines();
				started = true;
				timer.start();
			}

			// not clicked yet and not flagged square is clicked
			if( !sq.isPressed() && sq.getFlag() != 1 )
			{			
				if( sq.getNeighors() == 0 )
					showZeros( sq );
				else
					fieldsLeft--;
			}
			// notify a square that it was clicked
			sq.leftClick();
			
		}
		// if right mouse button was clicked
		else if( button == 3 )
		{
			minesLeft += sq.rightClick();
		}

		// when there is no more empty squares to click the game is won
		if( fieldsLeft == 0 )
		{
			finished = true;
			btnStart.setIcon( new ImageIcon( "images/cool001.gif" ) );
			timer.stop();
			if( currentTime < bestScore )
			{
				bestScore = currentTime;
				writeFile();
			}
			endGame( false );
		}

		txtMinesLeft.setText( "" + minesLeft );
	}

	/**
	 * Event handler; handles menu selections
	 */
	public void actionPerformed( ActionEvent e )
	{
		Object oSrc = e.getSource();
		String sHelp = "Left click: uncovers a field (or multiple fields) \n";
		sHelp += "if it is not marked with a flag. \n";
		sHelp += "Right Click: marks a field with a flag or a '?' or clears any flags.\n";
		sHelp += "You win if you uncover all the fields that have no mines in it.\n";
		sHelp += "Numbers show how many neighboring fields have mines in them.\n";
		String sAuthor = " Author: Marcin Sznips \n Date: 1/29/04";

		if( oSrc == itemAbout )
			JOptionPane.showMessageDialog(itemAbout, sAuthor, "About", JOptionPane.INFORMATION_MESSAGE );

		else if( oSrc == itemHelp )
			JOptionPane.showMessageDialog( itemHelp, sHelp, "Help", JOptionPane.INFORMATION_MESSAGE );

		else if( oSrc == itemQuit )
			System.exit( 0 );

		else if( oSrc == itemNewGame )
		{
			timer.stop();
			resetField();
			return;
		}
			
		else if( oSrc == itemFastest )
		{
			if( bestScore == -1 )
				JOptionPane.showMessageDialog( itemFastest, " There is no fastest time yet ", "The Best Time", JOptionPane.INFORMATION_MESSAGE );
			else
				JOptionPane.showMessageDialog( itemFastest, "Fastest time is: " + bestScore, "The Best Time", JOptionPane.INFORMATION_MESSAGE );
		}

		// timer event fires every 1 second, just increases our clock by one
		else if( oSrc == timer )
		{
			currentTime++;

			if( currentTime < 10 )
				txtTime.setText( "00" + currentTime );
			else if( currentTime < 100 )
				txtTime.setText( "0" + currentTime );
			else
				txtTime.setText( "" + currentTime );
		}
	}

	/**
	 * Main method to start the game
	 */
	public static void main( String args[] )
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		Minesweeper msw = new Minesweeper();
		msw.setJMenuBar( msw.myMenu() );
		msw.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		msw.setVisible( true );
		msw.setResizable( false );
	}

}