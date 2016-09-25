package org.softwarelibre.tapleau.haptic.symbols;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class SymbolUtilities {

    public static Bitmap getHapticMap(Context c, String syllable) {
        return drawTextToBitmap(c, syllable, Color.rgb(255, 255, 255), Color.rgb(0, 0, 0));
    }

    public static Bitmap punchAHoleInABitmap(Context context, Bitmap foreground, float x1, float y1) {
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(foreground, 0, 0, paint);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float radius = (float)(getScreenSize(context).x *.06);
        canvas.drawCircle(x1, y1 - 450, radius, paint);
        return bitmap;
    }

    public static Bitmap combineTwoBitmaps(Bitmap background, Bitmap foreground) {
        Bitmap combinedBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(foreground, 0, 0, paint);
        return combinedBitmap;
    }

    public static Point getScreenSize(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Bitmap drawTextToBitmap(Context gContext,
                                   String gText, int frontColor, int backColor) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        int w = 1536, h = 1280;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
        android.graphics.Bitmap.Config bitmapConfig = bmp.getConfig();
        Canvas canvas = new Canvas(bmp);

        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(frontColor);
        // text size in pixels
        paint.setTextSize((int) (400 * scale));
        // text shadow
        //paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        if (backColor != -1) {
            canvas.drawColor(backColor);
        }
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bmp.getWidth() - bounds.width())/2;
        int y = (bmp.getHeight() + bounds.height())/2;
        canvas.drawText(gText, x, y, paint);
        return bmp;
    }
}
