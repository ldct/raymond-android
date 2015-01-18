package com.pennapps.raymond;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import javax.sql.DataSource;


public class Gallery extends ListActivity {

    private String type; //category
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        type = getIntent().getStringExtra("Category");
        ((TextView)findViewById(R.id.heading)).setText("Library of " + type);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(type.equals("Receipt")) {
            ArrayList<Receipt> receipts = (new Receipt(null, this)).getCompleteData();
            setListAdapter(new CustomAdapter(this, receipts, R.layout.receipt));
        }
        else if(type.equals("Event")) {
            ArrayList<Event> receipts = (new Event(null, this)).getCompleteData();
            setListAdapter(new CustomAdapter(this, receipts, R.layout.events));
        }
        else if(type.equals("Nutrition")) {
            ArrayList<Nutrition> receipts = (new Nutrition(null, this)).getCompleteData();
            setListAdapter(new CustomAdapter(this, receipts, R.layout.nutrition));
        }
        else if(type.equals("BusinessCard")) {
            ArrayList<BusinessCard> receipts = (new BusinessCard(null, this)).getCompleteData();
            setListAdapter(new CustomAdapter(this, receipts, R.layout.bcard));
        }
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new ModeCallback());
        registerForContextMenu(getListView());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item =((DataStructure)getListAdapter().getItem(position)).getField(0);
        Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refresh(View view){

        DataStructure ds = new Receipt(null, this);

        if (type.equals("Receipt")) {
            ds = new Receipt(null, this);
        } else if (type.equals("BusinessCard")) {
            ds = new BusinessCard(null, this);
        } else if (type.equals("Nutrition")) {
            ds = new Nutrition(null, this);
        } else if (type.equals("Event")) {
            ds = new Event(null, this);
        } else {
            Log.e("gallery", "unknown type");
        }

        ArrayList<String> tokens = ds.getAllTokens();

        Intent refreshDataIntent = new Intent(this, RefreshStatusService.class);
        refreshDataIntent.putExtra(RefreshStatusService.PARAM_TOKENS, tokens);

        startService(refreshDataIntent);

        Log.d("refresh", "done with starting service");


    }
    private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_gallery, menu);
            mode.setTitle("Select Items");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Toast.makeText(getApplicationContext(), "Shared " + getListView().getCheckedItemCount() + " items", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Clicked " + item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            final int checkedCount = getListView().getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }

    }
}
