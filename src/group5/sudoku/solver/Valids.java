package group5.sudoku.solver;

// A Valids-object is basically a matrix N*N*N of booleans,
// where V[i,j,k] is true if number k is a valid option for
// square (i,j), considering the values that are filled in in
// the puzzle.
public class Valids {
    
    private boolean[][][] V;
    private int n;
    private int N;
    
    Valids(int n) {
        this.n = n;
        this.N = n*n;
        this.V = new boolean[N][N][N];
        this.clear();
    }
        
    Valids(int n, int[][] grid) {
        this(n);
        this.update(grid);
    }
 
    // Cleans V (sets all elements to 'true')
    public void clear() {
        for(int i=0; i<N; i++)
            for(int j=0; j<N; j++)
                for(int k=0; k<N; k++)
                    this.V[i][j][k] = true;
    }
    
    // Updates V using a grid
    public void update(int[][] grid) {
        this.clear();
        for(int i=0; i<N; i++)
            for(int j=0; j<N; j++)
                if(grid[i][j] != 0)
                    this.set(i,j,grid[i][j]);
    }
    
    // Updates V using a value for a single square
    public void set(int i, int j, int K) {
        // Eliminate candidate K from row, column and box:
        for(int a=0; a<N; a++) {
            this.V[i][a][K-1] = false;   // Eliminate K from row i
            this.V[a][j][K-1] = false;   // and column j
            this.V[i][j][a]   = false;   // and eliminate other candidates for (i,j)
        }
        // and from the box (i,j) is in:
        int[] box = Solver.rc2box(i,j);
        for(int a=0; a<3; a++) {
            for(int b=0; b<3; b++) {
                this.V[box[0]+a][box[1]+b][K-1] = false;
            }
        }
        
        // The derived value K is now also eliminated, so we must restore it:
        this.V[i][j][K-1] = true;
    }
    
//    public void set(int i, int j, int k, boolean value) {
//        this.V[i][j][k] = value;
//    }
    
    // Returns the valid values for square (i,j)
    public boolean[] get(int i, int j) {
        return this.V[i][j];
    }
    
    // Checks if a value is valid for a square (i,j)
    public boolean get(int i, int j, int k) {
        return this.V[i][j][k];
    }

    // Counts the number of candidates in an array (representing a row or column)
    public int[] countCandidates(int i, int j) {
        int r = 0;
        int r2 = -1;
        for(int a=0; a<N; a++) {
            if(this.V[i][j][a]) {
                r++;        // the number of possible values
                r2=a;       // one of the possible values (useful only if r=1)
            }
        }
        int[] _r = {r, r2};
        return _r;
    }
}