package com.pennapps.raymond;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class ImageUploaderService extends IntentService {

    public static final String PARAM_IN_MSG = "in_message";
    public static final String BACKEND_BASE_URL = "http://104.131.66.104";
    public static final String BACKEND_TASK_URL = BACKEND_BASE_URL + "/task";

    public String inputImageFilePath;

    public ImageUploaderService() {
        super("ImageUploaderThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("image", "handleIntent started");
        inputImageFilePath = intent.getStringExtra(PARAM_IN_MSG);
        Log.d("image", inputImageFilePath);
        new UploadImageTask().execute();
        return;
    }

    public class UploadImageTask  extends AsyncTask<String, Integer , String> {
        public byte[] dataToServer;

        @Override
        protected String doInBackground(String... uri) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost post_request = new HttpPost(BACKEND_TASK_URL);
            post_request.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

            HttpResponse response;
            String responseString = "";


            Log.d("image", "http starting");

            try {

                InputStream inputStream = new FileInputStream(inputImageFilePath);
                byte[] bytes;
                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                try {
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bytes = output.toByteArray();

                HttpEntity entity = new ByteArrayEntity(bytes);
                post_request.setEntity(entity);

                response = httpclient.execute(post_request);

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
            Log.d("image", "rs: " + responseString);
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("image", "http done");
            // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();
        }


    }


}