package geekgames.delichus4.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import geekgames.delichus4.R;
import geekgames.delichus4.customObjects.Paso;

public class PasoAdapter extends ArrayAdapter<Paso> {


    private Context idkContext ;

    public PasoAdapter(Context context){
        super(context, R.layout.receta_paso);
        idkContext = context;
    }

    public void swapRecipeRecords(List<Paso> objects) {
        clear();

        for(Paso object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.receta_paso, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        final Paso stepRecord = getItem(position);

        TextView tipo = (TextView) convertView.findViewById(R.id.paso_tipo);
        TextView descripcion = (TextView) convertView.findViewById(R.id.paso_descripcion);
        TextView tiempo = (TextView) convertView.findViewById(R.id.recipe_autor);

        tipo.setText("paso tipo: " + stepRecord.getTipo());
        descripcion.setText(stepRecord.getDescripcion());
        tiempo.setText("tiempo: "+ stepRecord.getTiempo());


        return convertView;
    }

}
