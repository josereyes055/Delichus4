package geekgames.delichus4;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus4.customObjects.*;


public class MainApplication extends Application {

    public static MainApplication sInstance;
    public RequestQueue mRequestQueue;
    public Usuario usuario;
    public static MediaPlayer mp;

    public List<Recipe> recomendados;
    public List<Ficha> favoritos;
    public List<Ficha> completados;
    public List<Ingrediente> shoppingList;

    public Ficha laReceta;
    public JSONArray losPasos;
    /*public JSONObject tipo_pasos;
    public JSONObject tipo_plato;
    public JSONObject tipo_ingrediente;
    public JSONObject tipo_coccion;
    public JSONObject categoria;
    public JSONObject ingrediente;
    public JSONObject origen;
    public JSONObject tags;
    public JSONArray logros;
    public JSONArray recetas;*/

    public String currentHeader = "";

    @Override
    public void onCreate() {
        super.onCreate();

        mRequestQueue = Volley.newRequestQueue(this);
        mp = MediaPlayer.create(this,R.raw.favoritos);

        sInstance = this;
    }

    public synchronized static MainApplication getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public void fetchUserData( int idUser){

        Toast.makeText(getApplicationContext(), "Visitendo al chef...", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=4&userId="+idUser,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject userData = jsonObject;

                            int id = userData.getInt("id");
                            String foto = userData.getString("foto");
                            String nombre = userData.getString("nombre");
                            String titulo = userData.getString("titulo");
                            int score = userData.getInt("puntaje");
                            int lvl = userData.getInt("nivel");

                            Log.i("FUCKING DEBUG", "usuario "+String.valueOf(id)+" creado : "+nombre);

                            usuario = new Usuario(id, foto, nombre, titulo, score, lvl);
                            Toast.makeText(getApplicationContext(), "Desenpolvando libros de recetas...", Toast.LENGTH_SHORT).show();
                            fetchUserAchievements( id );
                            //fetchCompletedList( id );
                        }
                        catch(JSONException e) {

                            Toast.makeText(getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        mRequestQueue.add(request);

    }

    public void fetchCompletedList( int idUser ){

        //Toast.makeText(getApplicationContext(), "Lavando los platos sucios...", Toast.LENGTH_SHORT).show();
        final JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=5&userId="+idUser,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            JSONArray favs = jsonObject.getJSONArray("completados");
                            ArrayList<Ficha> records = new ArrayList<Ficha>();

                            for(int i =0; i < favs.length(); i++) {

                                JSONObject favElement = favs.getJSONObject(i);
                                JSONObject plato = favElement.getJSONObject("plato");

                                final int idPlato = plato.getInt("id");
                                final String nombre = plato.getString("receta");
                                final int tipo = plato.getInt("tipo");
                                final String autor = plato.getString("autor");
                                final int puntuacion = plato.getInt("puntuacion");
                                String fecha = favElement.getString("fechaComp");

                                //SimpleRecipe record = new SimpleRecipe(idPlato, nombre, tipo, autor, fecha, puntuacion );
                                Log.i("FUCKING DEBUG", "adherido a la lista completados : "+nombre);
                                //records.add(record);
                            }
                            completados = records;

                        }
                        catch(JSONException e) {
                            Log.i("FUCKING DEBUG", "Unable to parse data: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        mRequestQueue.add(request);

    }

    public void fetchUserAchievements( int idUser ){

        //Toast.makeText(getApplicationContext(), "Puliendo trofeos de cocina...", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=13&userId="+idUser,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject userData = jsonObject;

                            JSONArray logros = userData.getJSONArray("logros_usuario");

                            Log.i("FUCKING DEBUG", "logros cargados : "+usuario.nombre);

                            usuario.logros = logros;
                        }
                        catch(JSONException e) {

                            Toast.makeText(getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        mRequestQueue.add(request);
    }

    public void addFav( int idUser, int idReceta){


        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=11&userId="+idUser+"&recipeId="+idReceta,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject userData = jsonObject;

                            String result = userData.getString("status");


                                Toast.makeText(getApplicationContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show();




                        }
                        catch(JSONException e) {

                            Toast.makeText(getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        mRequestQueue.add(request);

    }

    public void addFollow( int idUser, int idSeguido){
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=17&userId="+idUser+"&followId="+idSeguido,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject userData = jsonObject;

                            String result = userData.getString("status");
                            Toast.makeText(getApplicationContext(), "Añadido a seguidos", Toast.LENGTH_SHORT).show();

                        }
                        catch(JSONException e) {

                            Toast.makeText(getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        mRequestQueue.add(request);
    }




}// end MainApplication

