package geekgames.delichus4.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import geekgames.delichus4.BusquedaIngredientes;
import geekgames.delichus4.MainApplication;
import geekgames.delichus4.R;
import geekgames.delichus4.seconds.ActivityLogros;
import geekgames.delichus4.seconds.OtherUserPage;

public class ListDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        LayoutInflater inflater = getActivity().getLayoutInflater();
        String stringArray = MainApplication.getInstance().sp.getString("ingrediente",null);
        final JSONObject ingredientes;
        final ArrayList<String> listItems = new ArrayList<String>();
        final ArrayList<String> listIndex = new ArrayList<String>();
        if( stringArray != null ){
            try {
                ingredientes = new JSONObject(stringArray);
                Iterator<String> keysIterator = ingredientes.keys();
                while (keysIterator.hasNext())
                {
                    String keyStr = (String)keysIterator.next();
                    String valueStr = ingredientes.getString(keyStr);
                    listItems.add(valueStr);
                    listIndex.add(keyStr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final CharSequence[] lalista = listItems.toArray(new CharSequence[listItems.size()]);


        builder.setTitle("seleccione")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(lalista, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        Intent mainIntent = new Intent().setClass(
                                getActivity(), BusquedaIngredientes.class);

                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ArrayList<String> nombres = new ArrayList<String>();
                        mainIntent.putIntegerArrayListExtra("seleccionados", mSelectedItems);
                        startActivity(mainIntent);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();
    }
}