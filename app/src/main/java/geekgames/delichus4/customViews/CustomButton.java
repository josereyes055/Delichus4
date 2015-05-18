package geekgames.delichus4.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.apmem.tools.layouts.FlowLayout;

import geekgames.delichus4.R;

/**
 * Created by Jose on 17/05/2015.
 */


public class CustomButton extends Button implements OnClickListener {

    public boolean estado = false;
    public String label;
    public String id;

    public CustomButton(Context context) {
        super(context, null,  R.attr.customButtonStyle);

        init();
    }

    public CustomButton(Context context, int id, String text) {
        super(context, null,  R.attr.customButtonStyle);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs,  R.attr.customButtonStyle);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray array = context.obtainStyledAttributes( attrs,
                R.styleable.CustomButtonTheme, defStyleAttr,
                R.style.geekgames_delichus4_customViews_CustomButton ); // see below

        array.recycle();

        init();
    }

    private void init(){
        setOnClickListener(this);

        View v = getRootView();
        v.setBackground(this.getResources().getDrawable(R.drawable.custom_button_shape));
    }



    @Override
    public void onClick(View v) {
        estado = !estado;
        //Log.i("FUCKING DEBUG", "estado cambiado a: " + estado);
        if( estado ){
            v.setBackground(this.getResources().getDrawable(R.drawable.custom_button_shape_on));
        }
        else{
            v.setBackground(this.getResources().getDrawable(R.drawable.custom_button_shape));
        }
    }
}
