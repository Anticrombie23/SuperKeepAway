package com.superkeepaway;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by joshh on 7/28/2016.
 */
public class HighScore extends Activity {

    TextView highScore;

    int highScoreInt = 0;

    String fileName = "persist";

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);
        highScore = (TextView) this.findViewById(R.id.textView4);

        String HS = getObjectState();

        if (HS.equals("0")){
            highScore.setText("Yall aint even played yet bro");
        } else {
            highScore.setText(String.valueOf(HS));
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


    public void goBack(View view){
        Intent i = new Intent(HighScore.this, MainActivity.class);
        startActivity(i);
    }


}
