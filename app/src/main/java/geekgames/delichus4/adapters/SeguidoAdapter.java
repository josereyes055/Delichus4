package geekgames.delichus4.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.customObjects.Seguido;

public class SeguidoAdapter extends ArrayAdapter<Seguido> {

    private Context idkContext ;

    public SeguidoAdapter(Context context){
        super(context, R.layout.min_user);
        idkContext = context;
    }

    public void swapRecords(List<Seguido> objects) {
        clear();

        for(Seguido object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Seguido seguidoRecord = getItem(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.min_user, parent, false);

            LinearLayout layou = (LinearLayout)convertView.findViewById(R.id.contenedor_mini_user);
            layou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MainApplication.getInstance().laReceta = unaFicha;
                    //Log.i("FUCKING DEBUG", "la receta es " + MainApplication.getInstance().laReceta.nombre);
                    MainApplication.getInstance().visitAutor(getContext(), seguidoRecord.id);
                }
            });

            // NOTE: You would normally use the ViewHolder pattern here
            TextView nombre = (TextView) convertView.findViewById(R.id.follow_nombre);
            ImageView imagen = (ImageView) convertView.findViewById(R.id.follow_imagen);
            TextView titulo = (TextView) convertView.findViewById(R.id.follow_titulo);
            RatingBar puntuacion = (RatingBar) convertView.findViewById(R.id.follow_rating);

            //SimpleRecipe recipeRecord = getItem(position);

            nombre.setText(seguidoRecord.nombre);
            Picasso.with(idkContext).load(seguidoRecord.foto).fit().centerCrop().into(imagen);
            titulo.setText(seguidoRecord.titulo);
            puntuacion.setRating( seguidoRecord.lvl );


        return convertView;
    }


}
