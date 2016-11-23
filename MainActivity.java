package com.superkeepaway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.TimerTask;

public class MainActivity extends Activity {

    Button instructions;
    String fileName = "persist";
    TextView highScore;


    public static final int MENU_HOME = Menu.FIRST + 1;
    public static final int MENU_HIGH_SCORE = Menu.FIRST + 2;

    public final int SPLASH_SCREEN_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1594799765029799~2147682868");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        highScore = (TextView) this.findViewById(R.id.textView2);


        String HS = getObjectState();

        if (HS != null){

            if (HS.equals("0")){
                highScore.setText("Yall aint even played yet bro");
            } else {
                highScore.setText(String.valueOf(HS));
            }

        }else {
            init();

            highScore.setText("Welcome");
        }





    }

    public void startGame(View view){

        Intent i = new Intent(MainActivity.this, SpriteCanvas.class);
        startActivity(i);

    }

    public void settingsButton(View view){
        Intent i = new Intent(MainActivity.this, InstructionsDifficulty.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
    }


    @Override
    public void onResume(){

       String HS =  getObjectState();

        if (HS != null){

            if (HS.equals("0")){
                highScore.setText("Yall aint even played yet bro");
            } else {
                highScore.setText(String.valueOf(HS));
            }

        }




        super.onResume();

    }

    public void init(){

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(String.valueOf(0).getBytes());

            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }



    public String getObjectState(){

        File myFile = getFileStreamPath(fileName);

        if (myFile.exists()){
            try {
                BufferedReader in  = new BufferedReader(new FileReader(myFile));
                String highScore = in.readLine();

                if (highScore != null){
                    return highScore;
                } else {
                    return null;
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }



}
