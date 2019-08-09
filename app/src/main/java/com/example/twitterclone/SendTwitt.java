package com.example.twitterclone;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTwitt extends AppCompatActivity {
    EditText edtTxtSendTwitt;
    Button btnSendTwitt;
    private View myLoader;
    private TextView loaderText;
    private ListView tweetsListView;
    private ListAdapter listAdapter;
    private ArrayList arrayList;
    public void loadTwittsFunc(){
        tweetsListView= findViewById(R.id.twittList);
        final ArrayList<HashMap<String,String>> twittList=new ArrayList<>();
        final SimpleAdapter adapter=new SimpleAdapter(SendTwitt.this,twittList,android.R.layout.simple_list_item_2,
                new String[]{"tweetUserName","tweetValue"},new int[]{android.R.id.text1,android.R.id.text2});
        // Preloader elements..
        myLoader= findViewById(R.id.myLoader_SendTwitt);
        loaderText=findViewById(R.id.loaderText_SendTwitt);
        Log.i("SendTwitTAG",  "Loader defined");

        //show dialog..
        Log.i("SendTwitTAG",  "Loader showing");
        loaderText.setText("Loading tweets.. ");
        myLoader.setVisibility(View.VISIBLE);
        myLoader.setAlpha(0.0f);
        myLoader.bringToFront();
        myLoader.animate().alpha(1.0f).setDuration(2000);
        Log.i("SendTwitTAG",  "Loader should show");

        //Hide listView
        tweetsListView.setVisibility(View.GONE);
        try {
            ParseQuery<ParseObject> parseQuery= ParseQuery.getQuery("MyTweet");// This refers to the 'MyTweet' class that resides on the server!!
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));// this calls the data that is in the list that is in the field called "fanOf" where the "user" is the currentUser!
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size()>0 && e==null){
                        for (ParseObject tweetObject:objects){
                            HashMap<String,String>userTweet =new HashMap<>();
                            userTweet.put("tweetUserName",tweetObject.getString("user"));
                            userTweet.put("tweetValue",tweetObject.getString("tweet"));
                            twittList.add(userTweet);
                            Log.i("SendTwitTAG",  "TweetList accumuating data..");
                        }
                        Log.i("SendTwitTAG",  "TweetList accumulated data");
                        Log.i("SendTwitTAG",  "TweetList setting adapter");
                        tweetsListView.setAdapter(adapter);
                        Log.i("SendTwitTAG",  "TweetList adapter set");
                    }
                }
            });
        } catch (Exception e){
            Log.i("SendTwitTAG",  e.getMessage());
        }
        //hide preloader & show listView
        Log.i("SendTwitTAG",  "Loader - hiding");
        myLoader.animate().alpha(0.2f).setDuration(4000);
        //myLoader.setVisibility(View.GONE);
        tweetsListView.setVisibility(View.VISIBLE);
        Log.i("SendTwitTAG",  "Tweets List should show");
        Log.i("SendTwitTAG",  "Loader should be hidden");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_twitt);
        edtTxtSendTwitt = findViewById(R.id.edtTxtSendTwitt);
        btnSendTwitt = findViewById(R.id.btnSendTwitt);
        myLoader= findViewById(R.id.myLoader_SendTwitt  );
        loaderText=findViewById(R.id.loaderText_SendTwitt);
        loadTwittsFunc();
        btnSendTwitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edtTxtSendTwitt.getText().toString())|| TextUtils.isEmpty(edtTxtSendTwitt.getText().toString())) {
                    Log.i("SendTwitTAG",  "empty field");
                    return;
                }
                String myTwitt = edtTxtSendTwitt.getText().toString();
                ParseUser.getCurrentUser().put("twitt",myTwitt);
                ParseUser.getCurrentUser().put("username" ,ParseUser.getCurrentUser().getUsername());
                //show dialog..
                loaderText.setText("Twitting.. ");
                myLoader.setVisibility(View.VISIBLE);
                myLoader.setAlpha(0.0f);
                myLoader.bringToFront();
                myLoader.animate().alpha(1.0f).setDuration(2000);
                // save in BG
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(SendTwitt.this,"["+
                                    ParseUser.getCurrentUser().get("twitt").toString() +"] twitted!",FancyToast.LENGTH_LONG
                                    ,FancyToast.INFO,false).show();
                        }else {
                            FancyToast.makeText(SendTwitt.this,"["+
                                            ParseUser.getCurrentUser().get("twitt").toString() +"] twitted!",FancyToast.LENGTH_LONG
                                    ,FancyToast.ERROR,false).show();
                        }
                    }
                });
                //hide preloader
                myLoader.animate().alpha(0.0f).setDuration(1000);
                myLoader.setVisibility(View.GONE);

            }
        });
    }
}
