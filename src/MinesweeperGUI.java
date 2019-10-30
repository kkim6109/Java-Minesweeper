/*
 * MinesweeperGUI
 * Chapter 11 Exercise 2
 * NOTE: player should size window larger before starting play
 * Lawrenceville Press
 * June 24, 2005
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;        
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Timer;

public class MinesweeperGUI implements ActionListener {
	// declare variables for board size
	private static int rows = 9;
	private static int cols = 9;
	private static int numMines = 10;

	// create frame and panels
	private JFrame frame;
	private JPanel contentPane;
	private JPanel difficulties;
	private JPanel connectPanes;
	private JPanel lowerPane;
	private JButton[][] board;
	private JLabel winnerMessage;
	private JLabel timer;
	private JLabel mineCount;
	private Minesweeper MSGame = new Minesweeper();
	private boolean first = true;
	private ArrayList<Point> checked;
	private boolean stopGame = false;
	private ArrayList<ImageIcon> images;
	private ArrayList<Point> flagged;
	private JButton[] difficulty;
	private JButton face;
	private int[][] numMinesBoard;
	private ArrayList<ImageIcon> faces;

	// sets the height and width of the gui
	private static int height = 700;
	private static int width = 700;
	private static MinesweeperGUI playMS;

	// declares timer
	private Timer timed;
	private JLabel diffLabel;
	private int seconds;

	public MinesweeperGUI() {
		// starts timer, runs it
		seconds = 0;
		mineCount = new JLabel(MSGame.getNumMines() + "", JLabel.CENTER);
		mineCount.setFont(new Font(mineCount.getFont().getFontName(), Font.PLAIN, 30));
		mineCount.setForeground(Color.blue);
		mineCount.setBackground(Color.black);
		mineCount.setOpaque(true);
		timer = new JLabel("000", JLabel.CENTER);
		timed = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String time;
				seconds++;
				time = Integer.toString(seconds);
				while (time.length() != 3)
				{
					time = "0" + time;
				}
				timer.setText(time);
			}
		});
		timer.setFont(new Font(timer.getFont().getFontName(), Font.PLAIN, 30));
		timer.setForeground(Color.red);
		timer.setBackground(Color.black);
		timer.setOpaque(true);

		// creates the board with the correct rows and cols
		board = new JButton[rows][cols];
		numMinesBoard = new int[rows][cols]; 
		difficulty = new JButton[3];
		flagged = new ArrayList<Point>();
		images = new ArrayList<ImageIcon>();
		checked = new ArrayList<Point>();
		images.add(new ImageIcon("MINE.PNG"));
		for (int i = 1; i <= 8; i++)
		{
			images.add(new ImageIcon(i + "Mines.PNG"));
		}
		images.add(new ImageIcon("FLAG.PNG"));
		images.add(new ImageIcon("tile.PNG"));

		// declares the images for the face
		faces = new ArrayList<ImageIcon>();
		faces.add(new ImageIcon("Happy.PNG"));
		faces.add(new ImageIcon("Clicking.PNG"));
		faces.add(new ImageIcon("Lose.PNG"));
		faces.add(new ImageIcon("Win.PNG"));

		/* Create and set up the frame */
		frame = new JFrame("MinesweeperGUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Create a content pane with a GridLayout and empty borders */
		connectPanes = new JPanel();
		connectPanes.setLayout(new BorderLayout());
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(0, cols, 0, 0));
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.setBackground(new Color(204, 235, 255));
		contentPane.setPreferredSize(new Dimension(width, height));
		difficulties = new JPanel();
		difficulties.setLayout(new GridLayout(11, 1, 0, 0));
		difficulties.setBackground(new Color(204, 235, 255));
		difficulties.setPreferredSize(new Dimension(60, height));
		lowerPane = new JPanel();
		lowerPane.setLayout(new BorderLayout());
		lowerPane.setPreferredSize(new Dimension(width, 50));
		lowerPane.setBackground(new Color(204, 235, 255));

		// declares the difficulty buttons
		diffLabel = new JLabel("Difficulties", JLabel.CENTER);
		difficulties.add(timer);
		difficulties.add(mineCount);
		difficulties.add(diffLabel);
		difficulty[0] = new JButton("1");
		difficulty[1] = new JButton("2");
		difficulty[2] = new JButton("3");
		for(int i = 0; i < 3; i++)
		{
			difficulty[i].setActionCommand("difficulty" + i);
			difficulty[i].addActionListener(this);
			difficulties.add(difficulty[i]);
		}   
		// declares the face button
		face = new JButton("");
		face.setActionCommand("reset");
		face.addActionListener(this);
		face.setIcon(faces.get(0));
		difficulties.add(face);

		/* Create board and add buttons */
		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				final int r = row, c = col;
				board[r][c] = new JButton("");
				board[r][c].setIcon(images.get(10));
				board[r][c].setActionCommand("clicked" + row + " " + col);
				board[r][c].addActionListener(this);
				board[r][c].addMouseListener(new MouseAdapter(){
					public void mouseReleased(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON3) {
							if (!checked.contains(new Point(r, c)) && !stopGame)
							{
								if (!flagged.contains(new Point(r, c)))
								{
									fixImages();
									flagged.add(new Point(r, c));
									board[r][c].setIcon(images.get(9));
								}
								else
								{
									flagged.remove(new Point(r, c));
									board[r][c].setIcon(images.get(10));
								}
							}
							String display = (MSGame.getNumMines() - flagged.size()) + "";
							while (display.length() != 2)
							{
								display = "0" + display;
							}
							mineCount.setText(display);
						}
						else if (e.getButton() == MouseEvent.BUTTON1 && !stopGame)
						{
							face.setIcon(faces.get(0));
							String display = (MSGame.getNumMines() - flagged.size()) + "";
							while (display.length() != 2)
							{
								display = "0" + display;
							}
							mineCount.setText(display);
						}
					}

					public void mousePressed(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1 && !stopGame)
						{
							face.setIcon(faces.get(1));
							String display = (MSGame.getNumMines() - flagged.size()) + "";
							while (display.length() != 2)
							{
								display = "0" + display;
							}
							mineCount.setText(display);
						}
						if (e.getButton() == MouseEvent.BUTTON2 && !stopGame)
						{
							clickAround(r, c);
						}
					}
				});
				contentPane.add(board[r][c]);
			}
		}

		// inserts winner message label
		winnerMessage = new JLabel("");
		lowerPane.add(winnerMessage, BorderLayout.CENTER);

		connectPanes.add(contentPane, BorderLayout.CENTER);
		connectPanes.add(difficulties, BorderLayout.WEST);
		connectPanes.add(lowerPane, BorderLayout.SOUTH);

		/* Add content pane to frame */
		frame.setContentPane(connectPanes);

		/* Size and then display the frame. */
		frame.pack();
		frame.setVisible(true);

		fixImages();
	}

	public void clickAround(int row, int col) {
		if (checked.contains(new Point(row, col)) && checkAdjacent2(row, col) == numMinesBoard[row][col]) {
			int iMin = row - 1, iMax = row + 1;
			int jMin = col - 1, jMax = col + 1;
			if(iMin < 0) {
				iMin = 0;
			}
			if (iMax > rows - 1) {
				iMax = rows - 1;
			}
			if(jMin < 0) {
				jMin = 0;
			}
			if (jMax > cols - 1) {
				jMax = cols - 1;
			}
			for(int i = iMin; i <= iMax; i++) {
				for(int j = jMin; j <= jMax; j++) {
					if (!flagged.contains(new Point(i, j)) && !checked.contains(new Point(i, j)))
					{
						movement(i, j);
					}
				}
			}
		}
	}

	public int checkAdjacent2(int row, int col) {
		int count = 0;
		int iMin = row - 1, iMax = row + 1;
		int jMin = col - 1, jMax = col + 1;
		if(iMin < 0) {
			iMin = 0;
		}
		if (iMax > rows - 1) {
			iMax = rows - 1;
		}
		if(jMin < 0) {
			jMin = 0;
		}
		if (jMax > cols - 1) {
			jMax = cols - 1;
		}
		for(int i = iMin; i <= iMax; i++) {
			for(int j = jMin; j <= jMax; j++) {
				if(flagged.contains(new Point(i, j))) {
					count++;
				}
			}
		}

		return count;
	}


	/**
	 * Handle a button click
	 * pre: none
	 * post: A turn has been taken.
	 */
	public void actionPerformed(ActionEvent event) {
		String eventName = event.getActionCommand();

		if (eventName.equals("reset"))
		{
			reset();
		}
		else if (eventName.contains("clicked"))
		{
			for (int row = 0; row < rows; row++)
			{
				for (int col = 0; col < cols; col++)
				{
					if (eventName.equals("clicked" + row + " " + col) && !flagged.contains(new Point(row, col)))
					{
						movement(row, col);
					}
				}
			}
		}
		else if (eventName.contains("difficulty"))
		{
			for (int i = 0; i < 3; i++)
			{
				if (eventName.equals("difficulty" + i))
				{
					changeDifficulty(i);
				}
			}
		}
	}

	// resets the game
	public void reset()
	{
		playMS.frame.setVisible(false);
		playMS = new MinesweeperGUI();
	}

	// changes the difficulty
	public void changeDifficulty(int diff)
	{
		if (diff == 0)
		{
			rows = cols = 9;
			numMines = 10;
			width = 700;
		}
		else if (diff == 1)
		{
			rows = cols = 16;
			numMines = 40;
			width = 700;
		}
		else
		{
			rows = 16;
			cols = 30;
			numMines = 99;
			width = 1313;
		}
		MSGame.changeDifficulty(diff);
		playMS.frame.setVisible(false);
		playMS = new MinesweeperGUI();
	}

	// adds spaces to the board
	public void addSpace(int row, int col, int amt)
	{
		numMinesBoard[row][col] = amt;
		board[row][col].setIcon(null);
		flagged.remove(new Point(row, col));
		if (amt > 0)
		{
			board[row][col].setBackground(Color.lightGray);
			board[row][col].setIcon(images.get(amt));
		}
		else if (amt == 0)
		{
			board[row][col].setBackground(Color.lightGray);
		}
	}

	// reveals spaces that are 0 if clicked next to
	public void makeAuto(int row, int col) {
		if (!checked.contains(new Point(row, col)))
		{
			checked.add(new Point(row, col));
			int iMin = row - 1, iMax = row + 1;
			int jMin = col - 1, jMax = col + 1;
			if(iMin < 0) {
				iMin = 0;
			}
			if (iMax > rows - 1) {
				iMax = rows - 1;
			}
			if(jMin < 0) {
				jMin = 0;
			}
			if (jMax > cols - 1) {
				jMax = cols - 1;
			}
			for(int i = iMin; i <= iMax; i++) {
				for(int j = jMin; j <= jMax; j++) {
					if (!checked.contains(new Point(i, j)))
					{
						int move = MSGame.makeAMove(i, j);
						if (move != -1)
						{
							addSpace(i, j, move);
							if (move == 0)
							{
								makeAuto(i, j);
							}
							else
							{
								checked.add(new Point(i, j));
							}
						}
					}
				}
			}
		}
	}

	// moves the bomb
	public void moveBomb(int row, int col)
	{
		int iMin = row - 1, iMax = row + 1;
		int jMin = col - 1, jMax = col + 1;
		if(iMin < 0) {
			iMin = 0;
		}
		if (iMax > rows - 1) {
			iMax = rows - 1;
		}
		if(jMin < 0) {
			jMin = 0;
		}
		if (jMax > cols - 1) {
			jMax = cols - 1;
		}
		for(int i = iMin; i <= iMax; i++) {
			for(int j = jMin; j <= jMax; j++) {
				if (MSGame.checkForBomb(i, j))
				{
					MSGame.moveBomb(i, j);
				}
			}
		}
		movement(row, col);
	}

	// allows player to make moves
	public void movement(int row, int col)
	{
		fixImages();
		if (!stopGame)
		{
			int move = MSGame.makeAMove(row, col);
			if (first)
			{
				timed.start();				
			}
			if (move != 0 && first)
			{
				first = false;
				moveBomb(row, col);
			}
			else if (move == -1)
			{
				showBombs();
				board[row][col].setBackground(Color.red);
				board[row][col].setIcon(images.get(0));
				winnerMessage.setText("YOU LOSE"); // display YOU LOSE
				face.setIcon(faces.get(2));
				stopGame = true;
				timed.stop();
			}
			else if (move == 0)
			{
				addSpace(row, col, move);
				makeAuto(row, col);
			}
			else
			{
				addSpace(row, col, move);
			}
			if (!checked.contains(new Point(row, col)))
			{
				checked.add(new Point(row, col));
			}
			if (checkForWin())
			{
				winnerMessage.setText("YOU WIN!!!"); // display YOU WIN
				face.setIcon(faces.get(3));
				stopGame = true;
				timed.stop();
			}
			first = false;
		}
	}

	// fixes image scaling if the window is resized
	public void fixImages()
	{
		for(ImageIcon img : images)
		{
			img.setImage(scaleImage(img.getImage(), board[0][0].bounds().width, board[0][0].bounds().height));
		}
		for(ImageIcon img : faces)
		{
			img.setImage(scaleImage(img.getImage(), face.bounds().width, face.bounds().height));
		}
	}

	public Image scaleImage(Image srcImg, int w, int h){
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return img;
	}

	// reveals all bombs
	public void showBombs()
	{
		ArrayList<Point> bombs = MSGame.returnBombs();
		for (Point mine : bombs)
		{
			board[mine.x][mine.y].setIcon(images.get(0));
			board[mine.x][mine.y].setBackground(Color.lightGray);
		}
	}

	/**
	 * Create and show the GUI.
	 */
	private static void runGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);

		playMS = new MinesweeperGUI();
	}

	// checks for a winning situation
	public boolean checkForWin() {
		if (checked.size() >= ((rows * cols) - numMines)){
			return true;
		}
		return false;
	}

	// runs the GUI
	public static void main(String[] args) {
		/* Methods that create and show a GUI should be 
           run from an event-dispatching thread */
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});
	}
}




