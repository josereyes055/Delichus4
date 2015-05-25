package geekgames.delichus4.seconds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

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


public class Tutorial extends ActionBarActivity {

    int pos = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void changeImagen(View view){


        pos ++;
        if(pos < 11) {
            ImageView img = (ImageView)findViewById(R.id.imagenTuto);
            int idDraw = getResources().getIdentifier("tuto"+pos, "drawable", getApplicationContext().getPackageName());
            if( idDraw != 0){
                img.setImageResource(idDraw);
            }
        }else{
            Intent mainIntent = new Intent().setClass(
                    Tutorial.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
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

