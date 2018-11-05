package com.nerallan.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Nerallan on 11/5/2018.
 */

public class PictureUtils {
    public static Bitmap getScaledBitmap(String pPath, int pDestWidth, int pDestHeight){
        // read image size on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pPath, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // calculate scaling
        // inSampleSize determines the sample size for each pixel of the original image: a sample with a size of 1
        // contains one horizontal pixel for each horizontal pixel of the source file, and a sample with a size of 2
        // contains one horizontal pixel for every two horizontal pixels of the source file. Thus, if the inSampleSize value is 2,
        // the number of pixels in the image is a quarter of the number of pixels in the original.
        int inSampleSize = 1;
        if (srcHeight > pDestHeight || srcWidth > pDestWidth){
            if (srcWidth > srcHeight){
                inSampleSize = Math.round(srcHeight / pDestHeight);
            } else {
                inSampleSize = Math.round(srcWidth / pDestWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        // reading data and creating a final image
        return BitmapFactory.decodeFile(pPath, options);
    }

    // when the fragment is started, you still do not know the value of PhotoView.
    // Before processing the layout, screen dimensions not exist. The first pass of this processing occurs after
    // the execution of onCreate (...), onStart () and onResume (), therefore PhotoView does not know its size.
    // This method scales Bitmap to the size of a particular activity.
    public static Bitmap getScaledBitmap(String pPath, Activity pActivity){
        Point size = new Point();
        // checks the screen size and reduces the image to that size.
        pActivity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(pPath, size.x, size.y);
    }
}
