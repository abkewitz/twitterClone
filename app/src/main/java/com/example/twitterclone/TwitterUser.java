package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUser extends AppCompatActivity  implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twtter_users);
        //Initialize the elements
        Log.i("TwitterUsersTag","onCreate initiated");
        listView = findViewById(R.id.twitterUsersListView); // identify the listview in the xml
        tUsers= new ArrayList<>(); // Create a new ArrayList of twitterUsers
        adapter=  new ArrayAdapter(this,android.R.layout.simple_list_item_checked,tUsers); //Link the adapter to this class,the desired layout nd the tUsers arrayList
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);
        Log.i("TwitterUsersTag","onCreate elements defined");
        FancyToast.makeText(this, "Welcome"+  ParseUser.getCurrentUser().getUsername(),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
        //Create a parse query
        try {
            Log.i("TwitterUsersTag","onCreate Pre-Parse query");
            ParseQuery<ParseUser> query = ParseUser.getQuery(); // create the query
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            Log.i("TwitterUsersTag","onCreate Parse query Pre-Find in BG");
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    Log.i("TwitterUsersTag","onCreate Query find in BG started");
                    int ItemsCount = objects.size();
                    int ItemsCounter = objects.size();
                    if (objects.size()>0 && e==null);{
                        for(ParseUser twitterUser:objects){
                            ItemsCounter = ItemsCounter-1;
                            Log.i("TwitterUsersTag", ItemsCounter+"/"+ItemsCount + ":"+ twitterUser.getUsername());
                            tUsers.add(twitterUser.getUsername());
                            Log.i("TwitterUsersTag","onCreate tUsers size:"+ tUsers.size());
                        }
                        Log.i("TwitterUsersTag","onCreate ListView receiving adapter with "+ tUsers.size()+" users'");
                        listView.setAdapter(adapter);
                        Log.i("TwitterUsersTag","onCreate ListView received adapter with "+ tUsers.size()+" users'");
                        Log.i("TwitterUsersTag","onCreate ListView size: "+ listView.getCount());
                        for (String twitterUser:tUsers){// Run on all tUsers
                            if (ParseUser.getCurrentUser().getList("fanOf")!=null){// make sure the list is not empty so it won't crash..
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)){//If the currently forLooped twitterUser is found in the list on the server..
                                    listView.setItemChecked(tUsers.indexOf(twitterUser),true);// Find that user's index in the tUsers list and set him in the listView as checked!
                                }
                            }
                        }

                    }
                }
            });
        } catch (Exception e){
            Log.i("TwitterUsersTag","onCreate Catch Ex of query "+ e.getMessage());

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        Log.i("TwitterUsersTag","onCreateOptionsMenu Menu inflated");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.sendTwitt){
               //GoTwitt  - Satrt Twitter Acivity
                Intent intent =new Intent(TwitterUser.this,SendTwitt.class);
                startActivity(intent);
            Log.i("TwitterUsersTag","Menu Selected "+ item.getItemId());
        } else if (item.getItemId()==R.id.logOutUser){
            Log.i("TwitterUsersTag","Menu Selected "+ item.getItemId());

            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i("TwitterUsersTag","LogOut via menu started");

                    if (e==null){
                        Log.i("TwitterUsersTag","LogOut via menu started - Create intent");
                        Intent intent = new Intent(TwitterUser.this,SignUp.class);
                        Log.i("TwitterUsersTag","LogOut via menu started - Created intent - Starting activity");
                        startActivity(intent);
                        Log.i("TwitterUsersTag","LogOut via menu started - SignUp Activity started");
                        finish();
                        Log.i("TwitterUsersTag","LogOut via menu started - Finished Twitter Activity");

                    }}
            });
        }
        Log.i("TwitterUsersTag","LogOut via menu started - Rturning the last return");

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final int i =position;
        final CheckedTextView checkedTextView= (CheckedTextView) view;
        Log.i("TwitterUsersTag", "Item clicked");
        if (checkedTextView.isChecked()){
            ParseUser.getCurrentUser().add("fanOf",tUsers.get(i));
            Log.i("TwitterUsersTag",tUsers.get(i) + " checked");
            FancyToast.makeText(TwitterUser.this,"Following "+ tUsers.get(i)+ "is now followed",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
        }else{
            Log.i("TwitterUsersTag",tUsers.get(i) + " Un-checked");
            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(i)); // First we remove the user that is unchecked!
            List currentUsrFanOfList= ParseUser.getCurrentUser().getList("fanOf");//Now we create a list containing all the users that are checked!
            ParseUser.getCurrentUser().remove("fanOf");// Now we remove !! the whole list of users the user is a fan of!
            ParseUser.getCurrentUser().put("fanOf",currentUsrFanOfList);// Only in order to re-add them back but without the unchecked user!
            Log.i("TwitterUsersTag",tUsers.get(i) + " Un-checking process is done");

            FancyToast.makeText(TwitterUser.this,"Un-following "+ tUsers.get(i) ,FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
        }
        try {
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        Log.i("TwitterUsersTag",tUsers.get(i) +" ["+ checkedTextView.isChecked() + "] followed on server - Saved in BG ");
                    } else{
                        Log.i("TwitterUsersTag","Failed to ["+ checkedTextView.isChecked() +"] follow "+ tUsers.get(i) + " on server");
                    }
                }
            });
        }catch (Exception e){
            Log.i("TwitterUsersTag","Failed to ["+ checkedTextView.isChecked() +"] follow "+ tUsers.get(i) + " on server");
            FancyToast.makeText(TwitterUser.this,"Failed to un/follow "+ tUsers.get(i) + " on server" +"/n"+e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
        }
    }
}
