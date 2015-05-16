package geekgames.delichus4.seconds;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.SimpleRecipeAdapter;
import geekgames.delichus4.customObjects.Ficha;

public class ActivityFavoritos extends ActionBarActivity {

    private SimpleRecipeAdapter mAdapter;
    int idUser;
    private String currentHeader = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritos);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new SimpleRecipeAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list_favoritos);
        listView.setAdapter(mAdapter);
        setTitle("Favoritos");

        final SharedPreferences app_preferences =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        idUser = app_preferences.getInt("id",0);

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
                "http://www.geekgames.info/dbadmin/test.php?v=12&userId="+MainApplication.getInstance().usuario.id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            currentHeader = "";
                            List<Ficha> favsRecords = parse(jsonObject);

                            mAdapter.swapRecipeRecords(favsRecords);
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

    private List<Ficha> parse(JSONObject json) throws JSONException {
        ArrayList<Ficha> records = new ArrayList<Ficha>();

        JSONArray favs = json.getJSONArray("favoritos");

        // Se pasa la lista de recetas a un Array
        // para poder ordenarlo
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < favs.length(); i++)
            jsonValues.add(favs.getJSONObject(i));

        // Se ordenan por fecha
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                Date valA = new Date();
                Date valB = new Date();

                try {
                    valA = stringToDate((String) a.get("fechaFav"), "yyyy-MM-dd");
                    valB =  stringToDate((String) b.get("fechaFav"), "yyyy-MM-dd");

                } catch (Exception e) {
                    Log.e("ActivityFavoritos.parse", "JSONException in combineJSONArrays sort section", e);
                }

                int comp = valA.compareTo(valB);

                if (comp > 0)
                    return -1;
                if (comp < 0)
                    return 1;
                return 0;
            }
        });

        // Se obtiene la fecha actual en formato año-mes-dia
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpledateformat.format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        String yesterday = simpledateformat.format(cal.getTime());

        for(int i =0; i < favs.length(); i++) {
            JSONObject jsonContent = favs.getJSONObject(i);
            JSONObject jsonImage = jsonContent.getJSONObject("plato");
            int id = jsonImage.getInt("id");
            String receta = jsonImage.getString("receta");
            String imagen = jsonImage.getString("imagen");
            int idAutor = Integer.parseInt(jsonImage.getString("idAutor"));
            String autor = jsonImage.getString("autor");
            String foto = jsonImage.getString("foto");
            float puntuacion = Float.parseFloat(jsonImage.getString("puntuacion"));
            String descripcion = jsonImage.getString("descripcion");
            int pasos = jsonImage.getInt("pasos");
            String fecha = jsonContent.getString("fechaFav");

            // Se añade el encabezado
            if(fecha.equals(today) && !currentHeader.equals("FAVORITOS DE HOY")) {
                //records.add(new Ficha(-1, "FAVORITOS DE HOY", "", "", "", ""));
                currentHeader = "FAVORITOS DE HOY";
            }else if(fecha.equals(yesterday) && !currentHeader.equals("FAVORITOS DE AYER")) {
               // records.add(new Ficha(-1, "FAVORITOS DE AYER", "", "", "", ""));
                currentHeader = "FAVORITOS DE AYER";
            }else if(!fecha.equals(yesterday) && !fecha.equals(today) && !currentHeader.equals("FAVORITOS DE ANTES")){
              //  records.add(new Ficha(-1, "FAVORITOS DE ANTES", "", "", "", ""));
                currentHeader = "FAVORITOS DE ANTES";
            }

             Ficha record = new Ficha(id, receta, imagen, idAutor, autor, foto, puntuacion, descripcion, pasos);
             records.add(record);
        }

        return records;
    }

    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);

        return stringDate;
    }
}
