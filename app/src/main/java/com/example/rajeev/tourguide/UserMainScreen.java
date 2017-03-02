package com.example.rajeev.tourguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class UserMainScreen extends AppCompatActivity {
    Snackbar snackbar;
    Intent getUserIntent,intent;
    String userName;
    MenuItem change_password,logout;

    //Variables for SharedPreferences
    SharedPreferences sharedPreferences;
    String USERLOGINSTATUS="UserLoginStatus";
    String USERNAME="username";
    String FIRSTNAME="firstname";
    String LASTNAME="lastname";
    String PASSWORD="password";
    String MOBILE="mobile";
    String EMAILID="emailid";
    String STATUS="status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_screen);
        getUserIntent = getIntent();
        userName = getUserIntent.getStringExtra("username");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id =item.getItemId();
        if(id == R.id.change_password)
        {
            snackbar = Snackbar.make(getWindow().getDecorView(),"Change Password", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
            intent = new Intent(getApplicationContext(),ChangePassword.class);
            intent.putExtra("username",userName);
            startActivity(intent);
        }
        else if(id == R.id.logout)
        {
            snackbar = Snackbar.make(getWindow().getDecorView(),"Logout", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
            sharedPreferences = getSharedPreferences(USERLOGINSTATUS , Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent1 = new Intent(getApplicationContext(),SignInScreen.class);
            startActivity(intent1);
            finish();
        }
        else if(id == R.id.help)
        {
            snackbar = Snackbar.make(getWindow().getDecorView(),"Help", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        }
        else if(id == R.id.view_profile)
        {
            intent = new Intent(getApplicationContext(),UserProfile.class);
            intent.putExtra("username",userName);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(userName.equals("null"))
        {
            change_password=menu.getItem(0);
            logout=menu.getItem(1);
            change_password.setVisible(false);
            logout.setVisible(false);
        }
        return true;
    }
}
