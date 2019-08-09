package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity {
    private EditText  edtTextLEmail, edtTextLPass;
    private Button btnLLogin , btnLSignUp;
    private View myLLoader;
    private TextView loaderTextL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set header
        setTitle(R.string.LoginScreenHeader);
        // Elements..
        myLLoader = findViewById(R.id.myLLoader);
        loaderTextL=findViewById(R.id.loaderTextL);
        edtTextLEmail=findViewById(R.id.editTextLEmail);
        edtTextLPass=findViewById(R.id.edtTextLPass);
        btnLLogin= findViewById(R.id.btnLLogin);
        btnLSignUp=findViewById(R.id.btnLSignUp);
        if (ParseUser.getCurrentUser()!=null){
            //ParseUser.getCurrentUser().logOut();
        //    transToSocialMediaActivity();
        }
        // Login btn clicked
        btnLLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LoginTAG",  "Login btn clicked");
                if(TextUtils.isEmpty(edtTextLEmail.getText().toString())|| TextUtils.isEmpty(edtTextLPass.getText().toString())) {
                    Log.i("LoginTAG",  "empty fields");
                    return;
                }
                String myLEmail =  edtTextLEmail.getText().toString();
                String myLPass =  edtTextLPass.getText().toString();

                Log.i("LoginTAG",  "User & Pass: "+ myLEmail+":"+myLPass);

                if (myLEmail.length()<5 && myLPass.length()<5){
                FancyToast.makeText(Login.this , "Please fill all required data",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
                Log.i("LoginTAG",  "User & Pass not OK "+ myLEmail+":"+myLPass);
                    return;
                }
                Log.i("LoginTAG",  "creating Prealoader");
                loaderTextL.setText("Loading..");
                myLLoader.setVisibility(View.VISIBLE);
                myLLoader.setAlpha(0.0f);
                myLLoader.bringToFront();
                myLLoader.animate().alpha(1.0f).setDuration(1000);
                Log.i("LoginTAG",  "Prealoader  loaded");

                ParseUser.logInInBackground(myLEmail, myLPass, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        Log.i("LoginTAG",  "LoginInBG Started"  );

                        if (user!=null && e ==null){
                            Log.i("LoginTAG",  user.getEmail());
                            FancyToast.makeText(Login.this, "Welcome"+
                                    user.getUsername().toString(),
                                    FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                    transToTwitterUsersActivity();

                        } else {
                            Log.i("LoginTAG",  e.getMessage());
                            FancyToast.makeText(Login.this,
                                    e.getMessage(), FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
                        }
                        myLLoader.animate().alpha(0.0f).setDuration(1000);
                        myLLoader.setVisibility(View.GONE);
                    }
                });
            }
        });

        //SignUp btn clicked
        btnLSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });
    }
    public void transToTwitterUsersActivity(){
        Log.i("LoginTAG","transToTwitterUsersActivity started");
        Intent intent = new Intent(Login.this,TwitterUser.class);
        //intent.putExtra("UserName",ParseUser.getCurrentUser().get("FullName").toString());
        Log.i("LoginTAG","transToTwitterUsersActivity intent created moving to next activity");

        startActivity(intent);
        Log.i("LoginTAG","transToTwitterUsersActivity moved to next activity");

        finish();
        Log.i("LoginTAG","transToTwitterUsersActivity finished  login activity");

    }
    // Remove the keyboard
    public void rootLayoutTapped(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        try {
            if (inputMethodManager.getEnabledInputMethodList() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

}
