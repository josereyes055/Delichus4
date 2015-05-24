package geekgames.delichus4.seconds;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.adapters.IngredienteAdapter;
import geekgames.delichus4.customObjects.Ingrediente;

public class ActivityShoppingList extends ActionBarActivity {

    private IngredienteAdapter mAdapter;
    int idUser;
    private String currentHeader = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_ingredientes);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new IngredienteAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list_shopping);
        listView.setAdapter(mAdapter);
        setTitle("Lista de compras");



        //fetch();
        List<Ingrediente> shoppingList = MainApplication.getInstance().shoppingList;

        mAdapter.swapRecords(shoppingList);
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
                onBackPressed();
                return true;


            /*case R.id.action_settings:
                return true;*/

        }

        return super.onOptionsItemSelected(item);
    }



}
