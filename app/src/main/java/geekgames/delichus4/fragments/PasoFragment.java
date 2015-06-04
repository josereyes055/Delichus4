package geekgames.delichus4.fragments;


import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.PasoRecetaAdapter;
import geekgames.delichus4.customObjects.ImagenPaso;

public class PasoFragment extends Fragment{
    View laView;
    Bundle saved;
    private PasoRecetaAdapter mAdapter;
    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static PasoFragment newInstance(int index) {
        PasoFragment f = new PasoFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.receta_paso, container, false);
        laView = rootView;
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new PasoRecetaAdapter(getActivity());

        TextView tipo = (TextView)laView.findViewById(R.id.paso_tipo);
        TextView numero = (TextView)laView.findViewById(R.id.numero_paso);
        TextView descripcion = (TextView)laView.findViewById(R.id.paso_descripcion);
        ImageView imagen = (ImageView)laView.findViewById(R.id.paso_imagen);
        final TextView tiempo = (TextView)laView.findViewById(R.id.leChronometre);
        ImageButton btnTiempo = (ImageButton)laView.findViewById(R.id.cronometro);
        TwoWayView slider = (TwoWayView)laView.findViewById(R.id.fotosPasos);
        slider.setAdapter(mAdapter);





        Bundle args = getArguments();
        int index = args.getInt("index", 0);

        JSONArray pasos = MainApplication.getInstance().losPasos;
        try {
            JSONObject paso = pasos.getJSONObject(index);
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String prefArray = app_preferences.getString("tipo_pasos", null);
            if( prefArray != null ) {
                //JSONObject unArray = MainApplication.getInstance().tipo_pasos;
                JSONObject unArray = new JSONObject(prefArray);
                String indexTipo = String.valueOf(paso.getInt("tipo"));
                String elTipo = unArray.getString(indexTipo);
                JSONArray lasFotos = paso.getJSONArray("fotosP");
                ArrayList<ImagenPaso> laLista = new ArrayList<>();

                if (lasFotos.length() > 0) {
                    for (int i = 0; i < lasFotos.length(); i++) {
                        String unString = lasFotos.getString(i);

                        ImagenPaso unaImagen = new ImagenPaso(unString);
                        laLista.add(unaImagen);

                    }
                }

                if (lasFotos.length() < 2) {
                    for (int i = 0; i < 3; i++) {
                        ImagenPaso otraImagen = new ImagenPaso("empty");
                        laLista.add(otraImagen);
                    }
                }
                Log.i("FUCKING DEBUG", "items: " + laLista.size());
                mAdapter.swapRecords(laLista);

                tipo.setText(elTipo);

                int idDraw = getResources().getIdentifier("paso"+paso.getInt("tipo"), "drawable", getActivity().getPackageName());
                if( idDraw != 0){
                    imagen.setImageResource(idDraw);
                }

                numero.setText(String.valueOf(index + 1));
                descripcion.setText(paso.getString("paso"));

                int losSegundos = paso.getInt("tiempo");
                final boolean running = false;

                if( losSegundos > 0 ){
                    tiempo.setText( formatearTiempo( (double)losSegundos) );
                    final CountDownTimer timer = new CountDownTimer(losSegundos*1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            tiempo.setText( formatearTiempo( (double)millisUntilFinished/1000) );
                        }

                        public void onFinish() {
                            tiempo.setText("done!");
                        }
                    };

                    btnTiempo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            timer.start();

                        }
                    });


                }else{
                    btnTiempo.setVisibility(View.INVISIBLE);
                }

            }

        } catch (JSONException e) {
            Toast.makeText(getActivity(), getString(R.string.error_interno)  , Toast.LENGTH_SHORT).show();
        }

    }

    String formatearTiempo(double secs){
        String tiempo = "";
        double calculo = secs / 60;
        double entero = Math.floor( calculo );
        double sobra = calculo - entero;
        double seconds = sobra*60;
        Log.i("FUCKING DEBUG",calculo+","+entero+","+sobra+","+seconds);
        int minutos =  (int)entero ;
        int segundos = (int)seconds;
        tiempo = minutos+":"+segundos;
        return  tiempo;
    }







}
