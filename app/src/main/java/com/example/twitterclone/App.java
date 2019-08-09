package com.example.twitterclone;

import android.app.Application;
import android.content.Context;
import android.view.View;

import com.parse.Parse;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
    }
    public static void showPreloader(Context context, View view, String  loaderID,  String PreLoadrText, int PreLoadTime_ms){


    }

}
