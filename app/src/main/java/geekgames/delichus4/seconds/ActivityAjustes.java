package geekgames.delichus4.seconds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import geekgames.delichus4.Login;
import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.SimpleRecipeAdapter;
import geekgames.delichus4.customObjects.Recipe;

public class ActivityAjustes extends ActionBarActivity {

    private SimpleRecipeAdapter mAdapter;
    int idUser;
    private String currentHeader = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes_perfil);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        /*mAdapter = new SimpleRecipeAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list_favoritos);
        listView.setAdapter(mAdapter);
        setTitle("Favoritos");

        final SharedPreferences app_preferences =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        idUser = app_preferences.getInt("id",0);

        fetch();*/


    }

    public void logOut(View view){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.clear();
        Intent mainIntent = new Intent().setClass(
                ActivityAjustes.this, Login.class);
        startActivity(mainIntent);
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

    public void openSeguidos(View view){
        Intent mainIntent = new Intent().setClass(
                ActivityAjustes.this, ActivitySeguidos.class);
        startActivity(mainIntent);
    }




}
