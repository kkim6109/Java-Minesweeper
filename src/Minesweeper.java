import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;


public class Minesweeper {

	private String minesweeperBoard[][];
	private static int numMines = 10;
	private static int rows = 9;
	private static int cols = 9;
	private ArrayList<Point> checked;
	ArrayList<Point> bombs;

	// sets up the board, bombs and spaces
	public Minesweeper() {
		bombs = new ArrayList<Point>();
		minesweeperBoard = new String[rows][cols];
		setupBoard();
		checked = new ArrayList<Point>();
	}
	
	public int getNumMines() {
		return numMines;
	}

	// changes the difficulty
	public void changeDifficulty(int diff)
	{
		if (diff == 0)
		{
			rows = cols = 9;
			numMines = 10;
		}
		else if (diff == 1)
		{
			rows = cols = 16;
			numMines = 40;
		}
		else
		{
			rows = 16;
			cols = 30;
			numMines = 99;
		}
	}

	// places bombs in the board
	public void setupBoard()
	{
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				minesweeperBoard[i][j] = "";
			}
		}
		for (int i = 0; i < numMines; i++)
		{
			int ranRow = (int)(Math.random() * rows);
			int ranCol = (int)(Math.random() * cols);
			while (minesweeperBoard[ranRow][ranCol].equals("bomb"))
			{
				ranRow = (int)(Math.random() * rows);
				ranCol = (int)(Math.random() * cols);
			}
			minesweeperBoard[ranRow][ranCol] = "bomb";
			bombs.add(new Point(ranRow, ranCol));
		}
	}

	// returns number of bombs
	public ArrayList<Point> returnBombs()
	{
		return bombs;
	}

	// once a space is clicked, a move is made
	public int makeAMove(int row, int col) {
		if(checkForBomb(row, col)) {
			return -1; // lose
		}
		else {
			return(checkAdjacent(row, col));
		}
	}

	// moves a bomb
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
		int ranRow = (int)(Math.random() * rows);
		int ranCol = (int)(Math.random() * cols);
		while (minesweeperBoard[ranRow][ranCol].equals("bomb") || (ranRow >= iMin && ranRow <= iMax &&
				ranCol >= jMin && ranCol <= jMax))
		{
			ranRow = (int)(Math.random() * rows);
			ranCol = (int)(Math.random() * cols);
		}
		minesweeperBoard[row][col] = "";
		minesweeperBoard[ranRow][ranCol] = "bomb";
		bombs.remove(new Point(row, col));
		bombs.add(new Point(ranRow, ranCol));
	}

	// checks how many mines are adjacent to it
	public int checkAdjacent(int row, int col) {
		if (!checked.contains(new Point(row, col)))
		{
			checked.add(new Point(row, col));
		}
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
				if(checkForBomb(i, j) && !checked.contains(new Point(i, j))) {
					count++;
				}
			}
		}

		return count;
	}

	// checks for a bomb on that space
	public boolean checkForBomb(int row, int col) {
		if (minesweeperBoard[row][col].equals("bomb")) {
			return true;
		}

		return false;
	}

}





