package geekgames.delichus4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import geekgames.delichus4.customObjects.Ingrediente;
import geekgames.delichus4.customViews.CustomPager;
import geekgames.delichus4.fragments.ActivarAyudante;
import geekgames.delichus4.fragments.CompleteReceta;
import geekgames.delichus4.fragments.DescripcionReceta;
import geekgames.delichus4.fragments.PasoFragment;

public class Receta extends ActionBarActivity{

    private static final int CAMERA_IMAGE = 1;
    private static final int REQUEST_PHOTO_STEP = 2;
    Animation animScale;
    Animation animScaleSutile;
    Animation animScaleRectangular;

    SectionsPagerAdapter mSectionsPagerAdapter;
    CustomPager mViewPager;
    //ViewPager mViewPager;
    TextToSpeech ttobj;
    boolean laVieja = false;

    ImageButton play;
    ImageButton prev;
    ImageButton next;
    ImageButton speak;

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String mCurrentPhotoPath;

    String nombreReceta;
    String descripcionReceta;
    String imagenReceta;
    String pasosReceta;
    public String idReceta;
    public int idPaso;

    int tipoFoto = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receta);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        animScale = AnimationUtils.loadAnimation(this, R.anim.scale_button_animation);
        animScaleSutile = AnimationUtils.loadAnimation(this, R.anim.scale_button_sutile_animation);
        animScaleRectangular = AnimationUtils.loadAnimation(this, R.anim.scale_button_rectangular_animation);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomPager) findViewById(R.id.pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        prev = (ImageButton) findViewById(R.id.atras);
        next = (ImageButton) findViewById(R.id.adelantar);
        speak = (ImageButton) findViewById(R.id.asistente);
        play = (ImageButton) findViewById(R.id.play);

        hideButtons();

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        ttobj=new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            Locale locSpanish = new Locale("spa", "MEX");
                            ttobj.setLanguage(locSpanish);
                        }
                    }
                });


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getApplicationContext(), getString(R.string.si_foto)  , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(){

            }

            @Override
            public void onError(FacebookException exception){
                Toast.makeText(getApplicationContext(), getString(R.string.no_foto)  , Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        idReceta = intent.getStringExtra("id");
        nombreReceta = intent.getStringExtra("nombre");
        descripcionReceta = intent.getStringExtra("descripcion");
        imagenReceta = intent.getStringExtra("imagen");
        pasosReceta = intent.getStringExtra("pasos");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        if(ttobj !=null){
            ttobj.stop();
            ttobj.shutdown();
        }
        super.onPause();
    }



    public void comentar(View view){
        view.startAnimation(animScaleRectangular);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.hacer_comentario);

        TextView autor = (TextView)dialog.findViewById(R.id.opinion_autor);
        TextView titulo = (TextView)dialog.findViewById(R.id.opinion_titulo);
        ImageView foto = (ImageView)dialog.findViewById(R.id.opinion_foto);
        autor.setText(MainApplication.getInstance().sp.getString("userNombre",""));
        titulo.setText(MainApplication.getInstance().sp.getString("userTitulo",""));
        Picasso.with(getApplicationContext()).load(MainApplication.getInstance().sp.getString("userFoto","")).placeholder(R.drawable.cargando_perfil).fit().centerCrop().into(foto);
        // set the custom dialog components - text, image and button


        Button dialogButton = (Button) dialog.findViewById(R.id.opinion_enviar);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = v.getRootView();
                EditText input = (EditText) parent.findViewById(R.id.opinion_text);

                RatingBar rating = (RatingBar)parent.findViewById(R.id.opinion_rating);
                if(rating.getRating() != 0 ) {
                    MainApplication.getInstance().calificar(idReceta, rating.getRating());
                }
                String inputText = input.getText().toString();
                if( inputText.length() > 0) {
                    inputText = inputText.replaceAll("\\s+", "%20");
                    String query = "http://www.geekgames.info/dbadmin/test.php?v=19&userId=" +
                            String.valueOf(MainApplication.getInstance().sp.getInt("userId", 0)) +
                            "&recipeId=" + idReceta +
                            "&comentario=" + inputText;
                    Log.i("FUCKING DEBUG", query);

                    JsonObjectRequest request = new JsonObjectRequest(
                            query,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {

                                    Toast.makeText(getApplicationContext(), getString(R.string.coment_guardado), Toast.LENGTH_SHORT).show();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.coment_no_guardado), Toast.LENGTH_SHORT).show();
                                }
                            });

                    MainApplication.getInstance().getRequestQueue().add(request);

                    dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "No has escrito nada...",Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }


    public void shutUpVieja(View view){
        view.startAnimation(animScale);
        laVieja = false;
        ToggleButton tb = (ToggleButton)findViewById(R.id.viejaToggle);
        tb.setChecked(false);
        Toast.makeText(getApplicationContext(), "Asistente desabilitada",Toast.LENGTH_LONG).show();
    }

    public void letItBeVieja(View view){
        view.startAnimation(animScale);
        laVieja = true;
        ToggleButton tb = (ToggleButton)findViewById(R.id.viejaToggle);
        tb.setChecked(true);
        Toast.makeText(getApplicationContext(), "Asistente habilitada",Toast.LENGTH_LONG).show();
    }

    public void toggleVieja(View view){
        laVieja = !laVieja;

        Drawable drawSpeak;
        if( laVieja ){
            drawSpeak = getResources().getDrawable(R.drawable.asistente);
            Toast.makeText(getApplicationContext(), "Asistente habilitada",Toast.LENGTH_LONG).show();
            try {
                //Log.i("FUCKING DEBUF", "id = "+ MainApplication.getInstance().losPasos.getJSONObject().getInt("id"));
                JSONArray pasos = MainApplication.getInstance().losPasos;

                JSONObject paso = pasos.getJSONObject(mViewPager.getCurrentItem()-2);
                String text = paso.getString("paso");
                if(laVieja) {
                    ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {

            drawSpeak = getResources().getDrawable(R.drawable.no_asistente);
            Toast.makeText(getApplicationContext(), "Asistente desabilitada",Toast.LENGTH_LONG).show();

        }
        speak.setImageDrawable(drawSpeak);
    }

    public void beginRecipe(View view) {
        view.startAnimation(animScaleRectangular);
        mViewPager.setCurrentItem(1);
    }

    public void comenzarReceta(View view) {
        view.startAnimation(animScaleRectangular);
        mViewPager.setCurrentItem(2);
        mViewPager.enableSwipe(true);
        //Log.i("FUCKING DEBUG", "onreceta:"+mViewPager.onrecipe+" onsteps:"+mViewPager.onsteps);
        showButtons();
    }

    public void showButtons(){
        Drawable drawPrev = getResources().getDrawable(R.drawable.atrasar);
        Drawable drawPlay = getResources().getDrawable(R.drawable.play);
        Drawable drawNexr = getResources().getDrawable(R.drawable.adelantar);
        Drawable drawSpeak;
        if( laVieja ){
            drawSpeak = getResources().getDrawable(R.drawable.asistente);
        }else {
            drawSpeak = getResources().getDrawable(R.drawable.no_asistente);
        }

        prev.setImageDrawable(drawPrev);
        play.setImageDrawable(drawPlay);
        next.setImageDrawable(drawNexr);
        speak.setImageDrawable(drawSpeak);

        prev.setEnabled(true);
        play.setEnabled(true);
        next.setEnabled(true);
        speak.setEnabled(true);
    }

    public void hideButtons(){
        prev.setImageDrawable(null);
        play.setImageDrawable(null);
        next.setImageDrawable(null);
        speak.setImageDrawable(null);
        prev.setEnabled(false);
        play.setEnabled(false);
        next.setEnabled(false);
        speak.setEnabled(false);
    }

    public  void goBack(View view){
        view.startAnimation(animScaleSutile);
        if (mViewPager.getCurrentItem() > 0 ){
            mViewPager.setCurrentItem(0);
            mViewPager.enableSwipe(false);
            hideButtons();
        }
        else {
            onBackPressed();
        }
    }

    public void recetaPrev(View view){
        view.startAnimation(animScaleSutile);
        int state = mViewPager.getCurrentItem();
        if( state > 2 ){
            state --;
        }
        mViewPager.setCurrentItem(state);
    }

    public void recetaNext(View view){
        view.startAnimation(animScaleSutile);
        int state = mViewPager.getCurrentItem();
        if( state < mSectionsPagerAdapter.getCount() ){
            state ++;
        }
        mViewPager.setCurrentItem(state);
    }

    public void mainChangeRating(float ratingNew){

        MainApplication.getInstance().calificar(idReceta, ratingNew);

       /*RatingBar lerat = (RatingBar)findViewById(R.id.receta_rating);
        float newval =  (float)( lerat.getRating()+ratingNew )/2;
        if( lerat.getRating() != 0){
            lerat.setRating( newval );
        }else{
            lerat.setRating(ratingNew);
        }*/

    }

    public void recetaPlay(View view){

        view.startAnimation(animScale);
        try {
            //Log.i("FUCKING DEBUF", "id = "+ MainApplication.getInstance().losPasos.getJSONObject().getInt("id"));
            JSONArray pasos = MainApplication.getInstance().losPasos;

            JSONObject paso = pasos.getJSONObject(mViewPager.getCurrentItem()-2);
            String text = paso.getString("paso");
            if(laVieja) {
                ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void selectAllIngredients(View view){

        view.startAnimation(animScaleRectangular);
        ListView listaIngrediente = (ListView) findViewById(R.id.lista_ingredientes);
        int totalIngredientes = listaIngrediente.getChildCount();
        Button selectAllButton = (Button) findViewById(R.id.select_all);
        CheckBox checkBox;
        boolean allChecked = true;

        for (int i = 0; i < totalIngredientes; i++){
            checkBox = (CheckBox) listaIngrediente.getChildAt(i).findViewById(R.id.checkBox);
            allChecked = allChecked && checkBox.isChecked();
        }

        for (int i = 0; i < totalIngredientes; i++){
            checkBox = (CheckBox) listaIngrediente.getChildAt(i).findViewById(R.id.checkBox);
            checkBox.setChecked(!allChecked);
        }

        if(allChecked) {
            selectAllButton.setBackground(this.getResources().getDrawable(R.color.verde));
        } else {
            selectAllButton.setBackground(this.getResources().getDrawable(R.color.naranja));
        }
    }

    public void carrito_ingredientes(View view) {

        view.startAnimation(animScaleRectangular);
        ListView listaIngrediente = (ListView) findViewById(R.id.lista_ingredientes);
        int totalIngredientes = listaIngrediente.getChildCount();
        for (int i = 0; i < totalIngredientes; i++){
            CheckBox checkBox = (CheckBox) listaIngrediente.getChildAt(i).findViewById(R.id.checkBox);
            if(!checkBox.isChecked()){
                Log.i("FUCKING DEBUG", "agregando item " + i+" a la shoppingList");

                String nombre = ((TextView) listaIngrediente.getChildAt(i).findViewById(R.id.ingrediente_nombre)).getText().toString();
                String unidad = ((TextView) listaIngrediente.getChildAt(i).findViewById(R.id.ingrediente_unidad)).getText().toString();
                String cantidad = ((TextView) listaIngrediente.getChildAt(i).findViewById(R.id.ingrediente_cantidad)).getText().toString();

                Ingrediente ingrediente = new Ingrediente(-1, nombre, Double.parseDouble(cantidad), unidad);


                MainApplication.getInstance().shoppingList.add(ingrediente);
            }
        }
             Toast.makeText(getApplicationContext(), getString(R.string.lista_compras)  , Toast.LENGTH_SHORT).show();
    }

    public void validateCheckbox(){
        ListView listaIngrediente = (ListView) findViewById(R.id.lista_ingredientes);
        int totalIngredientes = listaIngrediente.getChildCount();
        Button selectAllButton = (Button) findViewById(R.id.select_all);
        Button comenzar = (Button) findViewById((R.id.botonComenzar));
        CheckBox checkBox;
        boolean allChecked = true;

        for (int i = 0; i < totalIngredientes; i++){
            checkBox = (CheckBox) listaIngrediente.getChildAt(i).findViewById(R.id.checkBox);
            allChecked = allChecked && checkBox.isChecked();
        }

        if(allChecked) {
            selectAllButton.setBackground(this.getResources().getDrawable(R.color.naranja));
        } else {
            selectAllButton.setBackground(this.getResources().getDrawable(R.color.verde));
        }
    }

    public void onCameraClick(View view){
        tipoFoto = 1;
        try {
            idPaso = MainApplication.getInstance().losPasos.getJSONObject(mViewPager.getCurrentItem()-2).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("FUCKING DEBUG", "id paso = "+idPaso);
        Log.i("FUCKING DEBUG","el gran triplemalparido tipo es: "+tipoFoto);
        ImageView animacion_op = (ImageView)findViewById(R.id.camara);
        animacion_op.startAnimation(animScaleSutile);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_PHOTO_STEP);
            }
        }
    }

    public void onShareRecipe(View view) {
        view.startAnimation(animScale);

        /*ImageView image = (ImageView)this.findViewById(R.id.receta_imagen);
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache(true);

        Bitmap bitMapImage = Bitmap.createBitmap(image.getDrawingCache());
        image.setDrawingCacheEnabled(false);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitMapImage)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();*/

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(this.nombreReceta)
                    .setContentDescription(this.descripcionReceta)
                    .setImageUrl(Uri.parse(this.imagenReceta))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    public void onTakePictureRecipe(View view){
        view.startAnimation(animScaleRectangular);

        tipoFoto = 2;
        Log.i("FUCKING DEBUG","el gran triplemalparido tipo es: "+tipoFoto);
        ImageView animacion_op = (ImageView)findViewById(R.id.boton_camara_grande);
        animacion_op.startAnimation(animScaleSutile);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_IMAGE);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_PHOTO_STEP){
            if (resultCode != RESULT_OK) return;

            this.sendPhotoToServer();

        }else if(requestCode == CAMERA_IMAGE){
            if (resultCode != RESULT_OK) return;

            this.sendPhotoToServer();


            if (ShareDialog.canShow(SharePhotoContent.class)) {

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

                Bitmap bitMapImage = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitMapImage)
                        .build();

                SharePhotoContent photoContent = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                shareDialog.show(photoContent);
            }

        }else if(requestCode == shareDialog.getRequestCode()){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Envia una imagen al servidor por medio de una petición POST
     */
    public void sendPhotoToServer(){
        uploadFile(mCurrentPhotoPath);
    }

    public void uploadFile(String sourceFileUri) {
        new UploadFilesTask().execute(sourceFileUri);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private class UploadFilesTask extends AsyncTask<String, Integer, Integer> {
        protected Integer doInBackground(String... params) {
            String upLoadServerUri = "http://www.geekgames.info/dbadmin/php/imgUpload.php";
            String fileName = params[0];
            int serverResponseCode = 0;

            HttpURLConnection conn;
            DataOutputStream dos;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(fileName);
            if (!sourceFile.isFile()) {
                Log.e("uploadFile", "Source File Does not exist");
            }
            try { // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("imagen", fileName);
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"imagen\";filename=\""+ fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                //close the streams //

                String query = "http://www.geekgames.info/dbadmin/test.php?v=26";
                if(tipoFoto == 1){
                    query += "&tipo=paso";
                    query += "&recipeId="+idPaso;
                    query += "&userId="+MainApplication.getInstance().sp.getInt("userId",0)+"&imageName="+sourceFile.getName();
                    MainApplication.getInstance().uptadteScore(10);
                }
                if(tipoFoto == 2){
                    query += "&tipo=receta";
                    query += "&recipeId="+idReceta;
                    query += "&userId="+MainApplication.getInstance().sp.getInt("userId",0)+"&imageName="+sourceFile.getName();
                    MainApplication.getInstance().uptadteScore(15);
                }
                JsonObjectRequest request = new JsonObjectRequest(
                        query,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.i("Photo","Imagen Actualizada");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.i("Photo","Error al actualizar imagen");
                            }
                        });
                MainApplication.getInstance().getRequestQueue().add(request);



                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();
                //Toast.makeText(this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Upload file to server", "Exception : " + e.getMessage(), e);
            }

            return serverResponseCode;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        Intent intent = getIntent();
        String lpasosReceta = intent.getStringExtra("pasos");

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            int numPasos = Integer.parseInt(lpasosReceta);

            String text = "Saludos";
            int totalPasos = numPasos+2;

            switch (position){
                case 0:
                    //text = laReceta.getLarga();
                    //ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    return new DescripcionReceta();


                case 1:
                    return new ActivarAyudante();


                default:
                    if(position == totalPasos){
                        MainApplication.getInstance().uptadteScore(10);
                        MainApplication.getInstance().uptadteScore(10);
                        return new CompleteReceta();
                    }else{
                        JSONArray pasos = MainApplication.getInstance().losPasos;
                        try {
                            JSONObject paso = pasos.getJSONObject(position-3);
                            text = paso.getString("paso");
                            if(laVieja) {
                                ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                            }


                        } catch (JSONException e) {

                        }
                        return PasoFragment.newInstance(position-2);
                    }

            }//end case

        }//end getitem

        @Override
        public int getCount() {
            Intent intent = getIntent();
            String lpasosReceta = intent.getStringExtra("pasos");
            //Log.i("FUCKING DEBUG", "los pasos: "+ lpasosReceta);
            int result = Integer.parseInt(lpasosReceta);
            return result+3;
        }
    }

    public void cerrar(View view){
        view.startAnimation(animScaleRectangular);
        this.finish();

    }
}
