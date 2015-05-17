package geekgames.delichus4.seconds;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.LogroAdapter;
import geekgames.delichus4.customObjects.Logro;

public class ActivityLogros extends ActionBarActivity {

    private LogroAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logros);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new LogroAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list_logros);
        listView.setAdapter(mAdapter);
        setTitle("Logros");

        List<Logro> logrosRecords;

        try {
            logrosRecords = parse();
            mAdapter.swapRecipeRecords(logrosRecords);
        }catch (JSONException e){

        }


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


    private List<Logro> parse() throws JSONException {
        ArrayList<Logro> records = new ArrayList<Logro>();
        ArrayList<Logro> logrosAlcanzados = new ArrayList<Logro>();
        ArrayList<Logro> logrosPorRealizar = new ArrayList<Logro>();

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String prefArray = app_preferences.getString("logros", null);

        JSONArray logros ;
        JSONArray completed = null;
        String prefArray2 = app_preferences.getString("userLogros", null);
        try {
            completed = new JSONArray(prefArray2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                } else {
                    logrosPorRealizar.add(record);
                }
            }

        }// end prefArrray if

        // Si hay logros alcanzados se
        // añade el header
        if(logrosAlcanzados.size() > 0) {
            records.add(new Logro(-1, "LOGROS ALCANZADOS", "", "", false));
            records.addAll(logrosAlcanzados);
        }


        // Si hay logros por realizar se añade el header
        if(logrosPorRealizar.size() > 0) {
            records.add(new Logro(-1, "LOGROS POR REALIZAR", "", "", false));
            records.addAll(logrosPorRealizar);
        }

        return records;
    }

}
