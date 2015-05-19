package geekgames.delichus4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import geekgames.delichus4.adapters.PasoAdapter;
import geekgames.delichus4.customObjects.Ficha;
import geekgames.delichus4.customViews.CustomPager;
import geekgames.delichus4.fragments.ActivarAyudante;
import geekgames.delichus4.fragments.CompleteReceta;
import geekgames.delichus4.fragments.DescripcionReceta;
import geekgames.delichus4.fragments.PasoFragment;
import geekgames.delichus4.seconds.OtherUserPage;

public class Receta extends ActionBarActivity{

    private static final int CAMERA_IMAGE = 1;
    Ficha laReceta = MainApplication.getInstance().laReceta;
    Animation animScale;
    Animation animScaleSutile;
    Animation animScaleRectangular;

    SectionsPagerAdapter mSectionsPagerAdapter;
    PasoAdapter mAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receta);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale_button_animation);
        animScaleSutile = AnimationUtils.loadAnimation(this, R.anim.scale_button_sutile_animation);
        animScaleRectangular = AnimationUtils.loadAnimation(this, R.anim.scale_button_rectangular_animation);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setTitle(laReceta.nombre);

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


    public void shutUpVieja(View view){
        view.startAnimation(animScale);
        laVieja = false;
        ToggleButton tb = (ToggleButton)findViewById(R.id.viejaToggle);
        tb.setChecked(false);
    }

    public void letItBeVieja(View view){
        view.startAnimation(animScale);
        laVieja = true;
        ToggleButton tb = (ToggleButton)findViewById(R.id.viejaToggle);
        tb.setChecked(true);
    }

    public void toggleVieja(View view){
        laVieja = !laVieja;

        Drawable drawSpeak;
        if( laVieja ){
            drawSpeak = getResources().getDrawable(R.drawable.asistente);
        }else {
            drawSpeak = getResources().getDrawable(R.drawable.no_asistente);
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
        showButtons();
    }

    public void showButtons(){
        Drawable drawPrev = getResources().getDrawable(R.drawable.atrasar);
        Drawable drawPlay = getResources().getDrawable(R.drawable.play);
        Drawable drawNexr = getResources().getDrawable(R.drawable.adelantar);
        Drawable drawSpeak = getResources().getDrawable(R.drawable.asistente);

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

    public void recetaPlay(View view){
        view.startAnimation(animScale);
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
                //MainApplication.getInstance().shoppingList.add(laReceta.ingredientes.get(i));
            }
        }
        Toast.makeText(this, "Los ingredientes no disponibles se agregaron a la lista de compras", Toast.LENGTH_LONG).show();
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
            comenzar.setBackground(this.getResources().getDrawable(R.color.verde));
            comenzar.setEnabled(true);
        } else {
            selectAllButton.setBackground(this.getResources().getDrawable(R.color.verde));
            comenzar.setBackground(this.getResources().getDrawable(R.color.disable));
            comenzar.setEnabled(false);
        }
    }

    public void onCameraClick(View view){
        view.startAnimation(animScaleSutile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

    public void onShareRecipe(View view) {

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
                    .setContentTitle(this.laReceta.nombre)
                    .setContentDescription(this.laReceta.descripcion)
                    .setImageUrl(Uri.parse(this.laReceta.imagen))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    public void onTakePictureRecipe(View view){

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

        //startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            Bitmap bitMapImage = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitMapImage)
                    .build();

            SharePhotoContent photoContent = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            shareDialog.show(photoContent);
        }
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
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            String text = "Saludos";
            int totalPasos = laReceta.pasos+1;

            switch (position){
                case 0:
                    //text = laReceta.getLarga();
                    //ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    return new DescripcionReceta();


                case 1:
                    return new ActivarAyudante();

                default:
                    if(position == totalPasos){
                        //Log.i("FUCKING DEBUG", "se sumo el puntaje");
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
            return laReceta.pasos+2;
        }
    }




}
