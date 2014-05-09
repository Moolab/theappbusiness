package com.theappbusiness.whoswho.helper;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageRounderHelper {
	
	/**
	 * Round Photo
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        
    	/*
    	 * Create a bitmap with 8888 ARGB. And init the canvas.
    	 */
    	Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        /*
         * Making the circle transparent
         */
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        /*
         * Return the original bitmap with overlay of circle.
         */
        
        return output;
    }
}
