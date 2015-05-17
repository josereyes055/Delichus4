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

import geekgames.delichus4.R;
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
    public int[] values;
    int cantFiltros = 6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda_avanzada_filtros);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale_button_animation);
        animScaleSutile = AnimationUtils.loadAnimation(this, R.anim.scale_button_sutile_animation);
        animScaleRectangular = AnimationUtils.loadAnimation(this, R.anim.scale_button_rectangular_animation);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomPager) findViewById(R.id.pager_filtros);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        chosen = new boolean[cantFiltros];
        values = new int[cantFiltros];

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
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public  void goBack(View view){
        view.startAnimation(animScaleSutile);
        if (mViewPager.getCurrentItem() > 0 ){
            mViewPager.setCurrentItem(0);

        }
        else {
            onBackPressed();
        }
    }

    public void busquedaPrev(View view){
        view.startAnimation(animScaleSutile);
        int state = mViewPager.getCurrentItem();
        if( state > 2 ){
            state --;
        }
        mViewPager.setCurrentItem(state);
    }

    public void busquedaNext(View view){
        view.startAnimation(animScaleSutile);
        int state = mViewPager.getCurrentItem();

            View root = view.getRootView();
            chosen[0] = ((CheckBox) root.findViewById(R.id.check_cantidad)).isChecked();
            chosen[1] = ((CheckBox) root.findViewById(R.id.check_tiempo)).isChecked();
            chosen[2] = true;
            chosen[3] = ((CheckBox) root.findViewById(R.id.check_tipo)).isChecked();
            chosen[4] = ((CheckBox) root.findViewById(R.id.check_ingredientes)).isChecked();
            chosen[5] = ((CheckBox) root.findViewById(R.id.check_coccion)).isChecked();


        if( state < mSectionsPagerAdapter.getCount() ){
            state ++;
        }

        mViewPager.setCurrentItem(state);
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
                    return new SelectFiltros();

                case 1:
                    Bundle bundle=new Bundle();
                    bundle.putBooleanArray("chosen",chosen);
                    bundle.putString("prueba", "no me digas lupita");
                    //set Fragmentclass Arguments
                    ConfigFiltros fragment = new ConfigFiltros();
                    fragment.setArguments(bundle);
                    return fragment;

                case 2:
                    return new ResultFiltros();

                default:
                    return new SelectFiltros();

            }//end case

        }//end getitem

        @Override
        public int getCount() {
            return 3;
        }
    }




}
