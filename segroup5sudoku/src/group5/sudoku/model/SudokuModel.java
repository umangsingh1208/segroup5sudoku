package group5.sudoku.model;

public class SudokuModel {

    // The Master Grid    
    public int[][] grid;
    
    // Size of a box (measured in squares) and the puzzle (meas. in boxes).
    private int n;
    private int N;  // n*n
    
 // Given numbers:
    public int[][] given;
    public int countGiven;
    
    private boolean[][] boxes;
    private boolean[][] rows;
    private boolean[][] cols;
    
    // Help figure out in which box (1..9) coordinates (i,j) are:
    private int[][] rc2box = {{0,0,0,1,1,1,2,2,2},
                      {0,0,0,1,1,1,2,2,2},
                      {0,0,0,1,1,1,2,2,2},
                      {3,3,3,4,4,4,5,5,5},
                      {3,3,3,4,4,4,5,5,5},
                      {3,3,3,4,4,4,5,5,5},
                      {6,6,6,7,7,7,8,8,8},
                      {6,6,6,7,7,7,8,8,8},
                      {6,6,6,7,7,7,8,8,8}};

	public int[][] getGrid() {
		return grid;
	}

	public int getSmallN() {
		return n;
	}

	public int getN() {
		return N;
	}

	public int[][] getGiven() {
		return given;
	}

	public int getCountGiven() {
		return countGiven;
	}

	public boolean[][] getBoxes() {
		return boxes;
	}

	public boolean[][] getRows() {
		return rows;
	}

	public boolean[][] getCols() {
		return cols;
	}

	public int[][] getRc2box() {
		return rc2box;
	}

	public void setGrid(int[][] grid) {
		this.grid = grid;
	}

	public void setSmallN(int n) {
		this.n = n;
	}

	public void setN(int n) {
		N = n;
	}

	public void setGiven(int[][] given) {
		this.given = given;
	}

	public void setCountGiven(int countGiven) {
		this.countGiven = countGiven;
	}

	public void setBoxes(boolean[][] boxes) {
		this.boxes = boxes;
	}

	public void setRows(boolean[][] rows) {
		this.rows = rows;
	}

	public void setCols(boolean[][] cols) {
		this.cols = cols;
	}

	public void setRc2box(int[][] rc2box) {
		this.rc2box = rc2box;
	}
    
}
