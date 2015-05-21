package geekgames.delichus4.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

import geekgames.delichus4.R;
import geekgames.delichus4.Receta;
import geekgames.delichus4.customObjects.Ingrediente;

public class SelectorIngredienteAdapter extends ArrayAdapter<Ingrediente>{

    private Context idkContext ;

    public SelectorIngredienteAdapter(Context context){
        super(context, R.layout.selector_ingrediente);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.selector_ingrediente, parent, false);
        }

        // NOTE: You would normally use the ViewHolder pattern here
        final Ingrediente ingreRecord = getItem(position);

        TextView nombre = (TextView) convertView.findViewById(R.id.nombre_selector_ingrediente);
        NumberPicker entero = (NumberPicker) convertView.findViewById(R.id.picker_ingrediente_entero);
        NumberPicker fraccion = (NumberPicker) convertView.findViewById(R.id.picker_ingrediente_fraccion);

        nombre.setText(" " + ingreRecord.nombre);
        entero.setMaxValue(100);
        entero.setMinValue(1);
        fraccion.setMaxValue(1);
        fraccion.setMinValue(0);

        entero.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ingreRecord.cantidad = (double)newVal;
                Log.i("FUCKING DEBUG", "asignado" + ingreRecord.cantidad);
            }
        });
        fraccion.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ingreRecord.cantidad += (double)newVal;
                Log.i("FUCKING DEBUG", "sumado-" + ingreRecord.cantidad);

            }
        });

        return convertView;
    }

}
