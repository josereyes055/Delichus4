package geekgames.delichus4.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import geekgames.delichus4.R;
import geekgames.delichus4.Receta;
import geekgames.delichus4.customObjects.Ingrediente;

public class IngredienteAdapter extends ArrayAdapter<Ingrediente>{

    private Context idkContext ;

    public IngredienteAdapter(Context context){
        super(context, R.layout.ingrediente);
        idkContext = context;
    }

    public void swapRecords(List<Ingrediente> objects) {
        clear();

        for(Ingrediente object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingrediente, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        final Ingrediente ingreRecord = getItem(position);

        TextView nombre = (TextView) convertView.findViewById(R.id.ingrediente_nombre);
        TextView cantidad = (TextView) convertView.findViewById(R.id.ingrediente_cantidad);
        TextView medida = (TextView) convertView.findViewById(R.id.ingrediente_unidad);

        nombre.setText(" "+ingreRecord.nombre);
        cantidad.setText(ingreRecord.cantidad.toString());
        medida.setText(ingreRecord.medida);



        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                    Receta activity = (Receta)idkContext;
                                                    activity.validateCheckbox();
                                                }
                                            }
        );


        return convertView;
    }

}
