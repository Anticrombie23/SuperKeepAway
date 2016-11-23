package com.superkeepaway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by joshh on 7/26/2016.
 */
public class InstructionsDifficulty extends Activity implements CompoundButton.OnCheckedChangeListener {

    public static final int MENU_HOME = Menu.FIRST + 1;
    public static final int MENU_HIGH_SCORE = Menu.FIRST + 2;

    String difficultFileName = "difficulty";

    RadioButton RB1, RB2, RB3;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_difficulty);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1594799765029799~2147682868");
        AdView mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        RB1 = (RadioButton) this.findViewById(R.id.radioButton);
        RB2 = (RadioButton) this.findViewById(R.id.radioButton2);
        RB3 = (RadioButton) this.findViewById(R.id.radioButton3);

        getObjectState();

        RB1.setOnCheckedChangeListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // menu.add(Menu.NONE, MENU_HOME, Menu.NONE, "Home Screen");
        menu.add(Menu.NONE, MENU_HIGH_SCORE, Menu.NONE, "Check High Score");

        return true;
    }

    public void getObjectState(){

        File myFile = getFileStreamPath(difficultFileName);

        if (myFile.exists()){

            try {
                BufferedReader in  = new BufferedReader(new FileReader(myFile));
                RB1.setChecked(Boolean.valueOf(in.readLine()));
                RB2.setChecked(Boolean.valueOf(in.readLine()));
                RB3.setChecked(Boolean.valueOf(in.readLine()));


//
//                String isClickedOne = in.readLine();
//                String isClickedTwo = in.readLine();
//                String isClickedThree= in.readLine();

                in.close();


            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }



    public void saveDifficulty(){
        FileOutputStream outputStream;
        String RB1String = String.valueOf(RB1.isChecked() + "\n");
        String RB2String = String.valueOf(RB2.isChecked() + "\n");
        String RB3String = String.valueOf(RB3.isChecked() + "\n");


        try {
            outputStream = openFileOutput(difficultFileName, Context.MODE_PRIVATE);

            outputStream.write(RB1String.getBytes());
            outputStream.write(RB2String.getBytes());
            outputStream.write(RB3String.getBytes());
            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        saveDifficulty();
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            settingSelected();
            return true;
        } else if (id == MENU_HOME){
            spriteSelected();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goBackk(View view){
        saveDifficulty();
        Intent i = new Intent(InstructionsDifficulty.this, MainActivity.class);
        startActivity(i);
    }

    public void spriteSelected(){
        Intent i = new Intent(InstructionsDifficulty.this, SpriteCanvas.class);
        startActivity(i);
    }

    public void settingSelected(){
        Intent i = new Intent(InstructionsDifficulty.this, InstructionsDifficulty.class);
        startActivity(i);


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        saveDifficulty();
    }
}
