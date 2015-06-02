package geekgames.delichus4.seconds;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.PasoRecetaAdapter;
import geekgames.delichus4.customObjects.Ficha;
import geekgames.delichus4.customObjects.ImagenPaso;

public class OtherUserPage extends ActionBarActivity {

    String idUser;
    private String currentHeader = "";
    private PasoRecetaAdapter aportesAdapter;
    private PasoRecetaAdapter agregadasAdapter;
    ScrollView sv;
    JSONArray recetas;
    Animation animScaleRectangular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otro_usuario);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        idUser = intent.getStringExtra("id");
        sv = (ScrollView)findViewById(R.id.otroscrolldemierda);
        sv.setVisibility(View.INVISIBLE);
        aportesAdapter = new PasoRecetaAdapter(getApplicationContext());
        agregadasAdapter = new PasoRecetaAdapter(getApplicationContext());
        animScaleRectangular = AnimationUtils.loadAnimation(this, R.anim.scale_button_rectangular_animation);

        TwoWayView sliderAportes = (TwoWayView)findViewById(R.id.fotos_aportes);
        TwoWayView sliderAgregadas = (TwoWayView)findViewById(R.id.fotos_recetas_agregadas);
        sliderAportes.setAdapter(aportesAdapter);
        sliderAgregadas.setAdapter(agregadasAdapter);

        String stringArray = MainApplication.getInstance().sp.getString("recetas",null);
        if( stringArray != null ){
            try {
                recetas = new JSONArray(stringArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


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
        Toast.makeText(getApplicationContext(), "Cargando información del usuario", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=22&userId="+idUser+"&visitorId="+MainApplication.getInstance().sp.getInt("userId",0),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject userData = jsonObject;

                            ImageView foto = (ImageView)findViewById(R.id.other_user_foto);
                            Picasso.with(getApplicationContext()).load(userData.getString("foto")).placeholder(R.drawable.cargando_perfil).fit().centerCrop().into(foto);
                            TextView nombre = (TextView)findViewById(R.id.su_name);
                            TextView nombre2 = (TextView)findViewById(R.id.su_name_reputacion);
                            TextView titulo = (TextView)findViewById(R.id.otro_perfil_titulo);
                            TextView nivel = (TextView) findViewById(R.id.otro_nivel);
                            TextView reputa = (TextView)findViewById(R.id.otro_reputacion);
                            TextView seguidores = (TextView)findViewById(R.id.personas_que_lo_siguen);
                            final ImageView favBtn = (ImageView) findViewById(R.id.seguir);
                            Log.i("FUCKING DEBUG", "el bool = "+userData.getBoolean("follow"));
                            boolean seguido = userData.getBoolean("follow");
                            if( seguido == false ) {

                                favBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        v.startAnimation(animScaleRectangular);
                                        MainApplication.getInstance().addFollow(MainApplication.getInstance().sp.getInt("userId", 0), Integer.parseInt(idUser));
                                        Drawable drawSpeak = getResources().getDrawable(R.drawable.siguiendo);
                                        favBtn.setImageDrawable(drawSpeak);
                                        favBtn.setEnabled(false);

                                    }
                                });
                            }
                            else{
                                Drawable drawSpeak = getResources().getDrawable(R.drawable.siguiendo);
                                favBtn.setImageDrawable(drawSpeak);
                            }


                                ArrayList<ImagenPaso> laListaAgregadas = new ArrayList<>();
                                ArrayList<ImagenPaso> laListaAportes  = new ArrayList<>();

                                JSONArray aportes = userData.getJSONArray("aportes");
                                JSONArray agregados = userData.getJSONArray("agregados");

                                if (aportes.length() > 0) {
                                    for (int i = 0; i < aportes.length(); i++) {
                                        String laFoto = aportes.getString(i);
                                        ImagenPaso unaImagen = new ImagenPaso(laFoto);
                                        laListaAportes.add(unaImagen);
                                    }
                                }

                                if (aportes.length() < 2) {
                                    for (int i = 0; i < 3; i++) {
                                        ImagenPaso otraImagen = new ImagenPaso("empty");
                                        laListaAportes.add(otraImagen);
                                    }
                                }


                            if (agregados.length() > 0) {
                                for (int i = 0; i < agregados.length(); i++) {
                                    JSONObject completeObj = agregados.getJSONObject(i);
                                    int compId = completeObj.getInt("upId");

                                    if(recetas != null ){
                                        for (int j = 0; j<recetas.length(); j++){
                                            JSONObject receta = recetas.getJSONObject(j);
                                            if(receta.getInt("id") == compId){
                                                String laFoto = receta.getString("imagen");
                                                ImagenPaso unaImagen = new ImagenPaso(laFoto);
                                                laListaAgregadas.add(unaImagen);
                                            }
                                        }
                                    }

                                }
                            }

                            if (agregados.length() < 2) {
                                for (int i = 0; i < 3; i++) {
                                    ImagenPaso otraImagen = new ImagenPaso("empty");
                                    laListaAgregadas.add(otraImagen);
                                }
                            }



                                agregadasAdapter.swapRecords(laListaAgregadas);
                                aportesAdapter.swapRecords(laListaAportes);


                            RatingBar rating = (RatingBar)findViewById(R.id.otro_rating);
                            nombre.setText(userData.getString("nombre") );
                            nombre2.setText( userData.getString("nombre") );
                            titulo.setText(userData.getString("titulo"));
                            nivel.setText(userData.getString("nivel"));
                            reputa.setText(userData.getInt("promedio")+".0");
                            rating.setRating(userData.getInt("promedio"));
                            seguidores.setText(userData.getInt("seguidores")+"");

                            sv.smoothScrollTo(0, 0);
                            AlphaAnimation animate_apear = new AlphaAnimation(0,1);
                            animate_apear.setDuration(400);
                            animate_apear.setFillAfter(true);
                            sv.startAnimation(animate_apear);

                        }
                        catch(JSONException e) {
                           // Toast.makeText(getApplicationContext(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Error interno" , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       // Toast.makeText(getApplicationContext(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Error en la base de datos" , Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
        //MainApplication.getInstance().fetchUserAchievements(  MainApplication.getInstance().getUserId() );
    }





}
