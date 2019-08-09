package com.example.twitterclone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity  {
    private EditText edtTextName , edtTextEmail, edtTextPass;
    private Button btnLogin , btnSignUp;
    private View myLoader;
    private TextView loaderText, passHintTxt,emailHintTxt ,nameHintTxt;
    private ImageView AppMainIcon;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Set header
        setTitle(R.string.SignUpScreenHeader);
        // Elements..
        myLoader = findViewById(R.id.myLoader);
        loaderText=findViewById(R.id.loaderText);
        edtTextEmail=findViewById(R.id.editTextEmail);
        edtTextName=findViewById(R.id.editTextName);
        edtTextPass=findViewById(R.id.edtTextPass);
        btnLogin= findViewById(R.id.btnSLogin);
        btnSignUp=findViewById(R.id.btnSSignUp);
        AppMainIcon=findViewById(R.id.AppMainIcon);
        passHintTxt = findViewById(R.id.passHintTxt);
        emailHintTxt= findViewById(R.id.emailHintTxt);
        nameHintTxt= findViewById(R.id.nameHintTxt);
        if (ParseUser.getCurrentUser()!=null){
            //ParseUser.logOut();
          //  transToSocialMediaActivity();
        }
        //hide image on tex field touch
        edtTextEmail.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    //edtTextName.setText("");
                    AppMainIcon.setVisibility(View.GONE);
                    emailHintTxt.setVisibility(View.GONE);
                }
                return false; // return is important...
            }
        });
        //hide image on tex field touch
        edtTextPass.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    //edtTextName.setText("");
                    AppMainIcon.setVisibility(View.GONE);
                    passHintTxt.setVisibility(View.GONE);
                }
                return false; // return is important...
            }
        });
        //hide image on tex field touch
        edtTextName.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                   //edtTextName.setText("");
                    AppMainIcon.setVisibility(View.GONE);
                    nameHintTxt.setVisibility(View.GONE);

                }
                 return false; // return is important...
            }
        });

        //SignUp
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppMainIcon.setVisibility(View.VISIBLE);
                rootLayoutTapped(getCurrentFocus());
                String  myEmail = edtTextEmail.getText().toString();
                String myName= edtTextName.getText().toString();
                String myPass=edtTextPass.getText().toString();
                boolean continueRunning = true;

                if (passVerify(myPass)==false){
                    FancyToast.makeText(SignUp.this , "Please fill all required data",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
                    passHintTxt.setVisibility(View.VISIBLE);
                    continueRunning= false;
                }
                if (myEmail.length()<5||!myEmail.contains("@")||!myEmail.contains(".")){
                    FancyToast.makeText(SignUp.this , "Please fill all required data",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
                    emailHintTxt.setVisibility(View.VISIBLE);
                    continueRunning= false;
                }
                if (myName.length()<4 || !myName.contains(" ")){
                    FancyToast.makeText(SignUp.this , "Please fill all required data",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
                    nameHintTxt.setVisibility(View.VISIBLE);
                    continueRunning= false;
                }
                if (!continueRunning){
                    return;
                }


                final ParseUser parseUser = new ParseUser();
                parseUser.setEmail(myEmail);
                parseUser.setPassword(myPass);
                parseUser.setUsername(myEmail);
                parseUser.put("FullName", myName);
                loaderText.setText("Registering "+parseUser.getUsername()+"..");
                myLoader.setVisibility(View.VISIBLE);
                myLoader.setAlpha(0.0f);
                myLoader.bringToFront();
                myLoader.animate().alpha(1.0f).setDuration(2000);
                rootLayoutTapped(getCurrentFocus());
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(SignUp.this, parseUser.getUsername()
                                            + " registered successfully",FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,false).show();
                            //transToSocialMediaActivity();

                        } else {
                            FancyToast.makeText(SignUp.this,e.getMessage()
                                    ,FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                        }
                        myLoader.animate().alpha(0.0f).setDuration(1000);
                        myLoader.setVisibility(View.GONE);
                        Intent intent = new Intent(SignUp.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        //Login btn clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,Login.class);
                startActivity(intent);
            }
        });
        // Enter key pressed when in password field
        edtTextPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    btnSignUp.callOnClick();
                }
                return false ;
            }
        });

    }
    // hide the con on cliking the text fields
    public void textFldOnClickFunction(View view){
        AppMainIcon.setVisibility(View.GONE);
        FancyToast.makeText(SignUp.this,view.getId()+"",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
        passHintTxt.setVisibility(View.GONE);
    }
    // Remove the keyboard
    public void rootLayoutTapped(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        try {
            if (inputMethodManager.getEnabledInputMethodList() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                AppMainIcon.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
    }
    public boolean passVerify(String str){
        boolean goodPass=false;
        boolean hasDigit = false;
        boolean hasUpper = false;
        boolean hasLower  = false;
        int myLen = str.length();
        for (int i =0; i < myLen; i++){
            if (Character.isLowerCase(str.charAt(i))){
                hasLower = true ;
            }
            if (Character.isUpperCase(str.charAt(i))){
                hasUpper = true ;
            }
            if (Character.isDigit(str.charAt(i))){
                hasDigit = true ;
            }
        }
        goodPass= hasDigit && hasLower && hasUpper;
        return goodPass;
    }
}

