package geekgames.delichus4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.SeekBar;

import org.apmem.tools.layouts.FlowLayout;

import geekgames.delichus4.R;
import geekgames.delichus4.customViews.CustomButton;
import geekgames.delichus4.customViews.CustomPager;
import geekgames.delichus4.fragments.busquedas.ConfigFiltros;
import geekgames.delichus4.fragments.busquedas.ResultFiltros;
import geekgames.delichus4.fragments.busquedas.SelectFiltros;

public class BusquedaFiltros extends ActionBarActivity{

    Animation animScale;
    Animation animScaleSutile;
    Animation animScaleRectangular;

    SectionsPagerAdapter mSectionsPagerAdapter;
    CustomPager mViewPager;

    public boolean[] chosen;
    public String[] values;
    int cantFiltros = 7;

    SelectFiltros select;
    ConfigFiltros config;
    ResultFiltros result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda_avanzada_filtros);

        select = new SelectFiltros();
        config = new ConfigFiltros();
        result = new ResultFiltros();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale_button_animation);
        animScaleSutile = AnimationUtils.loadAnimation(this, R.anim.scale_button_sutile_animation);
        animScaleRectangular = AnimationUtils.loadAnimation(this, R.anim.scale_button_rectangular_animation);
        // Set up the ViewPager with the sections adapter.

        mViewPager = (CustomPager) findViewById(R.id.pager_filtros);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        chosen = new boolean[cantFiltros];
        values = new String[cantFiltros];

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


    public void startConfig(View view){
        view.startAnimation(animScaleSutile);

            chosen[0] = ((CustomButton)findViewById(R.id.check_cantidad)).estado;
            chosen[1] = ((CustomButton)findViewById(R.id.check_tiempo)).estado;
            chosen[2] = ((CustomButton)findViewById(R.id.check_categoria)).estado;
            chosen[3] = ((CustomButton)findViewById(R.id.check_tipo)).estado;
            chosen[4] = ((CustomButton)findViewById(R.id.check_ingredientes)).estado;
            chosen[5] = ((CustomButton)findViewById(R.id.check_coccion)).estado;
            chosen[6] = ((CustomButton)findViewById(R.id.check_origen)).estado;

        config.setViews( chosen );

        mViewPager.setCurrentItem( mViewPager.getCurrentItem()+1);
    }

    public void setSearch(View view){
        view.startAnimation(animScaleSutile);

        SeekBar persos = (SeekBar)findViewById(R.id.seekPersonas);
        SeekBar time = (SeekBar)findViewById(R.id.seekTiempo);
        values[0] = "%22cantidad%22:"+persos.getProgress();
        values[1] = "%22tiempo%22:"+(time.getProgress()*60);
        values[2] = getParameters( (FlowLayout)findViewById(R.id.flowCategoria), "categorias" );
        values[3] = getParameters( (FlowLayout)findViewById(R.id.flowTipo), "tipo" );
        values[4] = getParameters( (FlowLayout)findViewById(R.id.flowIngredientes), "ingredientes" );
        values[5] = getParameters( (FlowLayout)findViewById(R.id.flowCoccion), "coccion" );
        values[6] = getParameters( (FlowLayout)findViewById(R.id.flowOrigen), "origen" );

        result.startSearch(values);
        mViewPager.setCurrentItem( mViewPager.getCurrentItem()+1);

    }

    String getParameters(FlowLayout fl, String label){
        String campos = "%22"+label+"%22:[";

        int contador = 0;
        for (int i = 0; i<fl.getChildCount(); i++){
            CustomButton cb = (CustomButton) fl.getChildAt(i);
            if( cb.estado ) {
                campos += cb.id + ",";
                contador ++;
            }
        }
        if( contador > 0) {
            campos = campos.substring(0, campos.length() - 1);
        }
        campos += "]";

        return campos;
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
                    return select;

                case 1:
                    return config;

                case 2:
                    return result;

                default:
                    return select;

            }//end case

        }//end getitem

        @Override
        public int getCount() {
            return 3;
        }
    }




}
