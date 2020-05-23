package edu.sapi.mestint;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author david.kegyes@p4b.ro
 */
public class BacktrackingMVRAC3 {

    private int n;
    private int dim;
    private int valueAssignmentCounter;

    public void solve(int[][] sudoku) {
        this.dim = sudoku.length;
        this.n = (int) Math.sqrt(dim);
        if (backtrackArc(sudoku)) {
            System.out.println("Solution");
            Utils.printSudoku(sudoku);
        } else {
            System.out.println("No solution found");
        }
        System.out.println("Value assignment count: " + valueAssignmentCounter);
    }

    private List<Pair<Cell, List<Integer>>> removeFixedCells(int[][] sudoku, Map<Cell, List<Integer>> cellValues) {
        List<Pair<Cell, List<Integer>>> cellValueList = cellValues.entrySet().stream()
                .filter(e -> sudoku[e.getKey().i][e.getKey().j] == 0)
                .map(es -> new Pair<>(new Cell(es.getKey()), (List<Integer>) new ArrayList<>(es.getValue())))
                .collect(Collectors.toList());
        cellValueList.sort((e1, e2) -> e1.getValue().size() > e2.getValue().size() ? 1 : 0);
        return cellValueList;
    }

    private Map.Entry<Cell, List<Integer>> getStartingCell(int[][] sudoku) {
        Queue arcs = getArcs(sudoku);
        Map<Cell, List<Integer>> cellValuesMap = arcConsistency(sudoku, arcs, getSudokuValues(sudoku));
        Map.Entry<Cell, List<Integer>> cellValuePair = null;
        for (Map.Entry<Cell, List<Integer>> es : cellValuesMap.entrySet()) {
            if ((sudoku[es.getKey().i][es.getKey().j] == 0) && (cellValuePair == null || cellValuePair.getValue().size() > es.getValue().size())) {
                cellValuePair = es;
            }
        }
        return cellValuePair;
    }

    private boolean backtrackArc(int[][] sudoku) {
        if (isComplete(sudoku)) {
            return true;
        }
        Map.Entry<Cell, List<Integer>> step = getStartingCell(sudoku);
        Cell cell = step.getKey();
        List<Integer> values = step.getValue();
        for (Integer v : values) {
            if (ok(v, cell, sudoku)) {
                sudoku[cell.i][cell.j] = v;
                valueAssignmentCounter++;
                if (backtrackArc(sudoku)) {
                    return true;
                }
                sudoku[cell.i][cell.j] = 0;
            }
        }
        return false;
    }

    private Map<Cell, List<Integer>> getSudokuValues(int[][] sudoku) {
        Map<Cell, List<Integer>> cellValues = new HashMap<>();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                Cell cell = new Cell(i, j);
                List<Integer> values = new ArrayList<>();
                if (sudoku[i][j] == 0) {
                    for (int v = 0; v < dim; v++) {
                        if (ok(v + 1, cell, sudoku)) {
                            values.add(v + 1);
                        }
                    }
                } else {
                    values.add(sudoku[i][j]);
                }
                cellValues.put(cell, values);
            }
        }
        return cellValues;
    }

    private Map<Cell, List<Integer>> arcConsistency(int[][] sudoku, Queue arcs, Map<Cell, List<Integer>> cellValues) {
        while (!arcs.isEmpty()) {
            boolean changed = false;
            Arc arc = (Arc) arcs.poll();
            if (removeInconsistentValues(arc, cellValues)) {
                addToArcsQueue(arc.xi, true, arcs);
            }
        }
        return cellValues;
    }

    private boolean removeInconsistentValues(Arc arc, Map<Cell, List<Integer>> cellValues) {
        boolean removed = false;
        List<Integer> cellXIValues = cellValues.get(arc.xi);
        for (int i = cellXIValues.size() - 1; i >= 0; --i) {
            if (!isConsistent(cellValues.get(arc.xj), cellXIValues.get(i))) {
                cellXIValues.remove(cellXIValues.get(i));
                removed = true;
            }
        }
        return removed;
    }

    private boolean isConsistent(List<Integer> values, int value) {
        for (int v : values) {
            if (value != v)
                return true;
        }
        return false;
    }

    private Queue<Arc> getArcs(int[][] sudoku) {
        Queue<Arc> arcs = new LinkedList<>();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                Cell cell = new Cell(i, j);
                addToArcsQueue(cell, false, arcs);
            }
        }
        return arcs;
    }

    private void addToArcsQueue(Cell cell, boolean inverse, Queue<Arc> arcs) {
        addRowConstraintArcs(cell, inverse, arcs);
        addColumnConstraintArcs(cell, inverse, arcs);
        addSquareConstraintArcs(cell, inverse, arcs);
    }

    private void addSquareConstraintArcs(Cell cell, boolean inverse, Queue<Arc> arcs) {
        int is = cell.i - cell.i % n;
        int js = cell.j - cell.j % n;
        for (int i = is; i < is + n; i++) {
            for (int j = js; j < js + n; j++) {
                if (i == cell.i || j == cell.j)
                    continue;
                addIfNotExists(cell, new Cell(i, j), inverse, arcs);
            }
        }
    }

    private void addRowConstraintArcs(Cell cell, boolean inverse, Queue<Arc> arcs) {
        for (int l = 0; l < dim; l++) {
            if (l == cell.j)
                continue;
            addIfNotExists(cell, new Cell(cell.i, l), inverse, arcs);
        }
    }

    private void addColumnConstraintArcs(Cell cell, boolean inverse, Queue<Arc> arcs) {
        for (int l = 0; l < dim; l++) {
            if (l == cell.i)
                continue;
            addIfNotExists(cell, new Cell(l, cell.j), inverse, arcs);
        }
    }

    private void addIfNotExists(Cell xi, Cell xj, boolean inverse, Queue<Arc> arcs) {
        Arc arc;
        if (inverse)
            arc = new Arc(xj, xi);
        else
            arc = new Arc(xi, xj);
        if (!arcs.contains(arc))
            arcs.add(arc);
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

    private boolean isComplete(int[][] sudoku) {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku.length; j++)
                if (sudoku[i][j] == 0)
                    return false;
        }
        return true;
    }

    public static class Cell {
        private int i;
        private int j;

        public Cell(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Cell(Cell cell) {
            this.i = cell.i;
            this.j = cell.j;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return i == cell.i &&
                    j == cell.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "i=" + i +
                    ", j=" + j +
                    '}';
        }
    }

    static class Arc {
        private Cell xi;
        private Cell xj;

        public Arc(Cell xi, Cell xj) {
            this.xi = xi;
            this.xj = xj;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Arc arc = (Arc) o;
            return Objects.equals(xi, arc.xi) &&
                    Objects.equals(xj, arc.xj);
        }

        @Override
        public int hashCode() {
            return Objects.hash(xi, xj);
        }

        @Override
        public String toString() {
            return "Arc{" +
                    "xi=" + xi +
                    ", xj=" + xj +
                    '}';
        }
    }
}
