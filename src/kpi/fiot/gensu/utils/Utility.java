package kpi.fiot.gensu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Utility {

    private static int[][] mIntegralIntensity;

    public static Bitmap convertToMonochrome(Bitmap originalBitmap) {

        calculateIntegralIntensity(originalBitmap);
        int intensity;
        int limit;

        Bitmap bitmap = Bitmap.createBitmap(originalBitmap);
        for (int i = 0; i < originalBitmap.getHeight(); i++) {
            for (int j = 0; j < originalBitmap.getWidth(); j++){
                intensity = getPixelIntensity(originalBitmap.getPixel(j, i));
                if (i < Consts.LOCAL_RADIUS || j < Consts.LOCAL_RADIUS || i > originalBitmap.getHeight() - 1 - Consts.LOCAL_RADIUS || j > originalBitmap.getWidth() - 1 - Consts.LOCAL_RADIUS) {
                    limit = 128;
                } else {
                    limit = (mIntegralIntensity[i + Consts.LOCAL_RADIUS][j + Consts.LOCAL_RADIUS] + mIntegralIntensity[i - Consts.LOCAL_RADIUS][j - Consts.LOCAL_RADIUS]
                            - mIntegralIntensity[i - Consts.LOCAL_RADIUS][j + Consts.LOCAL_RADIUS] - mIntegralIntensity[i + Consts.LOCAL_RADIUS][j - Consts.LOCAL_RADIUS])
                            / ((Consts.LOCAL_RADIUS * 2 + 1) * (Consts.LOCAL_RADIUS * 2 + 1));
                }
                intensity = intensity < 128 ? Color.WHITE : Color.BLACK;
                bitmap.setPixel(j, i, intensity);
            }
        }

        return bitmap;
    }

    public static int getPixelIntensity(int color) {
        color = Math.abs(color);
        return (color / 256 / 256 + color / 256 % 256 + color % 256) / 3;
    }

    public static void calculateIntegralIntensity(Bitmap bitmap) {
        mIntegralIntensity = new int[bitmap.getHeight()][bitmap.getWidth()];
        int sum = 0;
        for (int i = 0; i < bitmap.getHeight(); i++) {
            sum = 0;
            for (int j = 0; j < bitmap.getWidth(); j++) {
                sum += getPixelIntensity(bitmap.getPixel(j, i));
                mIntegralIntensity[i][j] = sum;

            }
        }

        for (int i = 0; i < bitmap.getWidth(); i++) {
            sum = 0;
            for (int j = 0; j < bitmap.getHeight(); j++) {
                sum += mIntegralIntensity[j][i];
                mIntegralIntensity[j][i] = sum;
            }
        }
    }
}
