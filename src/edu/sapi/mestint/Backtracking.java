package edu.sapi.mestint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author david.kegyes@p4b.ro
 */
public class Backtracking {

    private static final int UNASSIGNED = 0;
    private static int count = 0;
    private int n;
    private int[][] sudoku;
    private int dim;
    private int[][] sqStartPos;
    private int[][] sqEndPos;
    private boolean[][] sqNumInUse;
    private List<Cell> freeSpots;
    private int valueAssignmentCounter;


    public void solve(int[][] sudokuIn) {
        this.sudoku = sudokuIn;
        initHelperVariables();
        if (resolveSquare(0)) {
            System.out.println("Final");
            Utils.printSudoku(sudoku);
        } else {
            System.out.println("No solution");
        }
        System.out.println("Value assignment count: " + valueAssignmentCounter);

    }

    private void initHelperVariables() {
        this.dim = sudoku[0].length;
        this.n = (int) Math.sqrt(dim);
        this.sqNumInUse = new boolean[dim][dim];
        freeSpots = new ArrayList<>();
        generateStartPositions();
        generateEndPositions();
        generateUsedAndFixedNumbers();
    }

    private void generateUsedAndFixedNumbers() {
        int s = 0, i = 0, j = 0;
        while (i != dim - 1 || j != dim - 1) {
            if (sudoku[i][j] != 0) {
                sqNumInUse[s][sudoku[i][j] - 1] = true;
            } else {
                freeSpots.add(new Cell(i, j, s));
            }
            if (i == sqEndPos[s][0] && j == sqEndPos[s][1]) {
                s++;
                i = sqStartPos[s][0];
                j = sqStartPos[s][1];
            } else if (j != sqEndPos[s][1]) {
                j++;
            } else {
                i++;
                j = sqStartPos[s][1];
            }
        }
        if (sudoku[i][j] != 0) {
            sqNumInUse[s][sudoku[i][j] - 1] = true;
        } else {
            freeSpots.add(new Cell(i, j, s));
        }
    }

    private void generateStartPositions() {
        sqStartPos = new int[dim][2];
        int posi = -n;
        int posj = 0;
        for (int i = 0; i < dim; i++) {
            if ((i % n) == 0)
                posi += n;
            sqStartPos[i][0] = posi;
        }
        for (int i = 0; i < dim; i++) {
            sqStartPos[i][1] = posj;
            if (posj == dim - n)
                posj = 0;
            else
                posj += n;
        }
    }

    private void generateEndPositions() {
        sqEndPos = new int[dim][2];
        int posi = -1;
        int posj = -1;
        for (int i = 0; i < dim; i++) {
            if (i % n == 0)
                posi += n;
            sqEndPos[i][0] = posi;
        }
        for (int i = 0; i < dim; i++) {
            sqEndPos[i][1] = posj += n;
            if (posj == dim - 1)
                posj = -1;

        }
    }

    private boolean resolveSquare(int i) {
        if (i == freeSpots.size()) {
            return true;
        }
        Cell freeSpot = freeSpots.get(i);
        for (int k = 0; k < dim; k++) {
            if (ok(k + 1, freeSpot)) {
                sudoku[freeSpot.getI()][freeSpot.getJ()] = k + 1;
                sqNumInUse[freeSpot.getS()][k] = true;
                valueAssignmentCounter++;
                if (resolveSquare(i + 1)) {
                    return true;
                }
                sqNumInUse[freeSpot.getS()][k] = false;
                sudoku[freeSpot.getI()][freeSpot.getJ()] = 0;
            }
        }
        return false;
    }

    private boolean ok(int k, Cell freeSpot) {
        if (sqNumInUse[freeSpot.getS()][k - 1])
            return false;

        for (int l = 0; l < dim; l++) {
            if (sudoku[l][freeSpot.getJ()] == k)
                return false;
        }
        for (int l = 0; l < dim; l++) {
            if (sudoku[freeSpot.getI()][l] == k)
                return false;
        }
        return true;
    }

    class Cell {
        private int i;
        private int j;
        private int s;

        public Cell(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Cell(int i, int j, int s) {
            this.i = i;
            this.j = j;
            this.s = s;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getJ() {
            return j;
        }

        public void setJ(int j) {
            this.j = j;
        }

        public int getS() {
            return s;
        }

        public void setS(int s) {
            this.s = s;
        }
    }
}
