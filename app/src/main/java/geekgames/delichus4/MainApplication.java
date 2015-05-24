package geekgames.delichus4;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import geekgames.delichus4.customObjects.*;
import geekgames.delichus4.seconds.OtherUserPage;


public class MainApplication extends Application {

    public static MainApplication sInstance;
    public RequestQueue mRequestQueue;
    //public Usuario usuario;
    public static MediaPlayer mp;

    //public Ficha laReceta;
    public JSONArray losPasos;
    public SharedPreferences sp;


    public void exploreRecipe( Context ctx, int idReceta, String nombre, String descripcion, String imagen, int pasos ){
        Intent mainIntent = new Intent().setClass(
                ctx.getApplicationContext(), Receta.class);
        mainIntent.putExtra("id", String.valueOf(idReceta));
        mainIntent.putExtra("nombre", nombre);
        mainIntent.putExtra("descripcion", descripcion);
        mainIntent.putExtra("imagen", imagen);
        mainIntent.putExtra("pasos", String.valueOf(pasos));
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    public void visitAutor(Context ctx, int idAutor){
        Intent mainIntent = new Intent().setClass(
                ctx.getApplicationContext(), OtherUserPage.class);

        mainIntent.putExtra("id", String.valueOf(idAutor));
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mRequestQueue = Volley.newRequestQueue(this);
        mp = MediaPlayer.create(this, R.raw.favoritos);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sInstance = this;
    }

    public synchronized static MainApplication getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
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

    public void addComplete( int idUser, int idReceta){


        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=21&userId="+idUser+"&recipeId="+idReceta,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject userData = jsonObject;

                            String result = userData.getString("status");
                            //Toast.makeText(getApplicationContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show();

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

    public void uptadteScore( int puntaje){
        int score = sp.getInt("userPuntaje",0);
        int idUsuario = sp.getInt("userId",0);
        score += puntaje;
        double nivelPromedio = 1 + (Math.sqrt(score/10));
        int lvl = (int) Math.floor( nivelPromedio );
        SharedPreferences.Editor editor  = sp.edit();
        editor.putInt("userPuntaje",score);
        editor.putInt("userNivel", lvl);
        editor.commit();
        Log.i("FUCKING DEBUG", "se sumo el puntaje");
        String query = "http://www.geekgames.info/dbadmin/test.php?v=20&userId="+idUsuario+
                "&puntaje="+score+"&nivel="+lvl;
        Log.i("FUCKING DEBUG", query);

        JsonObjectRequest request = new JsonObjectRequest(
                query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("FUCKING DEBUG", "se guardo el puntaje");
                        //Toast.makeText(getApplicationContext(), "Calificación guardada", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(getApplicationContext(), "error al guardar la calificación", Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
    }

    public void calificar(String idReceta, double ratingNew){

        String query = "http://www.geekgames.info/dbadmin/test.php?v=18&id="+idReceta+"&nuevo="+ratingNew;
        Log.i("FUCKING DEBUG", query);

        JsonObjectRequest request = new JsonObjectRequest(
                query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        Toast.makeText(getApplicationContext(), "Calificación guardada", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "error al guardar la calificación", Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);



    }


}// end MainApplication

