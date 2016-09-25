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

    public boolean[] textToBraille(char brailleC) {
        boolean[] lexLuthor = new boolean[6];
        switch(brailleC) {
            case '!': {
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case '\"': {
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case '#': {
                lexLuthor[1] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case '&': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case '\'': {
                lexLuthor[4] = true;
            }break;case '(': {
                lexLuthor[2] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case ')': {
                lexLuthor[3] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case '*': {
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case ',': {
                lexLuthor[2] = true;
            }break;case '-': {
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case '.': {
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[5] = true;
            }break;case '/': {
                lexLuthor[1] = true;
                lexLuthor[4] = true;
            }break;case '0': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case '1': {
                lexLuthor[0] = true;
            }break;case '2': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
            }break;case '3': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
            }break;case '4': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[3] = true;
            }break;case '5': {
                lexLuthor[0] = true;
                lexLuthor[3] = true;
            }break;case '6': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
            }break;case '7': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case '8': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case '9': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
            }break;case ':': {
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case ';': {
                lexLuthor[2] = true;
                lexLuthor[4] = true;
            }break;case '?': {
                lexLuthor[2] = true;
                lexLuthor[5] = true;
            }break;case 'A': {
                lexLuthor[0] = true;
            }break;case 'B': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
            }break;case 'C': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
            }break;case 'D': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[3] = true;
            }break;case 'E': {
                lexLuthor[0] = true;
                lexLuthor[3] = true;
            }break;case 'F': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
            }break;case 'G': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case 'H': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case 'I': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
            }break;case 'J': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case 'K': {
                lexLuthor[0] = true;
                lexLuthor[4] = true;
            }break;case 'L': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
                lexLuthor[4] = true;
            }break;case 'M': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[4] = true;
            }break;case 'N': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 'O': {
                lexLuthor[0] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 'P': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[4] = true;
            }break;case 'Q': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 'R': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 'S': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[4] = true;
            }break;case 'T': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 'U': {
                lexLuthor[0] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case 'V': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case 'W': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[5] = true;
            }break;case 'X': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case 'Y': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case 'Z': {
                lexLuthor[0] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case 'a': {
                lexLuthor[0] = true;
            }break;case 'b': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
            }break;case 'c': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
            }break;case 'd': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[3] = true;
            }break;case 'e': {
                lexLuthor[0] = true;
                lexLuthor[3] = true;
            }break;case 'f': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
            }break;case 'g': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case 'h': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case 'i': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
            }break;case 'j': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
            }break;case 'k': {
                lexLuthor[0] = true;
                lexLuthor[4] = true;
            }break;case 'l': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
                lexLuthor[4] = true;
            }break;case 'm': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[4] = true;
            }break;case 'n': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 'o': {
                lexLuthor[0] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 'p': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[4] = true;
            }break;case 'q': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 'r': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 's': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[4] = true;
            }break;case 't': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
            }break;case 'u': {
                lexLuthor[0] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case 'v': {
                lexLuthor[0] = true;
                lexLuthor[2] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case 'w': {
                lexLuthor[1] = true;
                lexLuthor[2] = true;
                lexLuthor[3] = true;
                lexLuthor[5] = true;
            }break;case 'x': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case 'y': {
                lexLuthor[0] = true;
                lexLuthor[1] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case 'z': {
                lexLuthor[0] = true;
                lexLuthor[3] = true;
                lexLuthor[4] = true;
                lexLuthor[5] = true;
            }break;case '~': {
                lexLuthor[4] = true;
            }break;
        }

        return lexLuthor;
    }
}
