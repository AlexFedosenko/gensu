package kpi.fiot.gensu.core;

import android.util.Log;
import kpi.fiot.gensu.utils.Consts;

import java.util.HashSet;
import java.util.Set;

public class SudokuGrid {

    private int[][] mGrid;
    private SudokuSubgrid[][] mSubgrids;
    private HashSet[][] mCandidates;

    public SudokuGrid(int[][] grid) {
        if (grid.length == Consts.SUDOKU_GRID_SIZE && grid[0].length == Consts.SUDOKU_GRID_SIZE) {
            mGrid = grid;
            mSubgrids = new SudokuSubgrid[Consts.SUDOKU_SUBGRID_SIZE][Consts.SUDOKU_SUBGRID_SIZE];

            generateSubgrids();
        } else {
            Log.e(this.getClass().getSimpleName(), "Illegal grid size");
        }
    }

    public void generateSubgrids() {
        for (int i = 0; i < Consts.SUDOKU_GRID_SIZE / Consts.SUDOKU_SUBGRID_SIZE; i++) {
            for (int j = 0; j < Consts.SUDOKU_GRID_SIZE / Consts.SUDOKU_SUBGRID_SIZE; j++) {
                int[][] subgrid = new int[Consts.SUDOKU_SUBGRID_SIZE][Consts.SUDOKU_SUBGRID_SIZE];
                HashSet[][] candidates = new HashSet[Consts.SUDOKU_SUBGRID_SIZE][Consts.SUDOKU_SUBGRID_SIZE];
                for (int k = 0; k < Consts.SUDOKU_SUBGRID_SIZE; k++) {
                    for (int l = 0; l < Consts.SUDOKU_SUBGRID_SIZE; l++) {
                        subgrid[k][l] = mGrid[i * Consts.SUDOKU_SUBGRID_SIZE + k][j * Consts.SUDOKU_SUBGRID_SIZE + l];
                        if (mCandidates != null) {
                            candidates[k][l] = mCandidates[i * Consts.SUDOKU_SUBGRID_SIZE + k][j * Consts.SUDOKU_SUBGRID_SIZE + l];
                        }
                    }
                }
                mSubgrids[i][j] = new SudokuSubgrid(subgrid);
                mSubgrids[i][j].setCandidates(candidates);
            }
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

    /**
     * Check validity before variants calculating
     */
    public void calculateCandidates() {
        mCandidates = new HashSet[Consts.SUDOKU_GRID_SIZE][Consts.SUDOKU_GRID_SIZE];
        for (int i = 0; i < Consts.SUDOKU_GRID_SIZE; i++) {
            for (int j = 0; j < Consts.SUDOKU_GRID_SIZE; j++) {
                if (mGrid[i][j] == 0) {
                    // calculate variants for certain cell
                    Set<Integer> variants = new HashSet<Integer>(Consts.ALLOWED_DIGITS);
                    variants = calculateCelVariants(variants, i, j);
                    variants = mSubgrids[i / 3][j / 3].calculateCelVariants(variants);
                    if (variants.isEmpty()) {
                        Log.d(Consts.TAG, "No variants, sudoku is unresolvable");
                    }
                    mCandidates[i][j] = (HashSet) variants;
                } else {
                    mCandidates[i][j] = null;
                }
            }
        }
    }

    public Set<Integer> calculateCelVariants(Set<Integer> allowedDigits, int row, int column) {
        for (int i = 0; i < Consts.SUDOKU_GRID_SIZE; i++) {
            for (int j = 0; j < Consts.SUDOKU_GRID_SIZE; j++) {
                if (i == row || j == column) {
                    allowedDigits.remove(mGrid[i][j]);
                }
            }
        }
        return allowedDigits;
    }

    public HashSet[][] getCandidates() {
        return mCandidates;
    }

    public void setGridCellValue(int value, int row, int column) {
        if (mGrid[row][column] == 0) {
            mGrid[row][column] = value;
            generateSubgrids();
        } else {
            Log.e(Consts.TAG, "Illegal action: cannot set new value to existing cell");
        }
    }

    public boolean hasUnambiguousChoice() {
        calculateCandidates();

        for (int i = 0; i < mCandidates.length; i++) {
            for (int j = 0; j < mCandidates[i].length; j++) {
                if (mCandidates[i][j] != null && mCandidates[i][j].size() == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Integer> getUniqueCandidates(int row, int column) {
        if (mCandidates == null || mCandidates[row][column] == null) {
            return null;
        }

        Set<Integer> uniqueCandidates = new HashSet<Integer>(mCandidates[row][column]);

//        for (int i = 0; i < Consts.SUDOKU_GRID_SIZE; i++) {
//            if (i != row && mCandidates[i][column] != null) {
//                uniqueCandidates.removeAll(mCandidates[i][column]);
//            }
//        }
//
//        for (int i = 0; i < Consts.SUDOKU_GRID_SIZE; i++) {
//            if (i != column && mCandidates[row][i] != null) {
//                uniqueCandidates.removeAll(mCandidates[row][i]);
//            }
//        }

        uniqueCandidates = mSubgrids[row / 3][column / 3].getUniqueCandidates(uniqueCandidates, row % 3, column % 3);

        return uniqueCandidates;
    }

}
