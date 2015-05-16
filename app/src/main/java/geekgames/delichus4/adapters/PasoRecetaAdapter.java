package geekgames.delichus4.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import geekgames.delichus4.R;
import geekgames.delichus4.customObjects.ImagenPaso;

public class PasoRecetaAdapter extends ArrayAdapter<ImagenPaso> {

    private Context idkContext ;

    public PasoRecetaAdapter(Context context){
        super(context, R.layout.imagen_paso);
        idkContext = context;
    }

    public void swapRecords (List<ImagenPaso> objects) {
        clear();

        for(ImagenPaso object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.imagen_paso, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        final ImagenPaso imgPRecord = getItem(position);

        ImageView foto = (ImageView) convertView.findViewById(R.id.imagen_paso_u);
        if (imgPRecord.imagen.equalsIgnoreCase("empty")){
            Log.i("FUCKING DEBUG", "imagen vacia creada" );
            Drawable drw = convertView.getResources().getDrawable(R.drawable.img_paso_no_encontrado);
            foto.setImageDrawable(drw);
        }else {
            //Log.i("FUCKING DEBUG", "la lkmlk image del paso: " + imgPRecord.imagen);
            Picasso.with(idkContext).load(imgPRecord.imagen).fit().centerCrop().into(foto);
        }
        return convertView;
    }

}
