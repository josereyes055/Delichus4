package geekgames.delichus4.fragments.busquedas;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.etsy.android.grid.StaggeredGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.FichaAdapter;
import geekgames.delichus4.customObjects.Ficha;
import geekgames.delichus4.customObjects.Recipe;

public class ResultFiltros extends Fragment {

    ResultFiltros instance;

    private StaggeredGridView listViewLeft;
    private FichaAdapter leftAdapter;
    List<Ficha> lista1;
    JSONArray recetas;

    String query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.busqueda_resulado, container, false);
        instance = this;

        return rootView;
    }

    public void startSearch(String[] vals){

        query = "http://www.geekgames.info/dbadmin/test.php?" +
                "v=15&h={" +
                vals[2]+","+
                vals[4]+","+
                vals[5]+","+
                vals[3]+","+
                vals[0]+","+
                vals[1]+","+
                vals[6]+","+
                "%22autor%22:[0]}";

        Log.i("FUCKING DEBUG", query);

        fetch();

    }

    public void startSearchIngrediente(String vals){
        query = "http://www.geekgames.info/dbadmin/test.php?v=16&h="+vals;
        Log.i("FUCKING DEBUG", query);

        fetch();
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

        listViewLeft = (StaggeredGridView) getView().findViewById(R.id.grilla_resultados);

        listViewLeft.setAdapter(leftAdapter);



    }


    private void fetch() {
        Toast.makeText(getActivity(), "Buscando...", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
               query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            JSONArray jsonImages = jsonObject.getJSONArray("resultados");
                            if(jsonImages.length()<1){
                                Toast.makeText(getActivity(), "No se encontraron recetas", Toast.LENGTH_SHORT).show();

                            }else {
                                setRecipeList(jsonImages);
                            }
                        }
                        catch(JSONException e) {
                            Toast.makeText(getActivity(), "Error al cargar la lista de resultados", Toast.LENGTH_SHORT).show();
                            Log.e("PARSE JSON ERROR",  e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Error al ejecutar la busqueda", Toast.LENGTH_SHORT).show();
                        Log.e("FETCH JSON ERROR",  volleyError.getMessage());
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
    }

    private void setRecipeList(JSONArray lista) throws JSONException {

        String stringArray = MainApplication.getInstance().sp.getString("recetas", null);
        try {
            recetas = new JSONArray(stringArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(recetas != null){
            try {
                lista1 = new ArrayList<Ficha>();

                for (int a = 0; a<lista.length(); a++){
                    JSONObject ficha = lista.getJSONObject(a);
                    int idPos = ficha.getInt("id");
                    int restantes = ficha.getInt("restantes");

                    for(int i =0; i < recetas.length(); i++) {
                        int id = recetas.getJSONObject(i).getInt("id");

                        if(id == idPos) {
                            Ficha unaFicha = crearFicha(i);
                            unaFicha.color = restantes;

                            lista1.add(unaFicha);

                        }
                    }

                }


                leftAdapter.swapRecipeRecords(lista1);

            }
            catch(JSONException e) {
               // Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Error al crear la lista de recetas", Toast.LENGTH_SHORT).show();
            }
        }

    }// end setAllRecipeList

    Ficha crearFicha( int index ) throws JSONException {
        JSONObject ficha = recetas.getJSONObject(index);
        int id = ficha.getInt("id");
        String nombre = ficha.getString("receta");
        String imagen = ficha.getString("imagen");
        int idAutor = Integer.parseInt(ficha.getString("idAutor"));
        String autor = ficha.getString("autor");
        String foto = ficha.getString("foto");
        float puntuacion = Float.parseFloat( ficha.getString("puntuacion") );
        String descripcion = ficha.getString("descripcion");
        int pasos = ficha.getInt("pasos");

        Ficha unaFicha = new Ficha(id, nombre, imagen, idAutor, autor, foto, puntuacion, descripcion, pasos);
        return unaFicha;
    }



}
