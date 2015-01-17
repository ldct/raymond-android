package com.pennapps.raymond;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class CompletedActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        String results = getIntent().getStringExtra("File Name");
        if(results.equals("Error")){
            ((TextView)findViewById(R.id.titletext)).setText("Opps");
            ((TextView)findViewById(R.id.displayText)).setText("Something failed back there, please try again!");
        }else{
            ((TextView)findViewById(R.id.titletext)).setText("Success");
            ((TextView)findViewById(R.id.displayText)).setText("Feel free to add more, your library will be updated soon");

        }
        //((TextView)findViewById(R.id.displayText)).setText(getIntent().getStringExtra("File Name"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_completed, menu);
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
}
