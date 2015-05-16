package geekgames.delichus4.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import geekgames.delichus4.R;
import geekgames.delichus4.fragments.busquedas.BusquedaAvanzada;
import geekgames.delichus4.fragments.busquedas.BusquedaFiltros;
import geekgames.delichus4.fragments.busquedas.ConfigFiltros;
import geekgames.delichus4.fragments.busquedas.ResultFiltros;


public class Busqueda extends Fragment {

    public FragmentManager mManager;
    public boolean[] chosen;
    public int[] values;
    int cantFiltros = 6;

    BusquedaAvanzada advanced;
    BusquedaFiltros filtros;
    ConfigFiltros configFiltros;
    ResultFiltros resultFiltros;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.busqueda, container, false);

        mManager = getChildFragmentManager();

        advanced = new BusquedaAvanzada();
        filtros = new BusquedaFiltros();
        configFiltros = new ConfigFiltros();
        resultFiltros = new ResultFiltros();

        mManager.beginTransaction()
                .add(R.id.searchContainer, advanced).commit();


        chosen = new boolean[cantFiltros];
        values = new int[cantFiltros];

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void change( int lvl ){

        switch (lvl){

            case 1:
                filtros = new BusquedaFiltros();
                mManager.beginTransaction().replace(R.id.searchContainer, filtros)
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                configFiltros = new ConfigFiltros();
                mManager.beginTransaction().replace(R.id.searchContainer, configFiltros)
                        .addToBackStack(null)
                        .commit();
                break;
            case 3:
                resultFiltros = new ResultFiltros();
                mManager.beginTransaction().replace(R.id.searchContainer, resultFiltros)
                        .addToBackStack(null)
                        .commit();
                break;

        }

    }



}
