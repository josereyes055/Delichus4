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
import android.widget.SeekBar;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import geekgames.delichus4.BusquedaFiltros;
import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.customViews.CustomButton;

public class ConfigFiltros extends Fragment {

    Button filtros;
    ConfigFiltros instance;

    //NumberPicker cantidad;
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

        SeekBar seekPersonas = (SeekBar)rootView.findViewById(R.id.seekPersonas);
        SeekBar seekTiempo = (SeekBar)rootView.findViewById((R.id.seekTiempo));

        seekPersonas.setMax(20);
        seekTiempo.setMax(300);

        seekPersonas
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        TextView textPersonas = (TextView)getActivity().findViewById(R.id.numberPersonas);
                        textPersonas.setText(Integer.toString(progress));
                    }
                });

        seekTiempo
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        TextView textTiempo = (TextView)getActivity().findViewById(R.id.numberTiempo);
                        textTiempo.setText(Integer.toString(progress));
                    }
                });


        FlowLayout flowCategoria = (FlowLayout)rootView.findViewById(R.id.flowCategoria);
        FlowLayout flowCoccion = (FlowLayout)rootView.findViewById(R.id.flowCoccion);
        FlowLayout flowIngredientes = (FlowLayout)rootView.findViewById(R.id.flowIngredientes);
        FlowLayout flowTipo = (FlowLayout)rootView.findViewById(R.id.flowTipo);
        FlowLayout flowOrigen = (FlowLayout)rootView.findViewById(R.id.flowOrigen);

        String stringArrayCategoria = MainApplication.getInstance().sp.getString("categoria",null);
        String stringArrayCoccion = MainApplication.getInstance().sp.getString("tipo_coccion",null);
        String stringArrayIngredientes = MainApplication.getInstance().sp.getString("tipo_ingrediente",null);
        String stringArrayTipo = MainApplication.getInstance().sp.getString("tipo_plato",null);
        String stringArrayOrigen = MainApplication.getInstance().sp.getString("origen",null);

        JSONObject categorias;
        JSONObject cocciones;
        JSONObject tipoIngredientes;
        JSONObject tipoPlatos;
        JSONObject origen;
        try {
            categorias = new JSONObject(stringArrayCategoria);
            cocciones = new JSONObject(stringArrayCoccion);
            tipoIngredientes = new JSONObject(stringArrayIngredientes);
            tipoPlatos = new JSONObject(stringArrayTipo);
            origen = new JSONObject(stringArrayOrigen);

            setValues(flowCategoria, categorias);
            setValues(flowCoccion, cocciones);
            setValues(flowIngredientes,tipoIngredientes);
            setValues(flowTipo,tipoPlatos);
            setValues(flowOrigen,origen);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return rootView;
    }

    public void setValues(FlowLayout fl, JSONObject json){

        Iterator<String> iterator = json.keys();
        while(iterator.hasNext()){
            try {
                String key = iterator.next();
                String text = json.getString(key);
                CustomButton cb = new CustomButton(getActivity());
                cb.setText(text);
                cb.id = key;
                cb.label = text;
                //Log.i("FUCKING DEBUG","botoncreado, id: "+cb.id+" label: "+cb.label);
                fl.addView(cb);
            }catch (JSONException e){
                Log.e("FUCKING DEBUG", e.toString());
            }
        }


    }

    public void setViews( boolean[] chosen ){
        LinearLayout vcantidad = (LinearLayout) rootView.findViewById(R.id.selectorCantidad);
        LinearLayout vtiempo = (LinearLayout) rootView.findViewById(R.id.selectorTiempo);
        LinearLayout vcategoria = (LinearLayout) rootView.findViewById(R.id.selectorCategoria);
        LinearLayout vtipo = (LinearLayout) rootView.findViewById(R.id.selectorTipo);
        LinearLayout vingrediente = (LinearLayout) rootView.findViewById(R.id.selectorIngredientes);
        LinearLayout vcoccion = (LinearLayout) rootView.findViewById(R.id.selectorCoccion);
        LinearLayout vorigen =(LinearLayout) rootView.findViewById((R.id.selectorOrigen));

        if( !chosen[0]) vcantidad.setVisibility(View.GONE);
        if( !chosen[1]) vtiempo.setVisibility(View.GONE);
        if( !chosen[2]) vcategoria.setVisibility(View.GONE);
        if( !chosen[3]) vtipo.setVisibility(View.GONE);
        if( !chosen[4]) vingrediente.setVisibility(View.GONE);
        if( !chosen[5]) vcoccion.setVisibility(View.GONE);
        if( !chosen[6]) vorigen.setVisibility(View.GONE);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}
