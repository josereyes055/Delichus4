package geekgames.delichus4.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
        final ImageView torta = (ImageView)convertView.findViewById(R.id.torta);

        nombre.setText(" " + ingreRecord.nombre);
        entero.setMaxValue(100);
        entero.setMinValue(1);
        fraccion.setMinValue(0);
        fraccion.setMaxValue(5);
        fraccion.setDisplayedValues( new String[] { "0", "1/4","1/3", "1/2","2/3", "3/4" } );

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
                switch(oldVal){
                    case 1:
                        ingreRecord.cantidad -= 0.25;
                        break;
                    case 2:
                        ingreRecord.cantidad -= 0.33;
                        break;
                    case 3:
                        ingreRecord.cantidad -= 0.5;
                        break;
                    case 4:
                        ingreRecord.cantidad -= 0.66;
                        break;
                    case 5:
                        ingreRecord.cantidad -= 0.75;
                        break;
                }
                switch (newVal){
                    case 0:
                        torta.setImageResource(R.drawable.cero);
                        break;
                    case 1:
                        ingreRecord.cantidad += 0.25;
                        torta.setImageResource(R.drawable.un_cuarto);
                        break;
                    case 2:
                        ingreRecord.cantidad += 0.33;
                        torta.setImageResource(R.drawable.un_tercio);
                        break;
                    case 3:
                        ingreRecord.cantidad += 0.5;
                        torta.setImageResource(R.drawable.un_medio);
                        break;
                    case 4:
                        ingreRecord.cantidad += 0.66;
                        torta.setImageResource(R.drawable.dos_tercios);
                        break;
                    case 5:
                        ingreRecord.cantidad += 0.75;
                        torta.setImageResource(R.drawable.tres_cuartos);
                        break;
                }
                Log.i("FUCKING DEBUG", "asignado" + ingreRecord.cantidad);

            }
        });

        return convertView;
    }

}
