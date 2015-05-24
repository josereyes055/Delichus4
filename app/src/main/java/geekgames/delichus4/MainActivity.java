package geekgames.delichus4;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import geekgames.delichus4.Dialogs.ListDialog;
import geekgames.delichus4.fragments.BusquedaAvanzada;
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

    Crear crear;
    Todas todas;
    Recomendados recomendados;
    Perfil perfil;
    BusquedaAvanzada busqueda;
    Animation animScale;
    Animation animMove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale_button_animation);
        animMove = AnimationUtils.loadAnimation(this, R.anim.translation_button_animation);
        crear = new Crear();
        todas = new Todas();
        recomendados =new Recomendados();
        perfil = new Perfil();
        busqueda =new BusquedaAvanzada();


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(2);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                changeButtons(null, position);
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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

        int edward = (int) getResources().getDimension(R.dimen.responsive_menu);
        int alphonse = (int) getResources().getDimension(R.dimen.responsive_menu_grande);

        btn1.setPadding(edward, edward, edward, edward );
        btn2.setPadding(edward, edward, edward, edward );
        btn3.setPadding(edward, edward, edward, edward );
        btn4.setPadding(edward, edward, edward, edward );
        btn5.setPadding(edward, edward, edward, edward );


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
                btn1.setPadding(alphonse, alphonse, alphonse, alphonse);

                textView1.setTextSize(selectedSize);
                break;
            case 1:
                btn2.startAnimation(animScale);
                btn2.setImageResource(R.drawable.todas_las_recetas_selec);
                btn2.setPadding(alphonse, alphonse, alphonse, alphonse);

                textView2.setTextSize(selectedSize);
                break;
            case 2:
                btn3.startAnimation(animScale);
                btn3.setImageResource(R.drawable.recomendado_selec);
                btn3.setPadding(alphonse, alphonse, alphonse, alphonse);

                textView3.setTextSize(selectedSize);
                break;
            case 3:
                btn4.startAnimation(animScale);
                btn4.setImageResource(R.drawable.perfil_selec);
                btn4.setPadding(alphonse, alphonse, alphonse, alphonse);

                textView4.setTextSize(selectedSize);
                break;
            case 4:
                btn5.startAnimation(animScale);
                btn5.setImageResource(R.drawable.busqueda_selec);
                btn5.setPadding(alphonse, alphonse, alphonse, alphonse);

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

        ImageView animacion_op = (ImageView)findViewById(R.id.icon_logros);
        animacion_op.startAnimation(animScale);

        Intent mainIntent = new Intent().setClass(
                MainActivity.this, ActivityLogros.class);
        startActivity(mainIntent);

    }

    public void showDialog(View view){
        view.startAnimation(animMove);
        ListDialog list = new ListDialog();
        list.show(getFragmentManager(), "unTag");
    }

    public void buscarNombres(View view){
        view.startAnimation(animMove);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.busqueda_nombre);

        // set the custom dialog components - text, image and button


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_buscar_nombre);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = v.getRootView();
                EditText input = (EditText) parent.findViewById(R.id.nombre_receta);
                Intent mainIntent = new Intent().setClass(
                        getApplicationContext(), BusquedaNombre.class);

                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String nombre = input.getText().toString();
                mainIntent.putExtra("nombre", nombre);
                startActivity(mainIntent);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void gotoPage(View view){
        view.startAnimation(animScale);
        int tag = Integer.parseInt((String) view.getTag());
        mViewPager.setCurrentItem(tag, true);
        //changeButtons(view, tag);

    }


    public void buscarFiltros(View view){
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, BusquedaFiltros.class);
        startActivity(mainIntent);

    }

    public void viewOtherUser(View elView){
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, OtherUserPage.class);
        startActivity(mainIntent);
    }


    public void openFavoritos(View view){

        ImageView animacion_op = (ImageView)findViewById(R.id.icon_favoritos);
        animacion_op.startAnimation(animScale);

        Intent mainIntent = new Intent().setClass(
                MainActivity.this, ActivityFavoritos.class);
        startActivity(mainIntent);
    }

    public void openShoppingList(View view){

        ImageView animacion_op = (ImageView)findViewById(R.id.icon_shopping);
        animacion_op.startAnimation(animScale);

        Intent mainIntent = new Intent().setClass(
                MainActivity.this, ActivityShoppingList.class);
        startActivity(mainIntent);
    }

    public void openAjustes(View view){

        ImageView animacion_op = (ImageView)findViewById(R.id.icon_ajustes);
        animacion_op.startAnimation(animScale);

        Intent mainIntent = new Intent().setClass(
                MainActivity.this, ActivityAjustes.class);
        startActivity(mainIntent);
    }
//
//    public void openCocinadasReciente(View view){
//
//        ImageView animacion_op = (ImageView)findViewById(R.id.icon_cocinadas);
//        animacion_op.startAnimation(animScale);
//
//        Intent mainIntent = new Intent().setClass(
//                MainActivity.this, ActivityAjustes.class);
//        startActivity(mainIntent);
//    }
//
//    public void openAgregadasReciente(View view){
//
//        ImageView animacion_op = (ImageView)findViewById(R.id.icon_agregadas);
//        animacion_op.startAnimation(animScale);
//
//        Intent mainIntent = new Intent().setClass(
//                MainActivity.this, ActivityAjustes.class);
//        startActivity(mainIntent);
//    }


    public void openSeguidos(View view){

        ImageView animacion_op = (ImageView)findViewById(R.id.icon_seguir);
        animacion_op.startAnimation(animScale);

        Intent mainIntent = new Intent().setClass(
                MainActivity.this, ActivitySeguidos.class);
        startActivity(mainIntent);
    }

    @Override
    public void onBackPressed() {
        //Log.i("FUCKING DEBUG", "Back pressed...");
        // If the fragment exists and has some back-stack entry
        super.onBackPressed();

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

