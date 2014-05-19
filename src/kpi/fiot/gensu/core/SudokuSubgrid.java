package kpi.fiot.gensu.core;

import android.util.Log;
import kpi.fiot.gensu.utils.Consts;

import java.util.HashSet;
import java.util.Set;

public class SudokuSubgrid {

    private int[][] mSubgrid;

    public SudokuSubgrid(int[][] grid) {
        if (grid.length == Consts.SUDOKU_SUBGRID_SIZE && grid[0].length == Consts.SUDOKU_SUBGRID_SIZE) {
            mSubgrid = grid;
        } else {
            Log.e(this.getClass().getSimpleName(), "Illegal grid size");
        }
    }

    public boolean isSubgridValid() {
        Set<Integer> possibleDigits = new HashSet<Integer>(Consts.ALLOWED_DIGITS);
        for (int i = 0; i < Consts.SUDOKU_SUBGRID_SIZE; i++) {
            for (int j = 0; j < Consts.SUDOKU_SUBGRID_SIZE; j++) {
                if (mSubgrid[i][j] == 0 || possibleDigits.contains(mSubgrid[i][j])) {
                    possibleDigits.remove(mSubgrid[i][j]);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

}
