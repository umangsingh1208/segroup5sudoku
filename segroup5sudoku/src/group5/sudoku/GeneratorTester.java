package group5.sudoku;

import java.util.Arrays;

import group5.sudoku.generator.Difficulty;
import group5.sudoku.generator.Generator;

public class GeneratorTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Generator g = new Generator(3, 9);
		g.createSudoku();
		System.out.println(g.getDifficultyLevel());

		printSudoku(g.getModel().getGiven());
		System.out.println();
		printSudoku(g.getModel().getGrid());

		g = new Generator(3, 9);
		System.out.println(g.getDifficultyLevel());
		g.create(Difficulty.LEVEL_VERY_HARD);
		printSudoku(g.getModel().getGiven());
	}

	private static void printSudoku(int[][] arr) {

		System.out.print("\n+------+------+------+\n");
		for (int i = 0; i < arr.length; i++) {
			if (i != 0 && i % 3 == 0) {
				System.out.print("+------+------+------+\n");
			}
			for (int j = 0; j < arr[i].length; j++) {
				if (j % 3 == 0) {
					System.out.print("|");
				}
				System.out.print(arr[i][j] + " ");
			}
			System.out.print("|\n");

		}
		System.out.print("+------+------+------+\n");
	}

}
