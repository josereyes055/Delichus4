package geekgames.delichus4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.SeekBar;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;

import geekgames.delichus4.customObjects.Ingrediente;
import geekgames.delichus4.customViews.CustomButton;
import geekgames.delichus4.customViews.CustomPager;
import geekgames.delichus4.fragments.busquedas.ConfigFiltros;
import geekgames.delichus4.fragments.busquedas.ConfigIngredientes;
import geekgames.delichus4.fragments.busquedas.ResultFiltros;
import geekgames.delichus4.fragments.busquedas.SelectFiltros;

public class BusquedaIngredientes extends ActionBarActivity{

    Animation animScale;
    Animation animScaleSutile;
    Animation animScaleRectangular;

    SectionsPagerAdapter mSectionsPagerAdapter;
    CustomPager mViewPager;

    ConfigIngredientes config;
    ResultFiltros result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda_avanzada_ingredientes);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale_button_animation);
        animScaleSutile = AnimationUtils.loadAnimation(this, R.anim.scale_button_sutile_animation);
        animScaleRectangular = AnimationUtils.loadAnimation(this, R.anim.scale_button_rectangular_animation);
        // Set up the ViewPager with the sections adapter.

        mViewPager = (CustomPager) findViewById(R.id.pager_ingredientes);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        config = new ConfigIngredientes();
        result = new ResultFiltros();

        //getActionBar().setDisplayHomeAsUpEnabled(true);


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
                //NavUtils.navigateUpFromSameTask(this);
                goBack();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public  void goBack(){

        if (mViewPager.getCurrentItem() > 0 ){
            mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);

        }
        else {
            finish();
            //onBackPressed();
        }
    }


    public void starSearchIngredientes(View view){
        view.startAnimation(animScaleSutile);
        String campos = "[";

        int contador = 0;
        for (int i = 0; i<config.unaLista.size(); i++){
            Ingrediente ing = (Ingrediente)config.unaLista.get(i);
            campos += "["+ing.id +","+ing.cantidad+ "],";
            contador ++;

        }
        if( contador > 0) {
            campos = campos.substring(0, campos.length() - 1);
        }
        campos += "]";

        result.startSearchIngrediente(campos);

        //Log.i("FUCKING DEBUG", campos) ;
        mViewPager.setCurrentItem( mViewPager.getCurrentItem()+1);
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setMessage(String key, String Value){

        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


            switch (position){
                case 0:
                    return config;
                case 1:
                    return result;


                default:
                    return new ConfigIngredientes();

            }//end case

        }//end getitem

        @Override
        public int getCount() {
            return 2;
        }
    }




}
