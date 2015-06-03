package geekgames.delichus4.seconds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import geekgames.delichus4.Login;
import geekgames.delichus4.MainActivity;
import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.customViews.CustomPager;
import geekgames.delichus4.seconds.forms.Form1;
import geekgames.delichus4.seconds.forms.Form2;
import geekgames.delichus4.seconds.forms.Form3;
import geekgames.delichus4.seconds.forms.Form4;
import geekgames.delichus4.seconds.forms.Form5;
import geekgames.delichus4.seconds.forms.Form6;


public class FormularioInicio extends ActionBarActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    CustomPager mViewPager;
    int pos = 0;
    String query = "http://www.geekgames.info/dbadmin/test.php?v=23&userId=0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_inicial);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomPager) findViewById(R.id.pagerForm);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void formNext(View view){
        if (pos == 0){
            query += "&dieta=[";
            String campos = "";
            String[] valores = new String[]{"", "1", "2", "5"};
            LinearLayout dieta = (LinearLayout)findViewById(R.id.dieta_checkbox);
            for (int i = 0; i< dieta.getChildCount(); i++){
                CheckBox uncheck = (CheckBox)dieta.getChildAt(i);
                if( uncheck.isChecked() ){
                    campos += valores[i]+",";
                }
            }
            if(campos.length()>0){
                campos = campos.substring(0, campos.length() - 1);
            }
            Log.i("FUCKINGDEBUG","seleccionados; "+campos);
            query += campos;
            query += "]";
        }
        if(pos == 1){
            query += "&gustos=[";
            LinearLayout gustos = (LinearLayout)findViewById(R.id.pref_alimento_checkbox);
            query += getSelected(gustos);
            query += "]";
        }
        if(pos == 2){
            query += "&odios=[";
            LinearLayout odios = (LinearLayout)findViewById(R.id.odia_checkbox);
            query += getSelected(odios);
            query += "]";
        }
        if(pos == 3){
            query += "&alergia=[";
            LinearLayout alergia = (LinearLayout)findViewById(R.id.alergia_checkbox);
            query += getSelected(alergia);
            query += "]";
        }
        if(pos == 4){
            query += "&digestion=[";
            LinearLayout digestion = (LinearLayout)findViewById(R.id.digerir_checkbox);
            query += getSelected(digestion);
            query += "]";
        }
        if(pos == 5){
            query += "&condicion=[";
            String campos = "";
            String[] valores = new String[]{"", "7", "9", "8","10","11"};
            LinearLayout condicion = (LinearLayout)findViewById(R.id.condicion_checkbox);
            for (int i = 0; i< condicion.getChildCount(); i++){
                CheckBox uncheck = (CheckBox)condicion.getChildAt(i);
                if( uncheck.isChecked() ){
                    campos += valores[i]+",";
                }
            }
            if(campos.length()>0){
                campos = campos.substring(0, campos.length() - 1);
            }
            Log.i("FUCKINGDEBUG","seleccionados; "+campos);
            query += campos;
            query +="]";

            Log.i("FUCKING DEBUG", "query recomendados: "+query);
            SharedPreferences.Editor editor = MainApplication.getInstance().sp.edit();
            editor.putString("query",query);
            editor.commit();

            Intent mainIntent = new Intent().setClass(
                    FormularioInicio.this, Tutorial.class);
            startActivity(mainIntent);
            finish();
        }

        pos ++;
        if(pos < 6) {
            mViewPager.setCurrentItem(pos);
        }
    }

    String getSelected(LinearLayout parent){
        String campos = "";
        String[] valores = new String[]{"", "2", "1", "3", "6", "7","8","9","10","11","13","14","15","16","18","19","20"};

        for (int i = 0; i< parent.getChildCount(); i++){
            CheckBox uncheck = (CheckBox)parent.getChildAt(i);
            if( uncheck.isChecked() ){
                campos += valores[i]+",";
            }
        }
        if(campos.length()>0){
            campos = campos.substring(0, campos.length() - 1);
        }
        Log.i("FUCKINGDEBUG","seleccionados; "+campos);
        return campos;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new Form1();
                case 1:
                    return new Form2();
                case 2:
                    return new Form3();
                case 3:
                    return new Form4();
                case 4:
                    return new Form5();
                case 5:
                    return new Form6();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 6;
        }

    }


}

