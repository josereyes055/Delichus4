package geekgames.delichus4.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.Receta;

public class CompleteReceta extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.receta_fin, container, false);

        RatingBar calificador = (RatingBar)rootView.findViewById(R.id.ratingFinal);
        RatingBar.OnRatingBarChangeListener changeRating = new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float ratingNew, boolean fromUser) {
                ratingBar.setIsIndicator(true);
                Receta parent = (Receta)getActivity();
                Log.i("FUCKING DEBIG", "nuevo: " + ratingNew);
                parent.mainChangeRating( ratingNew );
            }

        };


        calificador.setOnRatingBarChangeListener(changeRating);
        Receta parent = (Receta)getActivity();
        addComplete(MainApplication.getInstance().sp.getInt("userId", 0), parent.idReceta);
        return rootView;
    }

    public void addComplete( int idUser, String idReceta){


        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=21&userId="+idUser+"&recipeId="+idReceta,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject userData = jsonObject;

                            String result = userData.getString("status");
                            //Toast.makeText(getApplicationContext(), "A�adido a favoritos", Toast.LENGTH_SHORT).show();
                            JSONArray logros = userData.getJSONArray("logrosObtenidos");
                            if(logros.length() > 0 ) {
                                for (int i = 0; i < logros.length(); i++){
                                    alertaLogro(logros.getInt(i));
                                }
                            }


                        }
                        catch(JSONException e) {

                            Toast.makeText(getActivity(), getString(R.string.error_seguidos)  , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), getString(R.string.internet)  , Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().mRequestQueue.add(request);

    }

    public void alertaLogro( int idLogro){

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefArray = app_preferences.getString("logros", null);

        try {


            JSONArray logros = new JSONArray(prefArray);
            for (int i= 0; i<logros.length(); i++){
                JSONObject logro = logros.getJSONObject( i);
                if ( idLogro == Integer.parseInt(logro.getString("id")) ){

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.logro_desbloqueado);

                    TextView nombre = (TextView)dialog.findViewById(R.id.logro_desbloqueado);
                    TextView titulo = (TextView)dialog.findViewById(R.id.titulo_desbloqueado);
                    TextView descripcion = (TextView)dialog.findViewById(R.id.descripcion_logro);
                    // set the custom dialog components - text, image and button

                    titulo.setText(logro.getString("titulo") );
                    descripcion.setText(logro.getString("descripcion"));
                    nombre.setText( logro.getString("logro") );


                    Button dialogButton = (Button) dialog.findViewById(R.id.ok_logro);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    Vibrator v = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
                    v.vibrate(pattern, -1);
                    MainApplication.logro.start();
                    dialog.show();



                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }



}


