package edu.sapi.mestint;

import java.util.HashSet;

/**
 * @author david.kegyes@p4b.ro
 */
public class BacktrackingMVRForwardChecking {

    private int n;
    private int dim;
    private int valueAssignmentCounter;

    public void solve(int[][] sudoku) {
        this.dim = sudoku.length;
        this.n = (int) Math.sqrt(dim);
        if (backTrackMVRForwardChecking(sudoku)) {
            System.out.println("Solution");
            Utils.printSudoku(sudoku);
        } else {
            System.out.println("No solution found");
        }
        System.out.println("Value assignment count: " + valueAssignmentCounter);
    }

    private Cell getStartingCell(int[][] sudoku) {
        Cell minimumPossibleValue = null;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (sudoku[i][j] == 0) {
                    Cell cell = new Cell(i, j, dim);
                    for (int v = 0; v < dim; v++) {
                        if (ok(v + 1, cell, sudoku)) {
                            cell.values.add(v + 1);
                        }
                    }
                    if (minimumPossibleValue == null || minimumPossibleValue.values.size() > cell.values.size()) {
                        minimumPossibleValue = cell;
                    }
                }
            }
        }
        return minimumPossibleValue;
    }

    private boolean ok(int k, Cell cell, int[][] sudoku) {
        int is = cell.i - cell.i % n;
        int js = cell.j - cell.j % n;
        for (int i = is; i < is + n; i++) {
            for (int j = js; j < js + n; j++) {
                if (sudoku[i][j] == k)
                    return false;
            }
        }

        for (int l = 0; l < dim; l++) {
            if (sudoku[l][cell.j] == k)
                return false;
        }
        for (int l = 0; l < dim; l++) {
            if (sudoku[cell.i][l] == k)
                return false;
        }
        return true;
    }

    private boolean backTrackMVRForwardChecking(int[][] sudoku) {
        if (isComplete(sudoku))
            return true;
        Cell cell = getStartingCell(sudoku);
        for (Integer v : cell.values) {
            if (ok(v, cell, sudoku)) {
                sudoku[cell.i][cell.j] = v;
                valueAssignmentCounter++;
                if (backTrackMVRForwardChecking(sudoku)) {
                    return true;
                }
                sudoku[cell.i][cell.j] = 0;
            }
        }
        return false;
    }

    private boolean isComplete(int[][] sudoku) {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku.length; j++)
                if (sudoku[i][j] == 0)
                    return false;
        }
        return true;
    }

    class Cell {
        private int i;
        private int j;
        private HashSet<Integer> values;

        public Cell(int i, int j, int dim) {
            this.i = i;
            this.j = j;
            this.values = new HashSet<>(dim);
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "i=" + i +
                    ", j=" + j +
                    ", values=" + values +
                    '}';
        }
    }
}
