package kpi.fiot.gensu.core;

import android.util.Log;
import kpi.fiot.gensu.utils.Consts;

import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.Set;

public class SudokuGrid {

    private int[][] mGrid;
    private SudokuSubgrid[][] mSubgrids;
    private Set<Integer>[][] mVariants;

    public SudokuGrid(int[][] grid) {
        if (grid.length == Consts.SUDOKU_GRID_SIZE && grid[0].length == Consts.SUDOKU_GRID_SIZE) {
            mGrid = grid;
            mSubgrids = new SudokuSubgrid[Consts.SUDOKU_SUBGRID_SIZE][Consts.SUDOKU_SUBGRID_SIZE];

            // generate subgrids
            for (int i = 0; i < Consts.SUDOKU_GRID_SIZE / Consts.SUDOKU_SUBGRID_SIZE; i++) {
                for (int j = 0; j < Consts.SUDOKU_GRID_SIZE / Consts.SUDOKU_SUBGRID_SIZE; j++) {
                    int[][] subgrid = new int[Consts.SUDOKU_SUBGRID_SIZE][Consts.SUDOKU_SUBGRID_SIZE];
                    for (int k = 0; k < Consts.SUDOKU_SUBGRID_SIZE; k++) {
                        for (int l = 0; l < Consts.SUDOKU_SUBGRID_SIZE; l++) {
                            subgrid[k][l] = mGrid[i * Consts.SUDOKU_SUBGRID_SIZE + k][j * Consts.SUDOKU_SUBGRID_SIZE + l];
                        }
                    }
                    mSubgrids[i][j] = new SudokuSubgrid(subgrid);
                }
            }
        } else {
            Log.e(this.getClass().getSimpleName(), "Illegal grid size");
        }
    }

    public boolean isValidRow(int row) {
        Set<Integer> possibleDigits = new HashSet<Integer>(Consts.ALLOWED_DIGITS);
        for (int i : mGrid[row]) {
            if (i == 0 || possibleDigits.contains(i)) {
                possibleDigits.remove(i);
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean isValidColumn(int column) {
        Set<Integer> possibleDigits = new HashSet<Integer>(Consts.ALLOWED_DIGITS);
        for (int i = 0; i < Consts.SUDOKU_GRID_SIZE; i++) {
            if (mGrid[i][column] == 0 || (possibleDigits.contains(mGrid[i][column]))) {
                possibleDigits.remove(mGrid[i][column]);
            } else {
                return false;
            }
        }
        return true;
    }

    public SudokuSubgrid getSubgrid(int row, int column) {
        return mSubgrids[row][column];
    }

    public void calculateVariants() {

    }
}
