package kpi.fiot.gensu;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import kpi.fiot.gensu.core.SolveHelper;
import kpi.fiot.gensu.core.SudokuGrid;
import kpi.fiot.gensu.utils.Consts;
import kpi.fiot.gensu.utils.Utility;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener{

    private static final String TAG = Consts.TAG;

    private static final int TAKE_PICTURE = 1;

    private final int[][] test = // #098
            {{0, 0, 6, 0, 9, 0, 1, 0, 0},
            {5, 0, 2, 0, 0, 0, 4, 0, 9},
            {4, 0, 0, 6, 0, 3, 0, 0, 7},
            {0, 2, 0, 8, 0, 7, 0, 9, 0},
            {0, 0, 8, 0, 0, 0, 2, 0, 0},
            {0, 4, 0, 5, 0, 6, 0, 1, 0},
            {1, 0, 0, 7, 0, 2, 0, 0, 4},
            {3, 0, 7, 0, 0, 0, 6, 0, 2},
            {0, 0, 4, 0, 6, 0, 3, 0, 0}};
    private final int[][] test1 = // habrahabr
            {{0, 2, 6, 5, 0, 0, 0, 9, 0},
            {5, 0, 0, 0, 7, 9, 0, 0, 4},
            {3, 0, 0, 0, 1, 0, 0, 0, 0},
            {6, 0, 0, 0, 0, 0, 8, 0, 7},
            {0, 7, 5, 0, 2, 0, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 4, 0, 0},
            {0, 0, 0, 3, 0, 8, 9, 0, 2},
            {7, 0, 0, 0, 6, 0, 0, 4, 0},
            {0, 3, 0, 2, 0, 0, 1, 0, 0}};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // test
        SudokuGrid grid = new SudokuGrid(test1);
        for (int i = 0; i < Consts.SUDOKU_GRID_SIZE; i++) {
            Log.d(TAG, "row " + i + " is valid " + grid.isValidRow(i));
            Log.d(TAG, "column " + i + " is valid " + grid.isValidColumn(i));
            Log.d(TAG, "subgrid " + i + " is valid " + grid.getSubgrid(i / 3, i % 3).isSubgridValid());
        }

        // TODO: Holly shit, what's going on. What the hell. Rewrite it. Seriously

        SolveHelper helper = new SolveHelper();
//        helper.solveWithOpenDigits(grid);
        helper.solveWithHiddenDigits(grid);

        // test image rotate
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        Matrix matrix = new Matrix();

        matrix.postRotate(17);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, bitmapOrg.getWidth(), bitmapOrg.getHeight(), true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        ImageView view = (ImageView) findViewById(R.id.image);
        view.setImageBitmap(rotatedBitmap);

        Button btnPhoto = (Button) findViewById(R.id.btn_photo);
        btnPhoto.setOnClickListener(this);

        File photo = new File(Environment.getExternalStorageDirectory(),  "Sudoku.jpg");
        Uri selectedImage = Uri.fromFile(photo);
        getContentResolver().notifyChange(selectedImage, null);
        final ImageView imageView = (ImageView) findViewById(R.id.image);
        ContentResolver cr = getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media
                    .getBitmap(cr, selectedImage);

            final Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);

            imageView.setImageBitmap(bitmap1);
            Handler handler = new Handler(getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    imageView.setImageBitmap(Utility.convertToMonochrome(bitmap1));
                }
            }, 0);
            Toast.makeText(this, selectedImage.toString(),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                    .show();
            Log.e("Camera", e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_photo:
                takePhoto(findViewById(R.id.image));
        }
    }


    public void takePhoto(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(),  "Sudoku.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    File photo = new File(Environment.getExternalStorageDirectory(),  "Sudoku.jpg");
                    Uri selectedImage = Uri.fromFile(photo);
                    getContentResolver().notifyChange(selectedImage, null);
                    final ImageView imageView = (ImageView) findViewById(R.id.image);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        final Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);

                        imageView.setImageBitmap(bitmap);
                        Handler handler = new Handler(getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                imageView.setImageBitmap(Utility.convertToMonochrome(bitmap1));
                            }
                        }, 10000);
                        Toast.makeText(this, selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
   }


}
