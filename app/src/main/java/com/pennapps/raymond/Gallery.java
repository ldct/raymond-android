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
import android.widget.TextView;

import java.util.ArrayList;


public class Gallery extends ActionBarActivity {

    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        type = getIntent().getStringExtra("Category");
        ((TextView)findViewById(R.id.heading)).setText("Library of " + type);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LinearLayout ln = (LinearLayout)findViewById(R.id.chart);
        if(type.equals("Receipt")){
            ArrayList<Receipt> receipts = (new Receipt(null, this)).getCompleteData();
            if(receipts.size()>=0) {
                int size = receipts.get(0).getAllFields().length;
                LinearLayout header = new LinearLayout(this);
                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                header.setWeightSum(size);
                header.setBackgroundColor(getResources().getColor(R.color.background));
                header.setLayoutParams(LLParams);
                for(String s: receipts.get(0).getAllFields()){
                    TextView tv = new TextView(this);
                    tv.setTextColor(getResources().getColor(R.color.textdes));
                    tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                    tv.setText(s);
                    header.addView(tv);
                }
                ln.addView(header);
                for (Receipt data : receipts) {
                }
            }
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
            return rootView;
        }
    }
}
