package group5.sudoku.generator;

import group5.sudoku.model.SudokuModel;
import group5.sudoku.solver.Solver;

import java.util.Date;

// Class Grid manages the creation of puzzles
public class Generator {
	

	public static boolean OPTION_SYMMETRICAL = false;
	
	public int difficulty;
	SudokuModel model;
    boolean output = true;
	
	
    public Generator(int n, int N) {
    	model = new SudokuModel();
    	model.setSmallN(n);
    	model.setN(N);
    	model.setGrid(new int[N][N]);
    	model.setGiven(new int[N][N]);
    	model.setBoxes(new boolean[N][N]);
    	model.setCols(new boolean[N][N]);
    	model.setRows(new boolean[N][N]);
    }

    public SudokuModel getModel(){
    	return model;
    }
    
    public SudokuModel createSudoku() {
		// Start the timer
        long t = new Date().getTime();
        this.difficulty = 0;

        // Fill a grid with numbers:
        this.cleanGrid();
        this.create(model.getGrid(), model.getBoxes(), model.getRows(), model.getCols(), 0);

		// Keep track of the best grid we've found:
		int[][] best = new int[model.getN()][model.getN()];
		int bestDifficulty = 0;
		int bestGiven = model.getN()*model.getN()+1;
		int bestTry = 0;
		
        int tries = 0;
        // Generate 100 puzzles and pick the most difficult one
		while(tries < 100) {
			model.setCountGiven(0);
			difficulty = 0;
			// Clean the given numbers, but keep the grid intact
	        for(int i=0; i<model.getN(); i++) {
	            for(int j=0; j<model.getN(); j++) {
	            	model.getGiven()[i][j] = 0;
	            }
	        }
			
	        // Select 17 random numbers to show
	        // 17 is an absolute minimum. Any less and the puzzle is garanteed to be unsolvable.
	        this.randomGiven(17);
            difficulty = this.solvable();
	        
	        // Keep adding numbers until the puzzle is solvable
	        while(difficulty==0) {
	        	this.addRandomGiven();
	            difficulty = this.solvable();
	        }
	        if(model.getCountGiven() <= bestGiven) {
	        	// If this puzzle is better then anything we've found so far, store it
				bestGiven = model.getCountGiven();
				bestDifficulty = difficulty;
				bestTry = tries;
		        for(int i=0; i<model.getN(); i++) {
		            for(int j=0; j<model.getN(); j++) {
		                best[i][j] = model.getGiven()[i][j];
		            }
		        }
		        if(bestGiven < 35 && bestDifficulty > 960)
		        	break; // Good enough. Stop searching.
	        }
	        tries++;
		}

		// Restore the best grid:		
		model.setCountGiven(bestGiven);
		difficulty = bestDifficulty;
		if(this.output) 
			System.out.println("Puzzle "+bestTry+" is best");
        for(int i=0; i<model.getN(); i++) {
            for(int j=0; j<model.getN(); j++) {
            	model.getGiven()[i][j] = best[i][j];
            }
        }
		
		if(this.output) {
	        long t2 = new Date().getTime();
	        System.out.println("Puzzle created in "+(t2-t)+" ms,\n"
	        				 + "with "+difficulty+" difficulty points\n"
	        				 + "and "+model.getCountGiven()+" given numbers.");
		}
		
		return model;
    }
    public boolean create(int level) {
        return create(model.getGrid(), model.getBoxes(), model.getRows(), model.getCols(), level);
    }
    
    
    // Fill the grid with numbers, with regards to the rules of the game:
    public boolean create(int[][] g, boolean[][] b, boolean[][] r, boolean[][] c, int level) {
        boolean validFound;
        boolean emptySquare = false;
        boolean _b, _r, _c;
        
        // Make sure the grid is realllllyyy random!
        int[] kList = permutateList();
        
        // For each row i...
        for(int i=0; i<model.getN(); i++) {
        	// ... and each column j...
            for(int j=0; j<model.getN(); j++) {
                if(g[i][j]==0) {
                    emptySquare = true;
                    validFound = false;
                    
                    // ... and for each value 1-9...
                    for(int k=0; k<model.getN(); k++) {
                        _b = b[model.getRc2box()[i][j]][kList[k]];
                        _r = r[i][kList[k]];
                        _c = c[j][kList[k]];
                        // ...if k is a valid value for grid[i,j]...
                        if(_b && _r && _c) {
                        	// ...fill it in...
                            b[model.getRc2box()[i][j]][kList[k]] = false;
                            r[i][kList[k]] = false;
                            c[j][kList[k]] = false;
                            g[i][j] = kList[k]+1;
                            
                            //...and try to fill the rest of the grid, recursively
                            if(create(g, b, r, c, level+1)) {
                                return true;
                            }
                            
                            g[i][j] = 0;
                            b[model.getRc2box()[i][j]][kList[k]] = _b;
                            r[i][kList[k]] = _r;
                            c[j][kList[k]] = _c;
                        }
                    }
                    if(!validFound) {
                        // Puzzle is invalid. Backtrack and try again with different numbers.
                        return false;
                    }
                }
            }
        }

        
        if(!emptySquare) {
            // We're done!
            model.setGrid(g);
            return true;
        }
        return false;
    }
    
    // randomGiven(x) means: exactly x numbers are shown.
    // The lower this number, the more difficult the puzzle.
    // At least 17 numbers need to be shown for any puzzle to be solvable.
    private void randomGiven(int showHowMany) {
        // Erase all given numbers, of previous tries.
        for(int i=0; i<this.model.getN(); i++) {
            for(int j=0; j<this.model.getN(); j++) {
                model.getGiven()[i][j] = 0;
            }
        }
        while(model.getCountGiven() < showHowMany)
        	this.addRandomGiven();
    }
    
    private void addRandomGiven() {
        int i = (int) (Math.random() * this.model.getN());
		int j = (int) (Math.random() * this.model.getN());
    	while(model.getGiven()[i][j] != 0) {
            i = (int) (Math.random() * this.model.getN());
			j = (int) (Math.random() * this.model.getN());
    	}
    	
    	model.getGiven()[i][j] = model.getGrid()[i][j];
    	model.setCountGiven(model.getCountGiven()+1);
    	
    	if(OPTION_SYMMETRICAL) {
	    	model.getGiven()[j][model.getN()-i-1] = model.getGrid()[j][model.getN()-i-1];
	    	model.getGiven()[model.getN()-i-1][model.getN()-j-1] = model.getGrid()[model.getN()-i-1][model.getN()-j-1];
	    	model.getGiven()[model.getN()-j-1][i] = model.getGrid()[model.getN()-j-1][i];
	    	model.getGiven()[j][i] = model.getGrid()[j][i];
	    	model.getGiven()[i][model.getN()-j-1] = model.getGrid()[i][model.getN()-j-1];
	    	model.getGiven()[model.getN()-i-1][j] = model.getGrid()[model.getN()-i-1][j];
	    	model.getGiven()[model.getN()-j-1][model.getN()-i-1] = model.getGrid()[model.getN()-j-1][model.getN()-i-1];
	    	model.setCountGiven(model.getCountGiven()+7);
    	}
    }
    
    // Starts a Solver to check if the generated puzzle is solvable
    private int solvable() {
    	// returns difficulty if puzzle is solvable, or 0 otherwise
        return new Solver().solve(this.model.getN(), model.getGiven());
    }
    
    // Returns the difficulty level (1...5) based on the difficulty points (roughly 500...1500)
    public int getDifficultyLevel() {
    	//    0... 515 = very easy
    	//  515... 570 = easy
    	//  570... 960 = normal
    	//  960...1200 = hard
    	// 1200...     = very hard
    	if(this.difficulty<515)
    		return Difficulty.LEVEL_VERY_EASY;
    	if(this.difficulty<570)
    		return Difficulty.LEVEL_EASY;
    	if(this.difficulty<960)
    		return Difficulty.LEVEL_NORMAL;
    	if(this.difficulty<1200)
    		return Difficulty.LEVEL_HARD;
		return Difficulty.LEVEL_VERY_HARD;
    }

	public void setOutput(boolean o) {
		this.output = o;
	}
	
/*** Helper functions ***/
    // Cleans all grids and prepare them for generating a new puzzle
    private void cleanGrid() {
        for(int i=0; i<model.getN(); i++) {
            for(int j=0; j<model.getN(); j++) {
                model.getGrid()[i][j] = 0;
                model.getBoxes()[i][j] = true;
                model.getRows()[i][j] = true;
                model.getCols()[i][j] = true;
                model.getGiven()[i][j] = 0;
            }
        }
    }
    
    // Creates a random permutation of {1,2,...,model.getN()}
    private int[] permutateList() {
        int[] a = new int[model.getN()];
        for(int i=0; i<model.getN(); i++)
            a[i] = i;
        for(int i=0; i<model.getN(); i++) {
            int r = (int) (Math.random() * model.getN());
            int swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }
        return a;
    }

/*** Debug functions ***/    
    public void printGrid() {
        this.printGrid(model.getGrid());
    }

    private void printGrid(int[][] g) {
        if(g == null)
            System.out.println("Grid == null!");
            
        for(int i=0; i<
            	model.getSmallN(); i++) {
            System.out.println("+-----+-----+-----+");
            for(int j=0; j<
                	model.getSmallN(); j++) {
                System.out.print("|");
                for(int k=0; k<
                    	model.getSmallN(); k++) {
                    int r = i*
                        	model.getSmallN()+j;
                    int c = k*
                        	model.getSmallN();
                    System.out.print((g[r][c]  !=0 ? ""+g[r][c]   : ".")
                                +" "+(g[r][c+1]!=0 ? ""+g[r][c+1] : ".")
                                +" "+(g[r][c+2]!=0 ? ""+g[r][c+2] : ".")
                                +"|");
                }
                System.out.print("\n");
            }
        }
        System.out.println("+-----+-----+-----+");
    }
}