package group5.sudoku;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import group5.sudoku.generator.Difficulty;
import group5.sudoku.generator.Generator;
import group5.sudoku.model.SudokuModel;

public class GeneratorTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Logger logger = Logger.getLogger("sudoku.generator");
		logger.setLevel(Level.INFO);
		
		logger.info("Creating generator");
		Generator g = new Generator();
		SudokuModel model;
		
		logger.info("Calling generator to create a valid model");
		model = g.generate();
		model.print();
		
		logger.info("Calling generator to create a valid model - based on difficulty-level "+Difficulty.LEVEL_NORMAL);
		model = g.generate(Difficulty.LEVEL_NORMAL);
		model.print();
	}

	

}
