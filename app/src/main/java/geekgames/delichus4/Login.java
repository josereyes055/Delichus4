package geekgames.delichus4;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import geekgames.delichus4.customObjects.Usuario;
import geekgames.delichus4.seconds.RegistroUsuario;


public class Login extends Activity {

    CallbackManager callbackManager;
    JSONObject fbjson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login_screen);

        // Facebook Login
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebookButton);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.i("Success", loginResult.getRecentlyGrantedPermissions().toString());

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("Error", "Error: " + exception.toString());
            }
        });

        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

                if(currentProfile != null) {

                     fbjson = new JSONObject();
                    JSONObject user = new JSONObject();
                    try {
                        fbjson.put("loginResponse", "ok");
                        user.put("id", currentProfile.getId());
                        user.put("nombre", currentProfile.getFirstName()+currentProfile.getLastName());
                        user.put("foto", currentProfile.getProfilePictureUri(100, 100) );
                        user.put("titulo", "");
                        user.put("nivel", 0);
                        user.put("puntaje", 0);
                        user.put("promedio",0);
                        fbjson.put("user", user);
                        //Log.i("FUCKING DEBUG", "acceso fb: usuario = "+currentProfile.getName()+" id = "+currentProfile.getId()+" uri = "+currentProfile.getProfilePictureUri(80,80));

                        //login(json);
                        insertUser(currentProfile.getFirstName()+currentProfile.getLastName(), currentProfile.getId(),"12345", currentProfile.getProfilePictureUri(100,100).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

    public void insertUser( final String nombre, String correo, String pass, String imagen){
        String laRequest = "http://www.geekgames.info/dbadmin/test.php?v=10&nombre="+nombre+"&correo="+correo+"&pass="+pass+"&imagen="+imagen;
        Log.i("FUCKING DEBUG", "request: "+laRequest);
        JsonObjectRequest request = new JsonObjectRequest( laRequest,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                        JSONObject userData = jsonObject;
                          String result = userData.getString("status");
                          Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                            JSONObject user = fbjson.getJSONObject("user");
                          dbAccess(user.getString("nombre"),"12345");


                        /*Intent mainIntent = new Intent().setClass(
                                Login.this, MainActivity.class);
                        startActivity(mainIntent);*/



                       }
                       catch(JSONException e) {
                            Toast.makeText(getApplicationContext(), "No se pudo leer la info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "No pudo registrarse la información: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void access(View view){

        EditText txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        EditText pass = (EditText) findViewById(R.id.txtPass);

        String usuario = txtUsuario.getText().toString();
        String contraseña = pass.getText().toString();

        dbAccess(usuario, contraseña);

    }

    public void dbAccess( String usuario, String contraseña){
        String reqUrl = "http://www.geekgames.info/dbadmin/test.php?v=3&user="+usuario+"&pass="+contraseña;
        Toast.makeText(getApplicationContext(), "Accediendo...", Toast.LENGTH_SHORT).show();

        JsonObjectRequest request = new JsonObjectRequest(
                reqUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            login(jsonObject);

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

        MainApplication.getInstance().getRequestQueue().add(request);
    }

    public void login(JSONObject json) throws JSONException{

        JSONObject response = json;

        if( response.getString("loginResponse").equals("ok") ){

            Toast.makeText(getApplicationContext(), "Autenticado", Toast.LENGTH_SHORT).show();

            JSONObject user = response.getJSONObject("user");
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putInt("userId", user.getInt("id"));
            editor.putString("userNombre", user.getString("nombre"));
            editor.putString("userFoto", user.getString("foto"));
            editor.putString("userTitulo", user.getString("titulo"));
            editor.putInt("userNivel", user.getInt("nivel"));
            editor.putInt("userPuntaje", user.getInt("puntaje"));
            editor.putInt("userPromedio", user.getInt("promedio"));
            editor.putString("userLogros", user.getJSONArray("logros").toString());
            editor.commit(); // Very important

            Intent mainIntent = new Intent().setClass(
                    Login.this, MainActivity.class);
            startActivity(mainIntent);

            finish();

        }else{
            Toast.makeText(getApplicationContext(), response.getString("loginResponse"), Toast.LENGTH_SHORT).show();
        }
    }

    public void ir_registro (View view){

        Intent mainIntent = new Intent().setClass(
                Login.this, RegistroUsuario.class);
        startActivity(mainIntent);


    }
}
