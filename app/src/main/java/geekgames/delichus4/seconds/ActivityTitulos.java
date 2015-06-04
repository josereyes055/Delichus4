package geekgames.delichus4.seconds;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.LogroAdapter;
import geekgames.delichus4.adapters.TituloAdapter;
import geekgames.delichus4.customObjects.Logro;

public class ActivityTitulos extends ActionBarActivity {

    private TituloAdapter mAdapter;
    int idUser;
    ListView listaLogros;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titulos);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new TituloAdapter(this);

        listaLogros = (ListView) findViewById(R.id.list_titulos);
        listaLogros.setAdapter(mAdapter);
        setTitle("Titulos");

        idUser = MainApplication.getInstance().sp.getInt("userId",0);

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
        Toast.makeText(getApplicationContext(), getString(R.string.cargando_seguidos), Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=28&userId="+idUser,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            JSONArray completed = jsonObject.getJSONArray("logros_usuario");
                            if(completed.length() > 0) {}else{
                                Toast.makeText(getApplicationContext(), "Aún no tienes logros que presumir :/"  , Toast.LENGTH_LONG).show();

                            }
                                List<Logro> logrosRecords = parse(completed);

                                AlphaAnimation animate_apear = new AlphaAnimation(0,1);
                                animate_apear.setDuration(400);


                                animate_apear.setFillAfter(true);
                                listaLogros.startAnimation(animate_apear);

                                mAdapter.swapRecipeRecords(logrosRecords);



                        }
                        catch(JSONException e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_seguidos)  , Toast.LENGTH_SHORT).show();
                            Log.e("PARSE JSON ERROR", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), getString(R.string.internet)  , Toast.LENGTH_SHORT).show();
                        Log.e("FETCH JSON ERROR", volleyError.getMessage());
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
        //MainApplication.getInstance().fetchUserAchievements(  MainApplication.getInstance().getUserId() );
    }


    private List<Logro> parse(JSONArray jsonObject) throws JSONException {

        ArrayList<Logro> logrosAlcanzados = new ArrayList<Logro>();

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String prefArray = app_preferences.getString("logros", null);

        JSONArray logros ;
        JSONArray completed = jsonObject;

        if(prefArray != null) {
            logros = new JSONArray(prefArray);


            if (completed == null) {
                completed = new JSONArray();
            }

            for (int i = 0; i < logros.length(); i++) {
                JSONObject logro = logros.getJSONObject(i);
                int id = Integer.parseInt(logro.getString("id"));
                String nombre = logro.getString("logro");
                String titulo = logro.getString("titulo");
                String descripcion = logro.getString("descripcion");
                boolean done = false;
                for (int j = 0; j < completed.length(); j++) {
                    if (id == completed.getInt(j)) {
                        done = true;
                        break;
                    }
                }
                Logro record = new Logro(id, nombre, titulo, descripcion, done);

                if (done) {
                    logrosAlcanzados.add(record);
                }
            }

        }// end prefArrray if


        return logrosAlcanzados;
    }

}
