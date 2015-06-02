package geekgames.delichus4.fragments;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import geekgames.delichus4.MainActivity;
import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.Receta;
import geekgames.delichus4.adapters.ComentarioAdapter;
import geekgames.delichus4.adapters.IngredienteAdapter;
import geekgames.delichus4.customObjects.Comentario;
import geekgames.delichus4.customObjects.Ficha;
import geekgames.delichus4.customObjects.Ingrediente;
import geekgames.delichus4.customObjects.Usuario;
import geekgames.delichus4.seconds.OtherUserPage;

public class DescripcionReceta extends Fragment {

    public IngredienteAdapter mAdapter;
    public ComentarioAdapter mAdapter2;
    public ListView listView;
    public ListView listComentarios;

    TextView nombre;
    TextView autor;
    TextView larga;
    RatingBar rating;
    ImageView imagen;
    ImageView foto;
    ImageView myPhoto;
    TextView myName;
    ImageView difImg;
    TextView difText;
    TextView tiempoReceta;
    TextView cantidadPersonas;
    ScrollView sv;

    Animation animScaleSutile;

    String idReceta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.receta_intro, container, false);
        listView = (ListView) rootView.findViewById(R.id.lista_ingredientes);
        mAdapter = new IngredienteAdapter(getActivity());
        listView.setAdapter(mAdapter);
        sv = (ScrollView)rootView.findViewById(R.id.scrolldemierda);
        sv.setVisibility(View.INVISIBLE);
        animScaleSutile = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_button_sutile_animation);

        listComentarios = (ListView) rootView.findViewById((R.id.lista_comentarios));
        mAdapter2 = new ComentarioAdapter(getActivity());
        listComentarios.setAdapter(mAdapter2);

       RatingBar calificador = (RatingBar)rootView.findViewById(R.id.rating_receta_usuario);
        RatingBar.OnRatingBarChangeListener changeRating = new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float ratingNew, boolean fromUser) {
                ratingBar.setIsIndicator(true);
                Receta parent = (Receta)getActivity();
                parent.mainChangeRating( ratingNew);
            }

        };

        calificador.setOnRatingBarChangeListener(changeRating);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        idReceta = intent.getStringExtra("id");


        nombre = (TextView)getView().findViewById(R.id.receta_nombre);
        autor = (TextView)getView().findViewById(R.id.receta_autor);
        larga = (TextView)getView().findViewById(R.id.receta_larga);
        rating = (RatingBar)getView().findViewById(R.id.receta_rating);
        imagen = (ImageView)getView().findViewById(R.id.receta_imagen);
        foto = (ImageView)getView().findViewById(R.id.receta_foto);
        myPhoto = (ImageView) getView().findViewById(R.id.my_photo);
        myName = (TextView)getView().findViewById(R.id.my_name);

        difImg = (ImageView)getView().findViewById(R.id.dificultad_medidor);
        difText = (TextView)getView().findViewById(R.id.dificultad_receta);
        tiempoReceta = (TextView)getView().findViewById(R.id.tiempo_receta);
        cantidadPersonas = (TextView)getView().findViewById(R.id.cantidad_personas_receta);


        fetchReceta(idReceta);
    }

    private void fetchReceta(String idReceta){
        Toast.makeText(getActivity(), "Cargando receta", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(
                "http://www.geekgames.info/dbadmin/test.php?v=6&recipeId="+idReceta,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            setLabels(jsonObject);
                        }
                        catch(JSONException e) {
                          // Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "Error interno" , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       // Toast.makeText(getActivity(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });

        MainApplication.getInstance().getRequestQueue().add(request);
    }


    private void setLabels(final JSONObject laReceta) throws JSONException {


        nombre.setText(laReceta.getString("receta"));
        autor.setText(laReceta.getString("autor"));
        autor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleSutile);
                try {
                    MainApplication.getInstance().visitAutor(getActivity(),laReceta.getInt("idAutor"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScaleSutile);
                try {
                    MainApplication.getInstance().visitAutor(getActivity(),laReceta.getInt("idAutor"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        larga.setText(laReceta.getString("larga"));

        cantidadPersonas.setText( Integer.toString( laReceta.getInt("personas") ) );
        tiempoReceta.setText( laReceta.getInt("tiempoTotal")/60+" mins" );

        int dificultad = laReceta.getInt("dificultad");
        Drawable mecagoenDios = null;
        if(dificultad < 3 ){
            mecagoenDios = getResources().getDrawable(R.drawable.dificultad_facil);
            difText.setText("Fácil");
        }
        if(dificultad>2 && dificultad<5 ){
            mecagoenDios = getResources().getDrawable(R.drawable.dificultad_media);
            difText.setText("Media");
        }
        if(dificultad>4){
            mecagoenDios = getResources().getDrawable(R.drawable.dificultad_dificil);
            difText.setText("Difícil");
        }
        difImg.setImageDrawable(mecagoenDios);

        myName.setText(MainApplication.getInstance().sp.getString("userNombre","") +
                " - " + MainApplication.getInstance().sp.getString("userTitulo",""));
        rating.setRating(Float.parseFloat( laReceta.getString("puntuacion") ) );
        Picasso.with(getActivity())
                .load(laReceta.getString("imagen"))
                .placeholder(R.drawable.img_no_encontrada_recomendado)
                .error(R.drawable.img_no_encontrada_recetas)
                .fit().centerCrop().into(imagen);
        Picasso.with(getActivity())
                .load(laReceta.getString("foto"))
                .placeholder(R.drawable.cargando_perfil)
                .error(R.drawable.cargando_perfil)
                .fit().centerCrop().into(foto);
       Picasso.with(getActivity()).load(MainApplication.getInstance().sp.getString("userFoto","")).fit().centerCrop().into(myPhoto);

        List<Ingrediente> ingredientes = parse( laReceta.getJSONArray("ingredientes") );
        mAdapter.swapRecords(ingredientes);
        setListViewHeightBasedOnChildren(listView);

        List<Comentario> comentarios = parseComentarios( laReceta.getJSONArray("comentarios") );
        mAdapter2.swapRecords(comentarios);
        setListViewHeightBasedOnChildren(listComentarios);

        MainApplication.getInstance().losPasos = laReceta.getJSONArray("steps");

        sv.smoothScrollTo(0, 0);
        AlphaAnimation animate_apear = new AlphaAnimation(0,1);
        animate_apear.setDuration(400);
        animate_apear.setFillAfter(true);
        sv.startAnimation(animate_apear);
    }


    private List<Ingrediente> parse(JSONArray json) throws JSONException {
        ArrayList<Ingrediente> records = new ArrayList<Ingrediente>();

        JSONArray holder= json;

        for (int i=0; i<holder.length(); i++){
            JSONObject ing = holder.getJSONObject(i);
            String nombre = ing.getString("nombre");
            Double cantidad = ing.getDouble("cantidad");
            String unidad = ing.getString("medida");

            Ingrediente ingrediente = new Ingrediente(0,nombre, cantidad, unidad);
            Log.i("FUCKING DEBUG", "ingrediente añadido: "+nombre);
            records.add(ingrediente);
        }

        return records;
    }

    private List<Comentario> parseComentarios(JSONArray json) throws JSONException {
        ArrayList<Comentario> records = new ArrayList<Comentario>();

        JSONArray holder= json;

        for (int i=0; i<holder.length(); i++){
            JSONObject ing = holder.getJSONObject(i);
            String foto = ing.getString("foto");
            String autor = ing.getString("autor");
            String coment = ing.getString("comentario");
            int idAutorComentario = ing.getInt("idAutor");

            Comentario comentario = new Comentario(foto, autor, coment, idAutorComentario);
            Log.i("FUCKING DEBUG", "comentario añadido: "+autor);
            records.add(comentario);
        }


        return records;
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
            Log.i("FUCKING DEBUG", "alto: "+totalHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+80;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
