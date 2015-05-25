package geekgames.delichus4.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.etsy.android.grid.StaggeredGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.FichaAdapter;
import geekgames.delichus4.customObjects.Ficha;
import geekgames.delichus4.customObjects.Recipe;


public class Todas extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.todas, container, false);

        return rootView;
    }

    private int listas = 1;
    private StaggeredGridView grid_op;
    private FichaAdapter leftAdapter;
    private FichaAdapter rightAdapter;
    JSONArray recetas;
    List<Ficha> lista1;
    List<Ficha> lista2;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        leftAdapter = new FichaAdapter(getActivity());
        rightAdapter = new FichaAdapter(getActivity());

        grid_op = (StaggeredGridView)getView().findViewById(R.id.grid_super_op);
        grid_op.setAdapter(leftAdapter);




        // A more complicated dynamic way
        String[] spinnerItems = getResources().getStringArray(R.array.filtros_recetas);
        // create your own spinner array adapter
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_custom,spinnerItems){
            public View getView(int position, View convertView, ViewGroup parent) {

                 View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.spinner));

                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setHeight(100);
                return v;
            }
        };
        final Spinner spinner = (Spinner) getView().findViewById(R.id.filtro_receta);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
            R.array.filtros_recetas, R.layout.spinner_custom);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner*/

        //spinner.setAdapter(adapter);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //((TextView) spinner.getChildAt(0)).setTextColor(getResources().getColor(R.color.naranja));
                setAllRecipeList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        // A more complicated dynamic way
        String[] spinnerItems2 = new String[]{"Mako", "Pikachu", "Zack", "Naruto", "Akira"};
        // create your own spinner array adapter
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_custom,spinnerItems2){
            public View getView(int position, View convertView, ViewGroup parent) {

                 View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.spinner));

                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setHeight(100);
                return v;
            }
        };
        final Spinner spinner2 = (Spinner) getView().findViewById(R.id.filtro_seguidos);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterGG = ArrayAdapter.createFromResource(getActivity(),
            R.array.filtros_seguidos, R.layout.spinner_custom);
        // Specify the layout to use when the list of choices appears
        adapterGG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner*/

        //spinner2.setAdapter(adapterGG);
        spinner2.setAdapter(adapter3);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //((TextView) spinner.getChildAt(0)).setTextColor(getResources().getColor(R.color.naranja));
                //setAllRecipeList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        fetchDatabase();

    }

    private void setAllRecipeList(int orden){
        //Toast.makeText(getActivity(), "Cargando recetas", Toast.LENGTH_LONG).show();
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(recetas != null){
            try {
                lista1 = new ArrayList<Ficha>();
                lista2 = new ArrayList<Ficha>();

                JSONArray lista;
                String prefArray = "";

                switch (orden){
                    case 1:
                        prefArray = app_preferences.getString("listaPuntuacion", null);
                    break;
                    case 2:
                        prefArray = app_preferences.getString("listaNovedad", null);
                    break;
                    case 3:
                        prefArray = app_preferences.getString("listaVegetariana", null);
                        break;
                    case 4:
                        prefArray = app_preferences.getString("listaVegana", null);
                        break;

                    default:
                        prefArray = app_preferences.getString("listaDefault", null);
                    break;
                }

                lista = new JSONArray(prefArray);

                for (int a = 0; a<lista.length(); a++){
                    int idPos = lista.getInt(a);

                    for(int i =0; i < recetas.length(); i++) {
                        int id = recetas.getJSONObject(i).getInt("id");

                        if(id == idPos) {
                            Ficha unaFicha = crearFicha(i);

                            if( listas == 2 ) {
                                if (a % 2 == 0) {
                                    lista1.add(unaFicha);
                                } else {
                                    lista2.add(unaFicha);
                                }
                            }else{
                                lista1.add(unaFicha);
                            }
                        }
                    }

                }


                leftAdapter.swapRecipeRecords(lista1);
                if (listas == 2) {
//                    rightAdapter.swapRecipeRecords(lista2);
                }
            }
            catch(JSONException e) {
                Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void fetchDatabase(){

        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=8",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject dataBase = jsonObject.getJSONObject("recipeDatabase");
                            SharedPreferences.Editor editor = MainApplication.getInstance().sp.edit();
                            editor.putString( "recetas", dataBase.getJSONArray("recetas").toString() );
                            editor.putString( "listaDefault", dataBase.getJSONArray("orden_default").toString() );
                            editor.putString( "listaPuntuacion", dataBase.getJSONArray("orden_puntuacion").toString() );
                            editor.putString( "listaNovedad", dataBase.getJSONArray("orden_novedad").toString() );
                            editor.putString( "listaVegetariana", dataBase.getJSONArray("orden_vegetariano").toString() );
                            editor.putString( "listaVegana", dataBase.getJSONArray("orden_vegano").toString() );
                            editor.commit(); // Very important

                            String stringArray = MainApplication.getInstance().sp.getString("recetas",null);
                            if( stringArray != null ){
                                try {
                                    recetas = new JSONArray(stringArray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            setAllRecipeList(0);

                        }
                        catch(JSONException e) {
                            Toast.makeText(getActivity(), "Error al cargar las recetas" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);

    }







}

