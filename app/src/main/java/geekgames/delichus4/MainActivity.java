package geekgames.delichus4;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import geekgames.delichus4.Dialogs.ListDialog;
import geekgames.delichus4.fragments.Busqueda;
import geekgames.delichus4.fragments.Crear;
import geekgames.delichus4.fragments.Perfil;
import geekgames.delichus4.fragments.Recomendados;
import geekgames.delichus4.fragments.Todas;
import geekgames.delichus4.seconds.ActivityAjustes;
import geekgames.delichus4.seconds.ActivityFavoritos;
import geekgames.delichus4.seconds.ActivityLogros;
import geekgames.delichus4.seconds.ActivitySeguidos;
import geekgames.delichus4.seconds.ActivityShoppingList;
import geekgames.delichus4.seconds.OtherUserPage;


public class MainActivity extends ActionBarActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;
    int tags[] = {0,1,2,3,4};

    Crear crear;
    Todas todas;
    Recomendados recomendados;
    Perfil perfil;
    Busqueda busqueda;
    Animation animScale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale_button_animation);
        crear = new Crear();
        todas = new Todas();
        recomendados =new Recomendados();
        perfil = new Perfil();
        busqueda =new Busqueda();


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(2);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                changeButtons(null, position);
            }
        });

    }

    public void changeButtons(View view, int tag){

        View root;
        int normalSize = 12;
        int selectedSize = 15;
        if (view != null) {
            root = view.getRootView();
        }else {
            root = getWindow().getDecorView().findViewById(android.R.id.content);
        }

        ImageView btn1 = (ImageView) root.findViewById(R.id.delimenu1);
        ImageView btn2 = (ImageView) root.findViewById(R.id.delimenu2);
        ImageView btn3 = (ImageView) root.findViewById(R.id.delimenu3);
        ImageView btn4 = (ImageView) root.findViewById(R.id.delimenu4);
        ImageView btn5 = (ImageView) root.findViewById(R.id.delimenu5);

        btn1.setImageResource(R.drawable.crearreceta);
        btn2.setImageResource(R.drawable.todas_las_recetas);
        btn3.setImageResource(R.drawable.recomendado);
        btn4.setImageResource(R.drawable.perfil);
        btn5.setImageResource(R.drawable.busqueda);

        btn1.setPadding(10,10,10,10);
        btn2.setPadding(10,10,10,10);
        btn3.setPadding(10,10,10,10);
        btn4.setPadding(10,10,10,10);
        btn5.setPadding(10,10,10,10);

        TextView textView1 = (TextView)root.findViewById(R.id.delimenu1_text);
        TextView textView2 = (TextView)root.findViewById(R.id.delimenu2_text);
        TextView textView3 = (TextView)root.findViewById(R.id.delimenu3_text);
        TextView textView4 = (TextView)root.findViewById(R.id.delimenu4_text);
        TextView textView5 = (TextView)root.findViewById(R.id.delimenu5_text);

        textView1.setTextSize(normalSize);
        textView2.setTextSize(normalSize);
        textView3.setTextSize(normalSize);
        textView4.setTextSize(normalSize);
        textView5.setTextSize(normalSize);
        switch (tag){
            case 0:
                btn1.startAnimation(animScale);
                btn1.setImageResource(R.drawable.crearreceta_selec);
                btn1.setPadding(0,0,0,0);

                textView1.setTextSize(selectedSize);
                break;
            case 1:
                btn2.startAnimation(animScale);
                btn2.setImageResource(R.drawable.todas_las_recetas_selec);
                btn2.setPadding(0,0,0,0);

                textView2.setTextSize(selectedSize);
                break;
            case 2:
                btn3.startAnimation(animScale);
                btn3.setImageResource(R.drawable.recomendado_selec);
                btn3.setPadding(0,0,0,0);

                textView3.setTextSize(selectedSize);
                break;
            case 3:
                btn4.startAnimation(animScale);
                btn4.setImageResource(R.drawable.perfil_selec);
                btn4.setPadding(0,0,0,0);

                textView4.setTextSize(selectedSize);
                break;
            case 4:
                btn5.startAnimation(animScale);
                btn5.setImageResource(R.drawable.busqueda_selec);
                btn5.setPadding(0,0,0,0);

                textView5.setTextSize(selectedSize);
                break;
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
        return super.onOptionsItemSelected(item);
    }

    public void openLogros(View view){
        view.startAnimation(animScale);
        Intent mainIntent = new Intent().setClass(
               MainActivity.this, ActivityLogros.class);
        startActivity(mainIntent);

    }

    public void showDialog(View view){
        view.startAnimation(animScale);
        ListDialog list = new ListDialog();
        list.show(getFragmentManager(),"unTag");
    }

    public void gotoPage(View view){
        view.startAnimation(animScale);
        int tag = Integer.parseInt((String) view.getTag());
        mViewPager.setCurrentItem(tag, true);
        //changeButtons(view, tag);

    }

    public void exploreRecipe(View view){
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, Receta.class);
        startActivity(mainIntent);

    }

    public void viewOtherUser(View elView){
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, OtherUserPage.class);
        startActivity(mainIntent);
    }


    public void openFavoritos(View view){
        view.startAnimation(animScale);
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, ActivityFavoritos.class);
        startActivity(mainIntent);
    }

    public void openShoppingList(View view){
        view.startAnimation(animScale);
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, ActivityShoppingList.class);
        startActivity(mainIntent);
    }

    public void openAjustes(View view){
        view.startAnimation(animScale);
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, ActivityAjustes.class);
        startActivity(mainIntent);
    }

    public void openSeguidos(View view){
        view.startAnimation(animScale);
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, ActivitySeguidos.class);
        startActivity(mainIntent);
    }

    @Override
    public void onBackPressed() {
        //Log.i("FUCKING DEBUG", "Back pressed...");
        // If the fragment exists and has some back-stack entry
        if (busqueda != null && busqueda.getChildFragmentManager().getBackStackEntryCount() > 0){
            // Get the fragment fragment manager - and pop the backstack
            if(busqueda.getChildFragmentManager().getBackStackEntryCount()>2){
                busqueda.mManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                busqueda.change(1);
            }else {
                busqueda.mManager.popBackStack();
            }
        }
        // Else, nothing in the direct fragment back stack
        else{
            // Let super handle the back press
            super.onBackPressed();
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
                    return crear;
                case 1:
                    return todas;
                case 2:
                    return recomendados;
                case 3:
                    return perfil;
                case 4:
                    return busqueda;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    }

