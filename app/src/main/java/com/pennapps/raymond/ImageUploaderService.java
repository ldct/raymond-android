package com.pennapps.raymond;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUploaderService extends IntentService {

    public static final String PARAM_IN_MSG = "in_message";

    public ImageUploaderService() {
        super("ImageUploaderThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("image", "handleIntent started");
        Log.d("image", intent.getStringExtra(PARAM_IN_MSG));
        new UploadImageTask().execute("http://stackoverflow.com");
        return;
    }

    public class UploadImageTask  extends AsyncTask<String, Integer , String> {
        public byte[] dataToServer;

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet get_request = new HttpGet(uri[0]);
            get_request.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
            HttpResponse response;
            String responseString = null;

            Log.d("image", "http starting");

            try {
                response = httpclient.execute(get_request);

                Log.d("image", "response received");

                StatusLine statusLine = response.getStatusLine();
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
            Log.d("image", "http done");
            // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.d("image", String.valueOf(progress[0]));
        }


    }


}