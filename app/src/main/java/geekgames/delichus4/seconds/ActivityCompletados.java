package geekgames.delichus4.seconds;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
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
import geekgames.delichus4.adapters.MiniFichaAdapter;
import geekgames.delichus4.customObjects.MiniFicha;

public class ActivityCompletados extends ActionBarActivity {

    private MiniFichaAdapter mAdapter;
    int idUser;
    private String currentHeader = "";
    JSONArray recetas;
    ListView listaFavs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritos);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new MiniFichaAdapter(this);
        String stringArray = MainApplication.getInstance().sp.getString("recetas",null);
        if( stringArray != null ){
            try {
                recetas = new JSONArray(stringArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        listaFavs = (ListView) findViewById(R.id.list_favoritos);
        listaFavs.setAdapter(mAdapter);
        setTitle("Completados");

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
        Toast.makeText(getApplicationContext(), getString(R.string.cargando_completadas)  , Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=13&userId="+idUser,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            currentHeader = "";
                            List<MiniFicha> favsRecords = parse(jsonObject);

                            AlphaAnimation animate_apear = new AlphaAnimation(0,1);
                            animate_apear.setDuration(400);


                            animate_apear.setFillAfter(true);
                            listaFavs.startAnimation(animate_apear);

                            mAdapter.swapRecords(favsRecords);
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

    private List<MiniFicha> parse(JSONObject json) throws JSONException {
        ArrayList<MiniFicha> records = new ArrayList<MiniFicha>();

        JSONArray favs = json.getJSONArray("completados");

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
                    valA = stringToDate((String) a.get("fechaComp"), "yyyy-MM-dd");
                    valB =  stringToDate((String) b.get("fechaComp"), "yyyy-MM-dd");

                } catch (Exception e) {
                    Log.e("ActivityCompletados.parse", "JSONException in combineJSONArrays sort section", e);
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

        for(int i =favs.length()-1; i >= 0; i--) {
            JSONObject jsonContent = favs.getJSONObject(i);

            String fecha = jsonContent.getString("fechaComp");

            // Se añade el encabezado
            if(fecha.equals(today) && !currentHeader.equals("COMPLETADOS DE HOY")) {
                records.add(new MiniFicha(-1, "COMPLETADOS DE HOY", "",  "", 0, "", 0));
                currentHeader = "COMPLETADOS DE HOY";
            }else if(fecha.equals(yesterday) && !currentHeader.equals("COMPLETADOS DE AYER")) {
                records.add(new MiniFicha(-1, "COMPLETADOS DE AYER", "",  "", 0,"",0));
                currentHeader = "COMPLETADOS DE AYER";
            }else if(!fecha.equals(yesterday) && !fecha.equals(today) && !currentHeader.equals("COMPLETADOS DE ANTES")){
                records.add(new MiniFicha(-1, "COMPLETADOS DE ANTES", "",  "", 0,"",0));
                currentHeader = "COMPLETADOS DE ANTES";
            }

            int idFav = jsonContent.getInt("compId");


            for(int r =0; r < recetas.length(); r++) {
                int id = recetas.getJSONObject(r).getInt("id");

                if(id == idFav) {
                    MiniFicha record = crearMiniFicha(r);
                    records.add(record);
                }
            }

        }

        return records;
    }

    public MiniFicha crearMiniFicha( int index ) throws JSONException {
        JSONObject ficha = recetas.getJSONObject(index);
        int id = ficha.getInt("id");
        String nombre = ficha.getString("receta");
        String imagen = ficha.getString("imagen");
        String autor = ficha.getString("autor");
        float puntuacion = Float.parseFloat( ficha.getString("puntuacion") );
        String descripcion = ficha.getString("descripcion");
        int pasos = ficha.getInt("pasos");

        MiniFicha unaMiniFicha = new MiniFicha(id, nombre, imagen, autor, puntuacion, descripcion,pasos);
        return unaMiniFicha;
    }


    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);

        return stringDate;
    }
}
