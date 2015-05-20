package geekgames.delichus4.seconds;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import geekgames.delichus4.Login;
import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;


public class RegistroUsuario extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void registro_completo (View view) {

        EditText nombre_usuario = (EditText) findViewById(R.id.nombre_de_usuario);
        EditText mail_usuario = (EditText) findViewById(R.id.mail_de_usuario);
        EditText contrasena = (EditText) findViewById(R.id.contrasena1_de_usuario);
        EditText contrasena_valid = (EditText) findViewById(R.id.contrasena2_de_usuario);

        if (!contrasena.getText().toString().equals( contrasena_valid.getText().toString())) {

            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden ", Toast.LENGTH_SHORT).show();
            return;
        }


        insertUser(nombre_usuario.getText().toString(), mail_usuario.getText().toString(), contrasena.getText().toString());
    }

    public void insertUser( String nombre, String correo, String pass){
        String query = "http://www.geekgames.info/dbadmin/test.php?v=10&nombre="+nombre+"&correo="+correo+"&pass="+pass+"&imagen=";
        Log.i("FUCKING DEBUG", "consulta: "+query);
        JsonObjectRequest request = new JsonObjectRequest(
                query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
//                        try {

                            JSONObject userData = jsonObject;
//                            String result = userData.getString("status");
                            Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();

                        Intent mainIntent = new Intent().setClass(
                                RegistroUsuario.this, Login.class);
                        startActivity(mainIntent);



//                        }
//                        catch(JSONException e) {
//
//                            Toast.makeText(getApplicationContext(), "No se pudo leer la info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
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

    }

