package nl.klokhet.app.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
//import org.holoeverywhere.widget.ListPopupWindow;
//import org.holoeverywhere.widget.ListView;
//import org.holoeverywhere.widget.Spinner;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import nl.klokhet.app.R;

public class CustomSpinner extends android.support.v7.widget.AppCompatSpinner {
    public CustomSpinner(Context context)
    {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyle, int mode)
    {
        super(context, attrs, defStyle, mode);
    }

    public CustomSpinner(Context context, int mode)
    {
        super(context, mode);
    }

    @Override
    public boolean performClick()
    {
        boolean bClicked = super.performClick();

        try
        {
            Field mPopupField = Spinner.class.getDeclaredField("mPopup");
            mPopupField.setAccessible(true);
            Display display = super.getDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            ListPopupWindow pop = (ListPopupWindow) mPopupField.get(this);

            ListView listview = pop.getListView();

            Field mScrollCacheField = View.class.getDeclaredField("mScrollCache");
            mScrollCacheField.setAccessible(true);
            Object mScrollCache = mScrollCacheField.get(listview);
            Field scrollBarField = mScrollCache.getClass().getDeclaredField("scrollBar");
            scrollBarField.setAccessible(true);
            Object scrollBar = scrollBarField.get(mScrollCache);
            Method method = scrollBar.getClass().getDeclaredMethod("setVerticalThumbDrawable", Drawable.class);
            method.setAccessible(true);
//            method.invoke(scrollBar, getResources().getDrawable(R.drawable.scrollbar_style));

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                Field mVerticalScrollbarPositionField = View.class.getDeclaredField("mVerticalScrollbarPosition");
                mVerticalScrollbarPositionField.setAccessible(true);
                mVerticalScrollbarPositionField.set(listview, SCROLLBAR_POSITION_LEFT);
            }
//            pop.setWidth(width-50);
//            pop.setHeight(height-20);
//            listview.setLayoutParams(new FrameLayout.LayoutParams(width-50, FrameLayout.LayoutParams.WRAP_CONTENT));
//            listview.invalidate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return bClicked;
    }
}