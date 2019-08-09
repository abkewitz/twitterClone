package com.example.twitterclone;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTwitt extends AppCompatActivity {
    EditText edtTxtSendTwitt;
    Button btnSendTwitt;
    private View myLoader;
    private TextView loaderText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_twitt);
        edtTxtSendTwitt = findViewById(R.id.edtTxtSendTwitt);
        btnSendTwitt = findViewById(R.id.btnSendTwitt);
        myLoader= findViewById(R.id.myLoader_SendTwitt);
            loaderText=findViewById(R.id.loaderText_SendTwitt);
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
