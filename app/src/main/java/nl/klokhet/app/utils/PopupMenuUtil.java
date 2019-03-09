package nl.klokhet.app.utils;

import android.annotation.SuppressLint;
import android.support.v7.widget.PopupMenu;

import java.lang.reflect.Method;

public class PopupMenuUtil {

    public static void enableIcon(PopupMenu popupMenu) {
        try {
            @SuppressLint("PrivateApi") Method method = popupMenu.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            method.setAccessible(true);
            method.invoke(popupMenu.getMenu(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
