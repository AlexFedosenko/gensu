package kpi.fiot.gensu.core;

import java.util.HashSet;

public class SolveHelper {

    private HashSet[][] mVariants;



    public SudokuGrid solveWithOpenDigits(SudokuGrid grid) {

        while (grid.hasUnambiguousChoice()) {
            mVariants = grid.getVariants();

            for (int i = 0; i < mVariants.length; i++) {
                for (int j = 0; j < mVariants[i].length; j++) {
                    if (mVariants[i][j] != null && mVariants[i][j].size() == 1) {
                        grid.setGridCellValue((Integer)mVariants[i][j].toArray()[0], i, j);
                    }
                }
            }
        }

        return grid;
    }


}
