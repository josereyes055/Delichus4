package geekgames.delichus4.seconds;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;

public class OtherUserPage extends ActionBarActivity {

    String idUser;
    private String currentHeader = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otro_usuario);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        idUser = intent.getStringExtra("id");
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
                onBackPressed();
                return true;


            /*case R.id.action_settings:
                return true;*/

        }

        return super.onOptionsItemSelected(item);
    }

    private void fetch() {

        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=4&userId="+idUser,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject userData = jsonObject;

                            ImageView foto = (ImageView)findViewById(R.id.other_user_foto);
                            Picasso.with(getApplicationContext()).load(userData.getString("foto")).placeholder(R.drawable.cargando_perfil).fit().centerCrop().into(foto);
                            TextView nombre = (TextView)findViewById(R.id.su_name);
                            TextView nombre2 = (TextView)findViewById(R.id.su_name_reputacion);
                            TextView titulo = (TextView)findViewById(R.id.otro_perfil_titulo);
                            TextView nivel = (TextView) findViewById(R.id.otro_nivel);
                            TextView reputa = (TextView)findViewById(R.id.otro_reputacion);
                            ImageView favBtn = (ImageView)findViewById(R.id.seguir);
                            favBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("FUCKING DEBUG", "se va a adherir " + MainApplication.getInstance().laReceta.autor +" como seguido" );
                                    MainApplication.getInstance().addFollow(MainApplication.getInstance().sp.getInt("userId",0), MainApplication.getInstance().laReceta.idAutor );
                                }
                            });
                            RatingBar rating = (RatingBar)findViewById(R.id.otro_rating);
                            nombre.setText(userData.getString("nombre") );
                            nombre2.setText( userData.getString("nombre") );
                            titulo.setText(userData.getString("titulo"));
                            nivel.setText(userData.getString("nivel"));
                            reputa.setText(userData.getInt("promedio")+".0");
                            rating.setRating(userData.getInt("promedio"));

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
        //MainApplication.getInstance().fetchUserAchievements(  MainApplication.getInstance().getUserId() );
    }

    private void parse(JSONObject json) throws JSONException {

        JSONArray favs = json.getJSONArray("favoritos");

        // Se pasa la lista de recetas a un Array
        // para poder ordenarlo
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();

        // Se ordenan por fecha
    }


}
