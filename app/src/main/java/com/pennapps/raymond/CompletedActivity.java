package com.pennapps.raymond;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class CompletedActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        String results = getIntent().getStringExtra("File Name");
        if(results.equals("Error")) {
            ((TextView)findViewById(R.id.titletext)).setText("Opps");
            ((TextView)findViewById(R.id.displayText)).setText("Something failed back there, please try again!");
        }else {
            ((TextView)findViewById(R.id.titletext)).setText("Success");
            ((TextView)findViewById(R.id.displayText)).setText("Feel free to add more, your library will be updated soon");
        }

        Intent imageUploadIntent = new Intent(this, ImageUploaderService.class);
        imageUploadIntent.putExtra(ImageUploaderService.PARAM_IN_MSG, getIntent().getStringExtra("File Name"));
        startService(imageUploadIntent);
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
