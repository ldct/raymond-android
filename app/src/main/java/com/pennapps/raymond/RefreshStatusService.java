package com.pennapps.raymond;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class RefreshStatusService extends IntentService {

    public static final String PARAM_TOKENS = "PARAM_TOKENS";

    private ArrayList<String> tokens;

    public RefreshStatusService() {
        super("RefreshStatusThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("refresh", "handleIntent");
        tokens = intent.getStringArrayListExtra(PARAM_TOKENS);
        Log.d("refresh", String.valueOf(tokens.size()));

        return;
    }
}
