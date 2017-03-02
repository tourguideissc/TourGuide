package com.example.rajeev.tourguide;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    ImageView tg_logoImageView;
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
        setContentView(R.layout.activity_splash_screen);
        tg_logoImageView = (ImageView) findViewById(R.id.tg_logo);
        animate();
    }

    private void animate()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(USERLOGINSTATUS , MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPreferences.edit();
                String Status = sharedPreferences.getString(STATUS,"");
                if(Status.equals("LoggedIn"))
                {
                    String Username = sharedPreferences.getString(USERNAME,"");
                    //Intent intent = new Intent(getBaseContext(), UserProfileActivity.class);
                    //Intent intent = new Intent(getBaseContext(), UsersPageActivity.class);
                    Intent intent = new Intent(getBaseContext(),UserMainScreen.class);
                    intent.putExtra("username",Username);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), SignInScreen.class);
                    startActivity(intent);
                }
                finish();

                //Intent intent = new Intent(SplashScreen.this,SignInScreen.class);
                //startActivity(intent);
            }
        },2000);
    }
}
