package geekgames.delichus4.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import geekgames.delichus4.MainActivity;
import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.customObjects.Ficha;
import geekgames.delichus4.customObjects.MiniFicha;

public class MiniFichaAdapter extends ArrayAdapter<MiniFicha> {

    private Context idkContext ;
    public MiniFichaAdapter(Context context){
        super(context, R.layout.mini_ficha);
        idkContext = context;
    }

    public void swapRecords(List<MiniFicha> objects) {
        clear();

        for(MiniFicha object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MiniFicha unaFicha = getItem(position);

        if(unaFicha.id == -1) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header, parent, false);
            TextView nombre = (TextView) convertView.findViewById(R.id.separator);
            nombre.setText(unaFicha.nombre);

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mini_ficha, parent, false);

            TextView nombre = (TextView) convertView.findViewById(R.id.mini_recipe_nombre);
            ImageView imagen = (ImageView) convertView.findViewById(R.id.mini_recipe_imagen);
            TextView autor = (TextView) convertView.findViewById(R.id.mini_recipe_autor);
            RatingBar puntuacion = (RatingBar) convertView.findViewById(R.id.mini_recipe_puntuacion);



            nombre.setText(unaFicha.nombre);
            Picasso.with(idkContext).load(unaFicha.imagen)
                    .placeholder(R.drawable.img_no_encontrada_recomendado)
                    .error(R.drawable.img_no_encontrada_recetas)
                    .fit().centerCrop().into(imagen);
            autor.setText(unaFicha.autor);
            puntuacion.setRating(unaFicha.puntuacion);
        }


        return convertView;
    }

}
