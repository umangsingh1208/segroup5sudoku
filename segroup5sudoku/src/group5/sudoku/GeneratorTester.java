package group5.sudoku;

import group5.sudoku.generator.Difficulty;
import group5.sudoku.generator.Generator;
import group5.sudoku.model.SudokuModel;

public class GeneratorTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Generator g = new Generator();
		g.beVerbose(true);
		
		SudokuModel model = g.generate();
		model.print();

		g = new Generator();
		model = g.generate(Difficulty.LEVEL_NORMAL);
		model.print();
	}

	

}
