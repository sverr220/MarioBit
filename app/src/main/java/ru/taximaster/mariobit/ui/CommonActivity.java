package ru.taximaster.mariobit.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

public class CommonActivity extends Activity {

    protected Typeface getTypefaceBtn() {
        return Typefaces.get(this, "fonts/btn_font.ttf");
    }

    protected Typeface getTypefaceContent() {
        return Typefaces.get(this, "fonts/content_font.ttf");
    }

    public static class Typefaces {
        private static final String TAG = "Typefaces";

        private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

        static Typeface get(Context c, String assetPath) {
            synchronized (cache) {
                if (!cache.containsKey(assetPath)) {
                    try {
                        Typeface t = Typeface.createFromAsset(c.getAssets(),
                                assetPath);
                        cache.put(assetPath, t);
                    } catch (Exception e) {
                        Log.e(TAG, "Could not get typeface '" + assetPath
                                + "' because " + e.getMessage());
                        return null;
                    }
                }
                return cache.get(assetPath);
            }
        }
    }
}
