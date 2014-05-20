package kpi.fiot.gensu.core;

import kpi.fiot.gensu.utils.Consts;

import java.util.HashSet;

public class SolveHelper {

    private HashSet[][] mCandidates;



    public SudokuGrid solveWithOpenDigits(SudokuGrid grid) {

        while (grid.hasUnambiguousChoice()) {
            mCandidates = grid.getCandidates();

            for (int i = 0; i < mCandidates.length; i++) {
                for (int j = 0; j < mCandidates[i].length; j++) {
                    if (mCandidates[i][j] != null && mCandidates[i][j].size() == 1) {
                        grid.setGridCellValue((Integer) mCandidates[i][j].toArray()[0], i, j);
                    }
                }
            }
        }

        return grid;
    }

    public SudokuGrid solveWithHiddenDigits(SudokuGrid grid) {
        grid.calculateCandidates();
        grid.generateSubgrids();
        mCandidates = grid.getCandidates();

        for (int i = 0; i < Consts.SUDOKU_GRID_SIZE; i++) {
            for (int j = 0; j < Consts.SUDOKU_GRID_SIZE; j++) {
                if (mCandidates[i][j] != null) {
                    if (!grid.getUniqueCandidates(i, j).isEmpty()) {
                        grid.getUniqueCandidates(i, j);
                    }
                }
            }
        }

        return grid;
    }

}
