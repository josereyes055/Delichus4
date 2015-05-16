package geekgames.delichus4.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private int listas = 2;
    private ListView listViewLeft;
    private ListView listViewRight;
    private FichaAdapter leftAdapter;
    private FichaAdapter rightAdapter;
    public JSONArray recetas;
    List<Ficha> lista1;
    List<Ficha> lista2;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getRecetas();
        leftAdapter = new FichaAdapter(getActivity());
        rightAdapter = new FichaAdapter(getActivity());


        LinearLayout contenedorListas = (LinearLayout) getView().findViewById(R.id.contenedorListas);
        listas = contenedorListas.getChildCount();

        listViewLeft = (ListView) getView().findViewById(R.id.all_list_view_left);


        listViewLeft.setAdapter(leftAdapter);


        listViewLeft.setOnTouchListener(touchListener);


        if( listas == 2){
            listViewRight = (ListView) getView().findViewById(R.id.all_list_view_right);
            listViewRight.setAdapter(rightAdapter);
            listViewRight.setOnTouchListener(touchListener);
        }
        //listViewLeft.setOnScrollListener(scrollListener);
        //listViewRight.setOnScrollListener(scrollListener);

        // A more complicated dynamic way
        String[] spinnerItems = getResources().getStringArray(R.array.filtros_recetas);
        // create your own spinner array adapter
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_custom,spinnerItems){
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextSize(22);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setHeight(69);
                return v;
            }
        };
        Spinner spinner = (Spinner) getView().findViewById(R.id.filtro_receta);
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
                setAllRecipeList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //fetch();
        setAllRecipeList(0);
    }

    // Passing the touch event to the opposite list
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        boolean dispatched = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (listas == 2 ) {
                if (v.equals(listViewLeft) && !dispatched) {
                    dispatched = true;
                    listViewRight.dispatchTouchEvent(event);
                } else if (v.equals(listViewRight) && !dispatched) {
                    dispatched = true;
                    listViewLeft.dispatchTouchEvent(event);
                } // similarly for listViewThree & listViewFour
                dispatched = false;

            }
            return false;
        }
    };

    public void getRecetas(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefArray = app_preferences.getString("recetas", null);
        Log.i("FUCKING DEBUG", prefArray);
        if( prefArray != null ){
            try {
                recetas = new JSONArray(prefArray);
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void setAllRecipeList(int orden){

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
                                if (i % 2 == 0) {
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
                    rightAdapter.swapRecipeRecords(lista2);
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



    private List<Recipe> parse(JSONObject json) throws JSONException {
        ArrayList<Recipe> records = new ArrayList<Recipe>();



        return records;
    }

    
}