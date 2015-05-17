package geekgames.delichus4.fragments.busquedas;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import geekgames.delichus4.BusquedaFiltros;
import geekgames.delichus4.R;

public class ConfigFiltros extends Fragment {

    Button filtros;
    ConfigFiltros instance;

    NumberPicker cantidad;
    NumberPicker tiempo;
    NumberPicker categoria;
    NumberPicker tipo;
    NumberPicker ingrediente;
    NumberPicker coccion;
    //NumberPicker origen;
    //NumberPicker autor;

    public boolean[] chosen;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.config_filtros, container, false);


        filtros = (Button)rootView.findViewById(R.id.buttonConfigFiltros);
        instance = this;

        chosen = getArguments().getBooleanArray("chosen");
        Log.i("FUCKING DEBUG", getArguments().getString("prueba"));

        cantidad = (NumberPicker) rootView.findViewById(R.id.filtros_cantidad);
        tiempo = (NumberPicker) rootView.findViewById(R.id.filtros_tiempo);
        categoria = (NumberPicker) rootView.findViewById(R.id.filtros_categoria);
        tipo = (NumberPicker) rootView.findViewById(R.id.filtros_tipo);
        ingrediente = (NumberPicker) rootView.findViewById(R.id.filtros_ingrediente);
        coccion = (NumberPicker) rootView.findViewById(R.id.filtros_coccion);
        //origen = (NumberPicker) rootView.findViewById(R.id.filtros_origen);
        //autor = (NumberPicker) rootView.findViewById(R.id.filtros_autor);

        LinearLayout vcantidad = (LinearLayout) rootView.findViewById(R.id.selectorCantidad);
        LinearLayout vtiempo = (LinearLayout) rootView.findViewById(R.id.selectorTiempo);
        LinearLayout vcategoria = (LinearLayout) rootView.findViewById(R.id.selectorCategoria);
        LinearLayout vtipo = (LinearLayout) rootView.findViewById(R.id.selectorTipo);
        LinearLayout vingrediente = (LinearLayout) rootView.findViewById(R.id.selectorIngredientes);
        LinearLayout vcoccion = (LinearLayout) rootView.findViewById(R.id.selectorCoccion);

        if( !chosen[0]) vcantidad.setVisibility(View.GONE);
        if( !chosen[1]) vtiempo.setVisibility(View.GONE);
        if( !chosen[2]) vcategoria.setVisibility(View.GONE);
        if( !chosen[3]) vtipo.setVisibility(View.GONE);
        if( !chosen[4]) vingrediente.setVisibility(View.GONE);
        if( !chosen[5]) vcoccion.setVisibility(View.GONE);

        return rootView;
    }

    public void setValues(NumberPicker np, JSONObject json){
        String[] lista = new String[json.length()];

        Iterator<String> iterator = json.keys();
        int contador = 0;
        while(iterator.hasNext()){
            try {
                lista[contador] = json.getString(iterator.next());
            }catch (JSONException e){
                Log.e("FUCKING DEBUG", e.toString());
            }
            contador ++;
        }

        np.setMaxValue(lista.length-1);
        np.setMinValue(0);
        np.setDisplayedValues(lista);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //JSONObject tipo_plato = MainApplication.getInstance().tipo_plato;
        //JSONObject tipo_ingrediente = MainApplication.getInstance().tipo_ingrediente;
        //JSONObject tipo_coccion = MainApplication.getInstance().tipo_coccion;
        //JSONObject list_categoria = MainApplication.getInstance().categoria;
        //JSONObject list_origen = MainApplication.getInstance().getOrigen();

        cantidad.setMaxValue(10);
        cantidad.setMinValue(1);

        tiempo.setMaxValue(60);
        tiempo.setMinValue(0);

        //setValues(categoria, list_categoria);
       // setValues(tipo,tipo_plato );
        //setValues(ingrediente, tipo_ingrediente);
        //setValues(coccion, tipo_coccion);
        //setValues(origen, list_origen);









        filtros.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                /*int[] valores = parent.values;

                valores[0] = cantidad.getValue();
                valores[1] = tiempo.getValue();
                valores[2] = categoria.getValue();
                valores[3] = tipo.getValue();
                valores[4] = ingrediente.getValue();
                valores[5] = coccion.getValue();

                parent.values = valores;*/

            }});

    }

}
