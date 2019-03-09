package nl.klokhet.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class WindowUtils {

    private WindowUtils() {
        //
    }

    public static Point getScreenSize(Context context) {
        Point size = new Point();
        WindowManager windowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getSize(size);
        }
        return size;
    }

    public static Point getScreenRealSize(Context context) {
        Point size = new Point();
        WindowManager windowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getRealSize(size);
        }
        return size;
    }

    public static void hideStatusBar(Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void hideStatusBarAndNavigation(Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void showStatusBar(Window window) {
        showStatusBar(window, true);
    }


    public static void showStatusBar(Window window, boolean useStableLayoutFlag) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (VersionsUtils.isMarshmallowOrAbove()) {
            visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        if (useStableLayoutFlag) {
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }
        window.getDecorView().setSystemUiVisibility(visibility);

        if (VersionsUtils.isLollipopOrAbove()) {
            window.setStatusBarColor(Color.BLACK);
        }
    }

}
