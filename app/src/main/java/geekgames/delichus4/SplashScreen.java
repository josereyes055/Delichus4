package geekgames.delichus4;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity{

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;
    MainApplication app;
    SharedPreferences app_preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        app = MainApplication.getInstance();

        setContentView(R.layout.splash_screen);
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);


        verifyDb();

    }

    public void verifyDb(){
        Toast.makeText(getApplicationContext(), "Verificando Bases de Datos", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=1",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int versionDB = jsonObject.getInt("version");
                            int versionActual = app_preferences.getInt("versionDB", 0);

                            if( versionActual != versionDB){
                                fetchDatabase();
                            }else{
                                login();
                            }

                        }
                        catch(JSONException e) {
                          //  Toast.makeText(getApplicationContext(), "unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), getString(R.string.error_interno)  , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       // Toast.makeText(getApplicationContext(), "unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), getString(R.string.internet)  , Toast.LENGTH_SHORT).show();
                        Log.i("FUCKING DEBUG", "el disque error es: " + volleyError.getMessage());
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
    }


    public void fetchDatabase(){
        Toast.makeText(getApplicationContext(), "Cosechando ingredientes...", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=2",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject dataBase = jsonObject.getJSONObject("database");
                            SharedPreferences.Editor editor = app_preferences.edit();
                            editor.putString( "tipo_pasos", dataBase.getJSONObject("tipo_pasos").toString() );
                            editor.putString( "tipo_plato", dataBase.getJSONObject("tipo_plato").toString() );
                            editor.putString( "tipo_ingrediente", dataBase.getJSONObject("tipo_ingrediente").toString() );
                            editor.putString( "tipo_coccion", dataBase.getJSONObject("tipo_coccion").toString() );
                            editor.putString( "categoria", dataBase.getJSONObject("categoria").toString() );
                            editor.putString( "ingrediente", dataBase.getJSONObject("ingrediente").toString() );
                            editor.putString( "origen", dataBase.getJSONObject("origen").toString() );
                            editor.putString( "tags", dataBase.getJSONObject("tags").toString() );
                            editor.putString( "logros", dataBase.getJSONArray("logros").toString() );
                            editor.putInt( "versionDB", dataBase.getInt("version") );
                            editor.commit(); // Very important

                            login();

                        }
                        catch(JSONException e) {
                            //Toast.makeText(getApplicationContext(), "unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), getString(R.string.error_interno)  , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       // Toast.makeText(getApplicationContext(), "unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), getString(R.string.internet)  , Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);

    }

    public void login(){

        int user = app_preferences.getInt("userId", 0);

        if (user == 0) {
            Intent mainIntent = new Intent().setClass(
                    SplashScreen.this, Login.class);
            startActivity(mainIntent);
            finish();
        }
        else{

            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    Intent mainIntent = new Intent().setClass(
                            SplashScreen.this, MainActivity.class);
                    startActivity(mainIntent);
                    // Close the activity so the user won't able to go back this
                    // activity pressing Back button
                    finish();
                }
            };
            // Simulate a long loading process on application startup.
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        }

    }//end login

}