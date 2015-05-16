package geekgames.delichus4.fragments.busquedas;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import geekgames.delichus4.R;
import geekgames.delichus4.fragments.Busqueda;

public class BusquedaFiltros extends Fragment {

    LinearLayout filtros;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.busqueda_filtros, container, false);
        filtros = (LinearLayout)rootView.findViewById(R.id.btnSelectFiltros);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        filtros.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Busqueda parent = (Busqueda) getParentFragment();
                boolean[] checks = parent.chosen;

                checks[0] = ((CheckBox) rootView.findViewById(R.id.check_cantidad)).isChecked();
                checks[1] = ((CheckBox) rootView.findViewById(R.id.check_tiempo)).isChecked();
                checks[2] = ((CheckBox) rootView.findViewById(R.id.check_categoria)).isChecked();
                checks[3] = ((CheckBox) rootView.findViewById(R.id.check_tipo)).isChecked();
                checks[4] = ((CheckBox) rootView.findViewById(R.id.check_ingredientes)).isChecked();
                checks[5] = ((CheckBox) rootView.findViewById(R.id.check_coccion)).isChecked();

                parent.chosen = checks;

                 parent.change(2);
            }});

    }


}
