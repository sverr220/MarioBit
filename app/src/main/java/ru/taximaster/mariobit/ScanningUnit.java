package ru.taximaster.mariobit;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ScanningUnit {

    private Context context;
    private String resourceName;

    public ScanningUnit(Context context, String resourceName) {
        super();
        this.context = context;
        this.resourceName = resourceName;
    }

    public int getRaw() {
        return getIdentifier(resourceName, "raw");
    }

    public Drawable getDrawable() {
        int res = getIdentifier(resourceName, "drawable");
        return context.getResources().getDrawable(res);
    }

    public boolean isExistHint() {
        int stringId = getIdentifier(resourceName + "_hint", "string");
        return stringId > 0;
    }

    public int getHint() {
        return getIdentifier(resourceName + "_hint", "string");
    }

    public int getName() {
        return getIdentifier(resourceName, "string");
    }

    public boolean isRepeate() {
        return resourceName.contains("looped");
    }

    public static boolean isExist(Context context, final String resource) {
        try {
            int resId = getIdentifier(context, resource, "string");
            return resId > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private int getIdentifier(final String resource, final String type) {
        return getIdentifier(context, resource, type);
    }

    private static int getIdentifier(Context context, final String resource, final String type) {
        try {
            return context.getResources().getIdentifier(resource, type, context.getPackageName());
        } catch (Exception e) {
            return 0;
        }
    }
}
