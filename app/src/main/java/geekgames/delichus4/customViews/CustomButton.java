package geekgames.delichus4.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import geekgames.delichus4.R;

/**
 * Created by Jose on 17/05/2015.
 */


public class CustomButton extends Button implements OnClickListener {

    public boolean estado = false;

    public CustomButton(Context context) {
        super(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        estado = !estado;
        Log.i("FUCKING DEBUG", "estado cambiado a: " + estado);
        if( estado ){
            v.setBackground(this.getResources().getDrawable(R.color.naranja));
        }
        else{
            v.setBackground(this.getResources().getDrawable(R.color.verde));
        }
    }
}
