package com.pennapps.raymond;

import android.content.Context;
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
        if(results.equals("Error")){
            ((TextView)findViewById(R.id.titletext)).setText("Opps");
            ((TextView)findViewById(R.id.displayText)).setText("Something failed back there, please try again!");
        }else{
            ((TextView)findViewById(R.id.titletext)).setText("Success");
            ((TextView)findViewById(R.id.displayText)).setText("Feel free to add more, your library will be updated soon");

        }

        new UploadImageTask().execute("http://stackoverflow.com");

        //getIntent().getStringExtra("File Name")
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

    public class UploadImageTask  extends AsyncTask<String, Integer , String> {
        public byte[] dataToServer;

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;

            Log.d("http", "starting");

            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                Log.d("http", "executed");
                StatusLine statusLine = response.getStatusLine();
                Log.d("http", String.valueOf(statusLine.getStatusCode()));
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else{
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                Log.d("http", "clientprotocolexception");
            } catch (IOException e) {
                Log.d("http", "ioexception");
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("http", "done");
            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
        }


    }


}
