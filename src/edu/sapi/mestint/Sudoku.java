package edu.sapi.mestint;

public class Sudoku {


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Sudoku solver");
        if (args.length != 2) {
            printUsageInfo();
            return;
        }
        int[][] sudoku = Utils.readSudokuFromFile(args[0]);
        System.out.println("Input Sudoku");
        Utils.printSudoku(sudoku);
        System.out.println("Solved Sudoku");
        switch (args[1]) {
            case "1":
                new Backtracking().solve(sudoku);
                break;
            case "2":
                new BacktrackingMVRForwardChecking().solve(sudoku);
                break;
            case "3":
                new BacktrackingMVRAC3().solve(sudoku);
                break;
            default:
                printUsageInfo();
        }
        System.out.println("Runtime : " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static void printUsageInfo() {
        System.out.println("Input parameters are required:");
        System.out.println("I. The input Sudoku in text file:");
        System.out.println("   a. On the first line should be one value which is the dimension of the table");
        System.out.println("   b. On the rest of the lines the values of the table separated with spaces");
        System.out.println("   c. Values from 1-DIM are the fixed values");
        System.out.println("   d. Put 0 where the values are missing and the program will fill the place with a proper one");
        System.out.println("!!! IMPORTANT the Sudoku should be a valid and solvable");
        System.out.println("II. The method key which will be used to solve the game");
        System.out.println("   1. Backtracking ");
        System.out.println("   2. Backtracking + MVR + Forward checking");
        System.out.println("   3. Backtracking + MVR + AC3");
        System.out.println("Example: java Sudoku input.txt 1");
    }

}
