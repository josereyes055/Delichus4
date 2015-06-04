package geekgames.delichus4.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.customObjects.Logro;


public class TituloAdapter extends ArrayAdapter<Logro> {

    private Context idkContext ;

    public TituloAdapter(Context context){
        super(context, R.layout.lista_titulos);
        idkContext = context;
    }

    public void swapRecipeRecords(List<Logro> objects) {
        clear();

        for(Logro object : objects) {
            add(object);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Logro logro = getItem(position);


        convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_titulos, parent, false);

        //NOTE: You would normally use the ViewHolder pattern here
        TextView nombre = (TextView) convertView.findViewById(R.id.titulillo);
        final LinearLayout fondo = (LinearLayout)convertView.findViewById(R.id.contenedor_titulo);
        final TextView letras = (TextView)convertView.findViewById(R.id.titulillo);
        fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("userTitulo", logro.getTitulo());
                editor.commit();
                fondo.setBackgroundColor(getContext().getResources().getColor(R.color.verde));
                letras.setTextColor(getContext().getResources().getColor(R.color.blanco));
                Toast.makeText(getContext(), "Cambiaste tu titulo!", Toast.LENGTH_SHORT).show();
            }
        });

        String titulo = logro.getTitulo();
        if(titulo.equals( MainApplication.getInstance().sp.getString("userTitulo", null) ) ){
            //Log.i("FUCKING DEBUG", "ej igualicto!");
            fondo.setBackgroundColor(getContext().getResources().getColor(R.color.verde));
        }

        nombre.setText(logro.getTitulo());

        return convertView;
    }
}
