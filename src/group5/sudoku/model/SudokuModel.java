package group5.sudoku.model;

import org.apache.log4j.Logger;

public class SudokuModel {

    // Size of a box (measured in squares) and the puzzle (meas. in boxes).
    private Integer n = 3;
    private Integer N = n*n;
    
    public InputBox[][] solutionMatrix = new InputBox[N][N];
    public InputBox[][] playerMatrix = new InputBox[N][N];
    
    public SudokuModel(){
        initMatrixes();
    }

	private void initMatrixes() {
		for (int i=0;i<N;i++){
			for (int j=0;j<N;j++){
				playerMatrix[i][j] = new InputBox();
				solutionMatrix[i][j] = new InputBox();
			}
		}
	}

	public int getPlayerMatrixValue(int x, int y){
		return playerMatrix[x][y].getValue();
	}
	
	public void setPlayerMatrixValue(int value, int x, int y){
		playerMatrix[x][y].setValue(value);
	}

	public int geSolutionMatrixValue(int x, int y){
		return solutionMatrix[x][y].getValue();
	}
	
	public void setSolutionMatrixValue(int value, int x, int y){
		solutionMatrix[x][y].setValue(value);
	}



	public void print() {
		printSolutionMatrix();
		printPlayerMatrix();
	}

	private void printPlayerMatrix() {
		Logger  logger = Logger.getLogger("sudoku.model");
		logger.info("Printing player matrix");
		System.out.print("\n+------+------+------+\n");
		for (int i = 0; i < playerMatrix.length; i++) {
			if (i != 0 && i % 3 == 0) {
				System.out.print("+------+------+------+\n");
			}
			for (int j = 0; j < playerMatrix[i].length; j++) {
				if (j % 3 == 0) {
					System.out.print("|");
				}
				System.out.print(playerMatrix[i][j].getValue() + " ");
			}
			System.out.print("|\n");

		}
		System.out.print("+------+------+------+\n\n");
	}
	
	private void printSolutionMatrix() {
		Logger  logger = Logger.getLogger("sudoku.model");
		logger.info("Printing solution matrix");
		System.out.print("\n+------+------+------+\n");
		for (int i = 0; i < solutionMatrix.length; i++) {
			if (i != 0 && i % 3 == 0) {
				System.out.print("+------+------+------+\n");
			}
			for (int j = 0; j < solutionMatrix[i].length; j++) {
				if (j % 3 == 0) {
					System.out.print("|");
				}
				System.out.print(solutionMatrix[i][j].getValue() + " ");
			}
			System.out.print("|\n");

		}
		System.out.print("+------+------+------+\n\n");
	}
	
	
}
