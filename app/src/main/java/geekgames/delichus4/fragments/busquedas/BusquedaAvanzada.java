package geekgames.delichus4.fragments.busquedas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import geekgames.delichus4.R;
import geekgames.delichus4.fragments.Busqueda;


public class BusquedaAvanzada extends Fragment {

    Button filtros;
    BusquedaAvanzada instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.busqueda_avanzada, container, false);

        filtros = (Button)rootView.findViewById(R.id.btnFiltros);
        instance = this;


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
                parent.change(1);
            }});

    }





}