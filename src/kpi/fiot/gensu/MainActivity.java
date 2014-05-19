package kpi.fiot.gensu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import kpi.fiot.gensu.core.SudokuGrid;
import kpi.fiot.gensu.utils.Consts;

public class MainActivity extends Activity {

    private final int[][] test =
            {{0, 0, 6, 0, 9, 0, 1, 0, 0},
            {5, 0, 2, 0, 0, 0, 4, 0, 9},
            {4, 0, 0, 6, 0, 3, 0, 0, 7},
            {0, 2, 0, 8, 0, 7, 0, 9, 0},
            {0, 0, 8, 0, 0, 0, 2, 0, 0},
            {0, 4, 0, 5, 0, 6, 0, 1, 0},
            {1, 0, 0, 7, 0, 2, 0, 0, 4},
            {3, 0, 7, 0, 0, 0, 6, 0, 2},
            {0, 0, 4, 0, 6, 0, 3, 0, 0}};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // test
        SudokuGrid grid = new SudokuGrid(test);
        for (int i = 0; i < Consts.SUDOKU_GRID_SIZE; i++) {
            Log.d("gensu", "row " + i + " is valid " +  grid.isValidRow(i));
            Log.d("gensu", "column " + i + " is valid " +  grid.isValidColumn(i));
        }
    }
}
