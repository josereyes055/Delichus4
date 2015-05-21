package geekgames.delichus4.fragments.busquedas;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.SelectorIngredienteAdapter;
import geekgames.delichus4.customObjects.Ingrediente;
import geekgames.delichus4.customViews.CustomButton;

public class ConfigIngredientes extends Fragment {

    ConfigIngredientes instance;

    View rootView;
    ArrayList<Integer> seleccionados;
    ArrayList<String> lista;
    ListView listaSelectores;
    SelectorIngredienteAdapter mAdapter;
    Button elboton;
    List<Ingrediente> unaLista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.config_ingredientes, container, false);
        listaSelectores = (ListView) rootView.findViewById(R.id.lista_selector_ingredientes);
        mAdapter = new SelectorIngredienteAdapter(getActivity());
        listaSelectores.setAdapter(mAdapter);
        elboton = (Button)rootView.findViewById(R.id.buttonConfigIngredientes);
        elboton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParameters();
            }
        });

        Intent intent = getActivity().getIntent();
        seleccionados = intent.getIntegerArrayListExtra("seleccionados");

        unaLista = setViews();
        mAdapter.swapRecords(unaLista);
        return rootView;


    }

     public void getParameters(){
        String campos = "%22ids%22:[";

        int contador = 0;
        for (int i = 0; i<unaLista.size(); i++){
            Ingrediente ing = (Ingrediente)unaLista.get(i);
            campos += "{"+ing.id +","+ing.cantidad+ "},";
            contador ++;

        }
        if( contador > 0) {
            campos = campos.substring(0, campos.length() - 1);
        }
        campos += "]";

        Log.i("FUCKING DEBUG", campos) ;
    }


    public List<Ingrediente> setViews( ){
        List<Ingrediente> laLista = new ArrayList<Ingrediente>();

        String stringArray = MainApplication.getInstance().sp.getString("ingrediente",null);
        List<String> indices = new ArrayList<String>();
        List<String> nombres = new ArrayList<String>();
        try {
            JSONObject lista = new JSONObject(stringArray);
            Iterator<String> iterator = lista.keys();
            while(iterator.hasNext()){
                try {
                    String key = iterator.next();
                    String text = lista.getString(key);
                    indices.add(key);
                    nombres.add(text);
                }catch (JSONException e){
                    Log.e("FUCKING DEBUG", e.toString());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i<seleccionados.size(); i++){
            int indice = seleccionados.get(i);
            int id = Integer.parseInt(indices.get(indice));
            String nombre = nombres.get(indice);
            Ingrediente ing = new Ingrediente(id,nombre,0.0,"");
            laLista.add(ing);

        }


        return laLista;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}
