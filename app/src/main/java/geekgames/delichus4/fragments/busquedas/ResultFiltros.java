package geekgames.delichus4.fragments.busquedas;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.FichaAdapter;
import geekgames.delichus4.customObjects.Recipe;
import geekgames.delichus4.fragments.Busqueda;

public class ResultFiltros extends Fragment {

    ResultFiltros instance;
    Busqueda parent;

    private ListView listViewLeft;
    private ListView listViewRight;
    private FichaAdapter leftAdapter;
    private FichaAdapter rightAdapter;
    List<Recipe> lista1;
    List<Recipe> lista2;

    String query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.busqueda_resulado, container, false);
        instance = this;
        parent = (Busqueda) getParentFragment();

        //JSONObject categorias = MainApplication.getInstance().categoria;
        //JSONObject tipo = MainApplication.getInstance().tipo_plato;
        //JSONObject ingredientes = MainApplication.getInstance().tipo_ingrediente;
        //JSONObject coccion = MainApplication.getInstance().tipo_coccion;

        //String[] lcategorias = setLista(categorias);
        //String[] ltipo = setLista(tipo);
        //String[] lingredientes = setLista(ingredientes);
        //String[] lcoccion = setLista(coccion);

        String[] vals = new String[6];
        if(parent.chosen[0]){ vals[0] = Integer.toString(parent.values[0]); }
        else{ vals[0] = "0"; }

        if(parent.chosen[1]){ vals[1] = Integer.toString(parent.values[1]*60); }
        else{ vals[1] = "0"; }

        //if(parent.chosen[2]){ vals[2] = lcategorias[ parent.values[2] ]; }
        //else{ vals[2] = "0"; }

        //if(parent.chosen[3]){ vals[3] = ltipo[ parent.values[3] ]; }
        //else{ vals[3] = "0"; }

        //if(parent.chosen[4]){ vals[4] = lingredientes[ parent.values[4] ]; }
        //else{ vals[4] = "0"; }

        //if(parent.chosen[5]){ vals[5] = lcoccion[ parent.values[5] ]; }
        //else{ vals[5] = "0"; }

        query = "http://www.geekgames.info/dbadmin/test.php?" +
                "v=15&h={" +
                "%22categorias%22:["+vals[2]+"]," +
                "%22ingredientes%22:["+vals[4]+"]," +
                "%22coccion%22:["+vals[5]+"]," +
                "%22tipo%22:["+vals[3]+"]," +
                "%22cantidad%22:"+vals[0]+"," +
                "%22tiempo%22:"+vals[1]+"," +
                "%22origen%22:[0]," +
                "%22autor%22:[0]}";

        Log.i("FUCKING DEBUG", query);

        fetch();

        return rootView;
    }

    public String[] setLista(JSONObject json){
        String[] lista = new String[json.length()];
        Iterator<String> keys = json.keys();
        int contador = 0;
        while (keys.hasNext()){
            lista[contador] = keys.next();
            contador ++;
        }
        return lista;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        leftAdapter = new FichaAdapter(getActivity());
        rightAdapter = new FichaAdapter(getActivity());

        listViewLeft = (ListView) getView().findViewById(R.id.search_list_view_left);
        listViewRight = (ListView) getView().findViewById(R.id.search_list_view_right);

        listViewLeft.setAdapter(leftAdapter);
        listViewRight.setAdapter(rightAdapter);

        listViewLeft.setOnTouchListener(touchListener);
        listViewRight.setOnTouchListener(touchListener);


    }

    // Passing the touch event to the opposite list
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        boolean dispatched = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.equals(listViewLeft) && !dispatched) {
                dispatched = true;
                listViewRight.dispatchTouchEvent(event);
            } else if (v.equals(listViewRight) && !dispatched) {
                dispatched = true;
                listViewLeft.dispatchTouchEvent(event);
            } // similarly for listViewThree & listViewFour
            dispatched = false;
            return false;
        }
    };


    private void fetch() {
        Toast.makeText(getActivity(), "Buscando...", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
               query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            lista1 = new ArrayList<Recipe>();
                            lista2 = new ArrayList<Recipe>();


                            JSONArray jsonImages = jsonObject.getJSONArray("recipes");
                            if(jsonImages.length()<1){
                                Toast.makeText(getActivity(), "No se encontraron recetas", Toast.LENGTH_SHORT).show();
                                parent.change(1);
                            }

                            for(int i =0; i < jsonImages.length(); i++) {
                                JSONObject jsonImage = jsonImages.getJSONObject(i);
                                int id = jsonImage.getInt("id");
                                String receta = jsonImage.getString("receta");
                                String larga = jsonImage.getString("larga");
                                String imagen = jsonImage.getString("imagen");
                                int idAutor = Integer.parseInt(jsonImage.getString("idAutor"));
                                String autor = jsonImage.getString("autor");
                                String foto = jsonImage.getString("foto");
                                String puntuacion = jsonImage.getString("puntuacion");
                                String descripcion = jsonImage.getString("descripcion");
                                int pasos = jsonImage.getInt("pasos");
                                int cantidad = jsonImage.getInt("personas");
                                int dificultad = jsonImage.getInt("dificultad");
                                int tiempo = jsonImage.getInt("tiempoTotal");
                                JSONArray steps = jsonImage.getJSONArray("steps");

                                Recipe record = new Recipe(id, receta, larga, imagen, idAutor, autor, foto, puntuacion, descripcion, pasos,cantidad,dificultad,tiempo, steps);
                                if(i%2 == 0) {
                                    lista1.add(record);
                                }else{
                                    lista2.add(record);
                                }
                            }

                            //leftAdapter.swapRecipeRecords(lista1);
                            //rightAdapter.swapRecipeRecords(lista2);
                        }
                        catch(JSONException e) {
                            Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
    }



}
