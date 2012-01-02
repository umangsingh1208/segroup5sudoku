package group5.sudoku.generator;

import group5.sudoku.model.SudokuModel;
import group5.sudoku.solver.Solver;

import java.util.Date;

import org.apache.log4j.Logger;

// Class Grid manages the creation of puzzles
public class Generator {

	public static boolean OPTION_SYMMETRICAL = false;

	// The Master Grid
	private int[][] grid;

	// Size of a box (measured in squares) and the puzzle (meas. in boxes).
	private int n = 3;
	private int N = n * n; // n*n
	private int difficulty;

	// Given numbers:
	private int[][] given;
	private int countGiven;

	// Available numbers in each box, row and column, used for contructing a
	// valid grid (in function 'create')
	private boolean[][] boxes;
	private boolean[][] rows;
	private boolean[][] cols;

	// Help figure out in which box (1..9) coordinates (i,j) are:
	private int[][] rc2box = { { 0, 0, 0, 1, 1, 1, 2, 2, 2 },
			{ 0, 0, 0, 1, 1, 1, 2, 2, 2 }, { 0, 0, 0, 1, 1, 1, 2, 2, 2 },
			{ 3, 3, 3, 4, 4, 4, 5, 5, 5 }, { 3, 3, 3, 4, 4, 4, 5, 5, 5 },
			{ 3, 3, 3, 4, 4, 4, 5, 5, 5 }, { 6, 6, 6, 7, 7, 7, 8, 8, 8 },
			{ 6, 6, 6, 7, 7, 7, 8, 8, 8 }, { 6, 6, 6, 7, 7, 7, 8, 8, 8 } };

	public Generator() {
		this.grid = new int[N][N];
		this.given = new int[N][N];
		this.boxes = new boolean[N][N];
		this.rows = new boolean[N][N];
		this.cols = new boolean[N][N];
	}

	public SudokuModel generate() {
		// Start the timer
		long t = new Date().getTime();
		this.difficulty = 0;

		// Fill a grid with numbers:
		this.cleanGrid();
		this.create(this.grid, this.boxes, this.rows, this.cols, 0);

		// Keep track of the best grid we've found:
		int[][] best = new int[N][N];
		int bestDifficulty = 0;
		int bestGiven = N * N + 1;
		int bestTry = 0;

		int tries = 0;
		// Generate 100 puzzles and pick the most difficult one
		while (tries < 100) {
			countGiven = 0;
			difficulty = 0;
			// Clean the given numbers, but keep the grid intact
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					this.given[i][j] = 0;
				}
			}

			// Select 17 random numbers to show
			// 17 is an absolute minimum. Any less and the puzzle is garanteed
			// to be unsolvable.
			this.randomGiven(17);
			difficulty = this.solvable();

			// Keep adding numbers until the puzzle is solvable
			while (difficulty == 0) {
				this.addRandomGiven();
				difficulty = this.solvable();
			}
			if (countGiven <= bestGiven) {
				// If this puzzle is better then anything we've found so far,
				// store it
				bestGiven = countGiven;
				bestDifficulty = difficulty;
				bestTry = tries;
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						best[i][j] = this.given[i][j];
					}
				}
				if (bestGiven < 35 && bestDifficulty > 960)
					break; // Good enough. Stop searching.
			}
			tries++;
		}

		// Restore the best grid:
		countGiven = bestGiven;
		difficulty = bestDifficulty;
		Logger logger = Logger.getLogger("sudoku.generator");
		logger.debug("Puzzle " + bestTry + " is best");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.given[i][j] = best[i][j];
			}
		}

		long t2 = new Date().getTime();
		logger.info("Puzzle created in " + (t2 - t) + " ms," + "with "
				+ difficulty + " difficulty points" + "and " + countGiven
				+ " given numbers.");

		return adaptDataToModel();
	}

	// Fill the grid with numbers, with regards to the rules of the game:
	private boolean create(int[][] g, boolean[][] b, boolean[][] r,
			boolean[][] c, int level) {
		boolean validFound;
		boolean emptySquare = false;
		boolean _b, _r, _c;

		// Make sure the grid is realllllyyy random!
		int[] kList = permutateList();

		// For each row i...
		for (int i = 0; i < N; i++) {
			// ... and each column j...
			for (int j = 0; j < N; j++) {
				if (g[i][j] == 0) {
					emptySquare = true;
					validFound = false;

					// ... and for each value 1-9...
					for (int k = 0; k < N; k++) {
						_b = b[this.rc2box[i][j]][kList[k]];
						_r = r[i][kList[k]];
						_c = c[j][kList[k]];
						// ...if k is a valid value for grid[i,j]...
						if (_b && _r && _c) {
							// ...fill it in...
							b[this.rc2box[i][j]][kList[k]] = false;
							r[i][kList[k]] = false;
							c[j][kList[k]] = false;
							g[i][j] = kList[k] + 1;

							// ...and try to fill the rest of the grid,
							// recursively
							if (create(g, b, r, c, level + 1)) {
								return true;
							}

							g[i][j] = 0;
							b[this.rc2box[i][j]][kList[k]] = _b;
							r[i][kList[k]] = _r;
							c[j][kList[k]] = _c;
						}
					}
					if (!validFound) {
						// Puzzle is invalid. Backtrack and try again with
						// different numbers.
						return false;
					}
				}
			}
		}

		if (!emptySquare) {
			// We're done!
			this.grid = g;
			return true;
		}
		return false;
	}

	// randomGiven(x) means: exactly x numbers are shown.
	// The lower this number, the more difficult the puzzle.
	// At least 17 numbers need to be shown for any puzzle to be solvable.
	private void randomGiven(int showHowMany) {
		// Erase all given numbers, of previous tries.
		for (int i = 0; i < this.N; i++) {
			for (int j = 0; j < this.N; j++) {
				this.given[i][j] = 0;
			}
		}
		while (countGiven < showHowMany)
			this.addRandomGiven();
	}

	private void addRandomGiven() {
		int i = (int) (Math.random() * this.N);
		int j = (int) (Math.random() * this.N);
		while (this.given[i][j] != 0) {
			i = (int) (Math.random() * this.N);
			j = (int) (Math.random() * this.N);
		}

		this.given[i][j] = this.grid[i][j];
		countGiven++;

		if (OPTION_SYMMETRICAL) {
			this.given[j][N - i - 1] = this.grid[j][N - i - 1];
			this.given[N - i - 1][N - j - 1] = this.grid[N - i - 1][N - j - 1];
			this.given[N - j - 1][i] = this.grid[N - j - 1][i];
			this.given[j][i] = this.grid[j][i];
			this.given[i][N - j - 1] = this.grid[i][N - j - 1];
			this.given[N - i - 1][j] = this.grid[N - i - 1][j];
			this.given[N - j - 1][N - i - 1] = this.grid[N - j - 1][N - i - 1];
			this.countGiven += 7;
		}
	}

	// Starts a Solver to check if the generated puzzle is solvable
	private int solvable() {
		// returns difficulty if puzzle is solvable, or 0 otherwise
		return new Solver().solve(this.N, this.given);
	}

	// Returns the difficulty level (1...5) based on the difficulty points
	// (roughly 500...1500)
	private int getDifficultyLevel() {
		// 0... 700 = easy
		// 570... 960 = normal
		// 960...1200 = hard
		// 1200... = very hard
		if (this.difficulty < 700)
			return Difficulty.LEVEL_EASY;
		if (this.difficulty < 960)
			return Difficulty.LEVEL_NORMAL;
		if (this.difficulty < 1200)
			return Difficulty.LEVEL_HARD;
		return Difficulty.LEVEL_VERY_HARD;
	}

	public SudokuModel generate(int difficulty) {
		Logger logger = Logger.getLogger("sudoku.generator");
		SudokuModel result = this.generate();
		logger.debug("Generating sudoku");
		while (this.getDifficultyLevel() != difficulty) {
			result = this.generate();
		}
		logger.debug("Generation finished");
		return result;
	}

	private SudokuModel adaptDataToModel() {
		SudokuModel model = new SudokuModel();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				model.setPlayerMatrixValue(given[i][j], i, j);
				model.setSolutionMatrixValue(grid[i][j], i, j);
			}
		}
		return model;
	}

	/*** Helper functions ***/
	// Cleans all grids and prepare them for generating a new puzzle
	private void cleanGrid() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.grid[i][j] = 0;
				this.boxes[i][j] = true;
				this.rows[i][j] = true;
				this.cols[i][j] = true;
				this.given[i][j] = 0;
			}
		}
	}

	// Creates a random permutation of {1,2,...,N}
	private int[] permutateList() {
		int[] a = new int[N];
		for (int i = 0; i < N; i++)
			a[i] = i;
		for (int i = 0; i < N; i++) {
			int r = (int) (Math.random() * N);
			int swap = a[r];
			a[r] = a[i];
			a[i] = swap;
		}
		return a;
	}
}