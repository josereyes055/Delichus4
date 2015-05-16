package geekgames.delichus4.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import geekgames.delichus4.R;
import geekgames.delichus4.customObjects.Logro;


public class LogroAdapter extends ArrayAdapter<Logro> {

    private Context idkContext ;

    public LogroAdapter(Context context){
        super(context, R.layout.lista_logros);
        idkContext = context;
    }

    public void swapRecipeRecords(List<Logro> objects) {
        clear();

        for(Logro object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Logro logro = getItem(position);


        // Si el id es -1 entonces es un header
        if(logro.getId() == -1) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header, parent, false);
            TextView nombre = (TextView) convertView.findViewById(R.id.separator);
            nombre.setText(logro.getNombre());

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_logros, parent, false);

            //NOTE: You would normally use the ViewHolder pattern here
            TextView nombre = (TextView) convertView.findViewById(R.id.logro_nombre);
            ImageView imagen = (ImageView) convertView.findViewById(R.id.logro_imagen);
            TextView titulo = (TextView) convertView.findViewById(R.id.logro_titulo);
            TextView descripcion = (TextView) convertView.findViewById(R.id.logro_descripcion);

            nombre.setText(logro.getNombre());
            titulo.setText(logro.getTitulo());
            descripcion.setText(logro.getDescripcion());
            if(logro.isCompleted()){
                imagen.setImageResource(R.drawable.logro_completo);
            }
        }

        return convertView;
    }
}
