package geekgames.delichus4;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
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
    //public Usuario usuario;
    public static MediaPlayer mp;

    public Ficha laReceta;
    public JSONArray losPasos;
    public SharedPreferences sp;

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
        score += puntaje;
        SharedPreferences.Editor editor  = sp.edit();
        editor.putInt("userPuntaje",score);
        editor.commit();
    }



}// end MainApplication

