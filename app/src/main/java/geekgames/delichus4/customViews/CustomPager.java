package geekgames.delichus4.customViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Akira on 24/04/2015.
 */
public class CustomPager extends ViewPager {
    private boolean onsteps = false;

    public CustomPager(Context context) {
        super(context);
    }

    public CustomPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        if(this.onsteps){
            return super.onInterceptTouchEvent(event);
        }else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        if(this.onsteps){
            return super.onTouchEvent(event);
        }else {
            return false;
        }
    }

    public void enableSwipe(boolean cambio){
        this.onsteps = cambio;
    }
}
