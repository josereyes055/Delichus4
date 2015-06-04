package geekgames.delichus4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.etsy.android.grid.StaggeredGridView;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus4.adapters.FichaAdapter;
import geekgames.delichus4.customObjects.Ficha;
import geekgames.delichus4.customViews.CustomButton;
import geekgames.delichus4.customViews.CustomPager;
import geekgames.delichus4.fragments.busquedas.ConfigFiltros;
import geekgames.delichus4.fragments.busquedas.ResultFiltros;
import geekgames.delichus4.fragments.busquedas.SelectFiltros;

public class BusquedaNombre extends ActionBarActivity{

    Animation animScale;
    Animation animScaleSutile;
    Animation animScaleRectangular;

    private StaggeredGridView listViewLeft;
    private FichaAdapter leftAdapter;
    List<Ficha> lista1;
    JSONArray recetas;
    String query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda_resulado);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        animScale = AnimationUtils.loadAnimation(this, R.anim.scale_button_animation);
        animScaleSutile = AnimationUtils.loadAnimation(this, R.anim.scale_button_sutile_animation);
        animScaleRectangular = AnimationUtils.loadAnimation(this, R.anim.scale_button_rectangular_animation);
        // Set up the ViewPager with the sections adapter.

        leftAdapter = new FichaAdapter(getApplicationContext());

        listViewLeft = (StaggeredGridView) findViewById(R.id.grilla_resultados);

        listViewLeft.setAdapter(leftAdapter);


        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        Log.i("FUCKING DEBUG", "string: "+nombre);
        String[] separated = nombre.split("\\s+");
        query = "http://www.geekgames.info/dbadmin/test.php?v=17&h=[";
        int contador = 0;
        for (int i = 0; i< separated.length; i++){
            query += "%22"+separated[i]+"%22,";
            contador ++;
        }
        if( contador > 0) {
            query = query.substring(0, query.length() - 1);
        }
        query += "]";

        fetch();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                goBack();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public  void goBack(){


            finish();
            //onBackPressed();

    }


    private void fetch() {
        Toast.makeText(getApplicationContext(), "Buscando...", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
                query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            JSONArray jsonImages = jsonObject.getJSONArray("resultados");
                            if(jsonImages.length()<1){
                                Toast.makeText(getApplicationContext(), getString(R.string.no_recetas)  , Toast.LENGTH_SHORT).show();

                            }else {
                                setRecipeList(jsonImages);
                            }
                        }
                        catch(JSONException e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_interno)  , Toast.LENGTH_SHORT).show();
                            Log.e("PARSE JSON ERROR", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_recetas)  , Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), getString(R.string.error_interno)  , Toast.LENGTH_SHORT).show();
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
