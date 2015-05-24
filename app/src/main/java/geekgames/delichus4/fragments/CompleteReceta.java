package geekgames.delichus4.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

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

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }



}


