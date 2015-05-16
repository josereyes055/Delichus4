package geekgames.delichus4.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        TextView descripcion = (TextView)laView.findViewById(R.id.paso_descripcion);
        //TextView tiempo = (TextView)laView.findViewById(R.id.paso_tiempo);
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
                descripcion.setText(paso.getString("paso"));
               // tiempo.setText("tiempo: " + paso.getInt("tiempo"));
            }

        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Unable to parse data: " + e, Toast.LENGTH_LONG).show();
        }

    }







}
