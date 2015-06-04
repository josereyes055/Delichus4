package geekgames.delichus4.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import geekgames.delichus4.MainActivity;
import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.FichaAdapter;
import geekgames.delichus4.customObjects.Ficha;
import geekgames.delichus4.seconds.OtherUserPage;




public class Recomendados extends Fragment {


    Animation animScale;
    Animation animScaleSutile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recomendados, container, false);
        animScale = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_button_animation);
        return rootView;

    }

    private FichaAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        animScaleSutile = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_button_sutile_animation);
        fetch();
    }

    private void fetch() {
        String query = MainApplication.getInstance().sp.getString("query","");
        JsonObjectRequest request = new JsonObjectRequest(
                query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            //List<Recipe> recipeRecords = parse(jsonObject);
                            setLabels(jsonObject);
                            //mAdapter.swapRecipeRecords(recipeRecords);
                        }
                        catch(JSONException e) {
                          //  Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), getString(R.string.error_interno)  , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                      //  Toast.makeText(getActivity(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), getString(R.string.internet)  , Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
    }

    private void setLabels(JSONObject ficha) throws JSONException{

        int id = ficha.getInt("id");
        String nombre = ficha.getString("receta");
        String imagen = ficha.getString("imagen");
        final int idAutor = Integer.parseInt(ficha.getString("idAutor"));
        String autor = ficha.getString("autor");
        String foto = ficha.getString("foto");
        float puntuacion = Float.parseFloat(ficha.getString("puntuacion"));
        String descripcion = ficha.getString("descripcion");
        int pasos = ficha.getInt("pasos");

        final Ficha unaFicha = new Ficha(id, nombre, imagen, idAutor, autor, foto, puntuacion, descripcion, pasos);


        TextView tnombre = (TextView) getView().findViewById(R.id.recipe_nombre);
        ImageView imagenV = (ImageView) getView().findViewById(R.id.recipe_imagen);
        imagenV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleSutile);
                Log.i("FUCKING DEBUG", "la receta es " + unaFicha.nombre);
                MainApplication.getInstance().exploreRecipe(getActivity(),unaFicha.id, unaFicha.nombre, unaFicha.descripcion, unaFicha.imagen, unaFicha.pasos);
            }
        });
        ImageView favBtn = (ImageView)getView().findViewById(R.id.recomendado_fav);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.i("FUCKING DEBUG", "se va a adherir " + unaFicha.nombre +" como favorito" );
               v.startAnimation(animScale);
               MainApplication.mp.start();
               MainApplication.getInstance().addFav( MainApplication.getInstance().sp.getInt("userId",0), unaFicha.id );

            }
        });
        ImageView fotoV = (ImageView) getView().findViewById(R.id.recipe_foto);
        fotoV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleSutile);
                    MainApplication.getInstance().visitAutor(getActivity(),idAutor);
            }
        });
        TextView autorV = (TextView) getView().findViewById(R.id.recipe_autor);
        autorV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleSutile);
                MainApplication.getInstance().visitAutor(getActivity(),idAutor);
            }
        });
        RatingBar puntuacionV = (RatingBar) getView().findViewById(R.id.recipe_puntuacion);
        TextView descripcionV = (TextView) getView().findViewById(R.id.recipe_descripcion);



        tnombre.setText(unaFicha.nombre);
        Picasso.with(getActivity())
                .load(unaFicha.imagen)
                .placeholder(R.drawable.img_no_encontrada_recomendado)
                .error(R.drawable.img_no_encontrada_recetas)
                .fit().centerCrop().into(imagenV);
        Picasso.with(getActivity())
                .load(unaFicha.foto)
                .placeholder(R.drawable.noob)
                .placeholder(R.drawable.cargando_perfil)
                .error(R.drawable.noob)
                .fit().centerCrop().into(fotoV);
        autorV.setText(unaFicha.autor);
        descripcionV.setText(descripcion);
        puntuacionV.setRating(unaFicha.puntuacion);

    }

}
