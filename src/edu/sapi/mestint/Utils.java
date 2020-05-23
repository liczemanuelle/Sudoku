package edu.sapi.mestint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author david.kegyes@p4b.ro
 */
public class Utils {

    public static int[][] readSudokuFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            int dim = scanner.nextInt();
            int[][] sudoku = new int[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    sudoku[i][j] = scanner.nextInt();
                    if (sudoku[i][j] < 0) {
                        throw new IllegalArgumentException("The provided Sudoku is not a valid one.\nThe file should contain on the first line the dimension of the Sudoku and on the rest of the lines the values separated with spaces.\n!Only numbers should contain!");
                    }
                }
            }
            return sudoku;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found:  " + filePath);
        }
    }

    public static void printSudoku(int[][] sudoku) {
        for (int i = 0; i < sudoku.length; i++) {
            if (i % Math.sqrt(sudoku.length) == 0) {
                printSeparatorLine(sudoku.length);
            }
            for (int j = 0; j < sudoku[i].length; j++) {
                if (j % Math.sqrt(sudoku[i].length) == 0) {
                    System.out.print("|\t");
                }
                System.out.print(sudoku[i][j] + "\t");
            }
            System.out.println("|");
        }
        printSeparatorLine(sudoku.length);
    }

    private static void printSeparatorLine(int dim) {
        for (int j = 0; j < dim; j++) {
            if (j % Math.sqrt(dim) == 0) {
                System.out.print("\t");
            }
            System.out.print("--\t");
        }
        System.out.println();
    }
}
