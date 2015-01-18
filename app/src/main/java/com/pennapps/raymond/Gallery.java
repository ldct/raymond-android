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
            setListAdapter(new CustomAdapter(this, receipts, R.layout.nutrition));
        }
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new ModeCallback());
        registerForContextMenu(getListView());

//        LinearLayout ln = (LinearLayout)findViewById(R.id.chart);
//        ArrayList receipts;
//        if(type.equals("Receipt")) {
//            receipts = (new Receipt(null, this)).getCompleteData();
//        }else if(type.equals("Event")) {
//            receipts = (new Event(null, this)).getCompleteData();
//        }
//        else if(type.equals("BusinessCard")) {
//            receipts = (new BusinessCard(null, this)).getCompleteData();
//        }else{
//            receipts = (new Nutrition(null, this)).getCompleteData();
//        }
//        if(receipts.size()>0) {
//            int size = ((DataStructure)receipts.get(0)).getAllFields().length+2;
//            LinearLayout header = new LinearLayout(this);
//            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//            header.setWeightSum(size);
//            header.setBackgroundColor(getResources().getColor(R.color.background));
//            header.setLayoutParams(LLParams);
//            for(String s: ((DataStructure)receipts.get(0)).getAllFields()){
//                TextView tv = new TextView(this);
//                tv.setTextColor(getResources().getColor(R.color.textdes));
//                tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
//                tv.setText(s);
//                header.addView(tv);
//            }
//            ln.addView(header);
//            for (DataStructure data : ((ArrayList<DataStructure>)receipts)) {
//                LinearLayout newRow = new LinearLayout(this);
//                newRow.setWeightSum(size);
//                newRow.setLayoutParams(LLParams);
//                newRow.setBackgroundColor(getResources().getColor(R.color.textdes));
//                for(int i =0; i < data.getAllFields().length+2; i++){
//                    TextView tv = new TextView(this);
//                    tv.setTextColor(getResources().getColor(R.color.text));
//                    tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
//                    tv.setText(data.getField(i));
//                    newRow.addView(tv);
//                }
//                ln.addView(newRow);
//            }
//        }else{
//            TextView tv = new TextView(this);
//            tv.setText("No content yet, add some to your library by taking some pictures!");
//            ln.addView(tv);
//        }
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
        //TODO
        if (type.equals("Receipt")) {
            Receipt rs = new Receipt(null, this);
            ArrayList<Receipt> completeData = rs.getCompleteData();
            Log.d("refresh", String.valueOf(completeData.size()));

            for (Receipt r : completeData) {
                // r.getToken();
            }

            ArrayList<String> tokenList = new ArrayList<String>();

            Intent refreshDataIntent = new Intent(this, RefreshStatusService.class);
            refreshDataIntent.putExtra(RefreshStatusService.PARAM_TOKENS, tokenList);

            startService(refreshDataIntent);

            Log.d("refresh", "done with starting service");

        }


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
