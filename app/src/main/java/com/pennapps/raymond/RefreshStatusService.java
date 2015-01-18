package com.pennapps.raymond;

import android.app.IntentService;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class RefreshStatusService extends IntentService {

    public static final String PARAM_TOKENS = "PARAM_TOKENS";
    public static final String PARAM_CATEGORY = "PARAM_CATEGORY";

    public static final String BACKEND_BASE_URL = "http://104.131.66.104";
    public static final String BACKEND_BATCH_TASKS_URL = BACKEND_BASE_URL + "/batch_tasks";

    private ArrayList<String> tokens;
    private String category;

    public RefreshStatusService() {
        super("RefreshStatusThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("refresh", "handleIntent");
        tokens = intent.getStringArrayListExtra(PARAM_TOKENS);
        category = intent.getStringExtra(PARAM_CATEGORY);

        for (String t : tokens) {
            Log.d("refresh", t);
        }

        new RefreshStatusTask().execute();
        return;
    }

    public class RefreshStatusTask extends AsyncTask<String, Integer, String> {

        @Override protected String doInBackground(String... uri) {

            String responseString = "";
            String delimitedString = TextUtils.join(",", tokens);
            Log.d("refresh", delimitedString);

            Log.d("refresh", "starting refresh http");

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet get_request = new HttpGet(BACKEND_BATCH_TASKS_URL + "?tokens=" + delimitedString);
            get_request.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

            HttpResponse response;

            try {
                response = httpclient.execute(get_request);

                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else {
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }

                Log.d("refresh", responseString);
                JSONObject jObject = new JSONObject(responseString);

                if (category.equals("Receipt")) {
                    for (Iterator<String> ks = jObject.keys(); ks.hasNext();) {
                        String token = ks.next();
                        JSONObject v = jObject.getJSONObject(token);

                        String date = v.getString("date");
                        String time = v.getString("time");
                        String location = v.getString("location");
                        JSONArray items = v.getJSONArray("items");

                        for (int i=0; i < v.length() - 1; i++) {
                            JSONObject item = items.getJSONObject(i);
                            String item_name = item.getString("item");
                            String price = item.getString("price");
                            Log.d("refresh", item_name);
                            Log.d("refresh", price);

                            Receipt r = new Receipt(new String[]{
                                    date, time, location, item_name, price
                            }, getApplicationContext());
                            r.addtoDB(token + String.valueOf(i), "inputImageFilePath");
                            r.addThis(token + String.valueOf(i));
                        }
                    }

                } else if (category.equals("BusinessCard")) {
                    for (Iterator<String> ks = jObject.keys(); ks.hasNext();) {
                        String token = ks.next();
                        JSONObject v = jObject.getJSONObject(token);

                        String name = v.getString("name");
                        String phone = v.getString("phone");
                        String email = v.getString("email");
                        String job = v.getString("job");
                        String company = v.getString("company");

                        BusinessCard b = new BusinessCard(new String[]{
                                name, email, phone, job, company
                        }, getApplicationContext());
                        b.addThis(token);
                    }
                } else if (category.equals("Nutrition")) {
                    for (Iterator<String> ks = jObject.keys(); ks.hasNext();) {
                        String token = ks.next();
                        JSONObject v = jObject.getJSONObject(token);

                        String name = v.getString("name");
                        String calories = v.getString("calories");
                        String fat = v.getString("fat");
                        String protein = v.getString("protein");
                        String carbs = v.getString("carbs");
                        String sodium = v.getString("sodium");
                        String sugar = v.getString("sugar");

                        Log.d("refresh", name + calories + fat + protein + carbs + sodium + sugar);

                        Nutrition n = new Nutrition(new String[]{
                                name, calories, fat, protein, carbs, sodium, sugar
                        }, getApplicationContext());
                        n.addThis(token);
                    }
                }

            } catch (Exception e) {
                Log.e("refresh", e.getMessage());
            }

            return "yo";
        }

        @Override protected void onPostExecute(String result) {

        }
    }
}
