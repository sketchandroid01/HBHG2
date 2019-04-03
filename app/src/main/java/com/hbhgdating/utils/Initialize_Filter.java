package com.hbhgdating.utils;

import android.graphics.Bitmap;

import com.hbhgdating.ImageFilter.filters.AlphaBlending;
import com.hbhgdating.ImageFilter.filters.BrownishFilter;
import com.hbhgdating.ImageFilter.filters.CreateSepia;
import com.hbhgdating.ImageFilter.filters.DoBrightness;
import com.hbhgdating.ImageFilter.filters.GaussianBlur;
import com.hbhgdating.ImageFilter.filters.HardLightMode;
import com.hbhgdating.ImageFilter.filters.Vignette;

import net.alhazmy13.imagefilter.ImageFilter;

/**
 * Created by Developer on 7/11/17.
 */

public class Initialize_Filter {


    public Initialize_Filter() {

    }


    public Bitmap updateFilter(Bitmap bitmap, int value) {
        switch (value) {
            case 1:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.GRAY);
            case 2:
                GaussianBlur gaussianBlur = new GaussianBlur(bitmap);
                return gaussianBlur.executeFilter();
            case 3:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.OLD);
            case 4:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SHARPEN);
            case 5:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.HDR);
            case 6:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SOFT_GLOW, 0.6);
            case 7:
                DoBrightness doBrightness = new DoBrightness(bitmap, 20);
                return doBrightness.executeFilter();
            case 8:
                AlphaBlending alphaBlending = new AlphaBlending(bitmap);
                return alphaBlending.executeFilter();
            case 9:
                BrownishFilter brownishFilter = new BrownishFilter(bitmap);
                return brownishFilter.executeFilter();
            case 10:
                CreateSepia createSepia = new CreateSepia(bitmap, 20);
                return createSepia.executeFilter();
            case 11:
                HardLightMode hardLightMode = new HardLightMode(bitmap);
                return hardLightMode.executeFilter();
            case 12:
                Vignette vignette = new Vignette(bitmap);
                return vignette.executeFilter();

            default:
                return bitmap;
        }


    }





    /*private Bitmap updateFilter2() {
        switch (value) {
            case 1:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.GRAY);
            case 2:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.RELIEF);
            case 3:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.AVERAGE_BLUR, 9);
            case 4:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.OIL, 10);
            case 5:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.NEON, 200, 50, 100);
            case 6:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.PIXELATE, 9);
            case 7:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.TV);
            case 8:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.INVERT);
            case 9:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.BLOCK);
            case 10:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.OLD);
            case 11:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SHARPEN);
            case 12:
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.LIGHT, width / 2, height / 2, Math.min(width / 2, height / 2));
            case 13:
                double radius = (bitmap.getWidth() / 2) * 95 / 100;
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.LOMO, radius);
            case 14:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.HDR);
            case 15:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.GAUSSIAN_BLUR, 1.2);
            case 16:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SOFT_GLOW, 0.6);
            case 17:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SKETCH);
            case 18:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.MOTION_BLUR, 5, 1);
            case 19:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.GOTHAM);
            default:
                return bitmap;
        }


    }*/

}
