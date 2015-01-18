package com.pennapps.raymond;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Gallery extends ActionBarActivity {

    private String type;
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
        ListView listView = (ListView)findViewById(R.id.list);

        if(type.equals("Receipt")) {
            ArrayList<Receipt> receipts = (new Receipt(null, this)).getCompleteData();
            listView.setAdapter(new CustomAdapter(this, receipts, R.layout.receipt));
        }
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

        BusinessCard bc = new BusinessCard(null,this);
        ArrayList<BusinessCard> completeData = bc.getCompleteData();

        //type is the category


    }
}
