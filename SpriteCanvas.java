package com.superkeepaway;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class SpriteCanvas extends Activity {

    GameView gv;
    Paint drawPaint = new Paint();
    Paint recPaint = new Paint();
    int backGroundColor = Color.BLACK;

    //Polcemen graphics
    Bitmap policeOne;
    Bitmap policeTwo;

    //Controllable Graphic Variables
    Bitmap controlledGraphic;
    float xControl = 600, yControl = 1500;
    boolean imageSelected = true;
    Rect controlledRect;

    //Background graphic bitmap stuffs
    Bitmap fixedGraphic;
    Bitmap backgroundImage;

    //Initialize display variable to grab screen sizes
    Display display;
    int screenX, screenY;

    //Lost game variables
    boolean gameOver = false;
    boolean restart = false;
    boolean newHighScore = false;

    //Score Variables
    int score = 0;
    int highScore = 0;


    //Policemen speed + rectangles
    int graphic1X = 100, graphic1Y = 100, graphic1XSpeed = 14, graphic1YSpeed = 14;
    int graphic2X = 200, graphic2Y = 500, graphic2XSpeed = 10, graphic2YSpeed = 10;


    //Intialize X, Y and speed of "random" graphics
    int amb1X, amb1Y, amb1XSpeed = 8, amb1Yspeed = 8;
    int amb2X, amb2Y, amb2XSpeed = 11, amb2Yspeed = 11;
    int amb3X, amb3Y, amb3XSpeed = 12, amb3Yspeed = 12;
    int amb4X, amb4Y, amb4XSpeed = 13, amb4Yspeed = 13;



    Rect graphic1Rect, graphic2Rect, amb1Rect, amb2Rect, amb3Rect, amb4Rectt;

    //Additional graphics, randomly added via timertasks or other triggers
    Bitmap ambulanceCar;
    Bitmap bomb;

    ArrayList<RandomPictureModel> newGraphics = new ArrayList<RandomPictureModel>();
    ArrayList<Rect> newRects = new ArrayList<Rect>();
    Timer randomGraphicTimer = new Timer();

    //Score intervals in which additional pictures begin to spawn.
    int interval1 = 500;
    int interval2 = 1000;
    int interval3 = 1500;
    int interval4 = 2200;


    //Loser sound stuff
    SoundPool loserSoundPool;
    int loserSound;

    boolean test1 = false;
    boolean test2 = false;
    boolean test3 = false;
    boolean test4 = false;

    //Stuff for persistence
    String fileName = "persist";
    String difficultFileName = "difficulty";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);



        int difficulty = getDifficultyState();

        setDifficultyState(difficulty);


        gv = new GameView(this);
        setContentView(gv);

        policeOne = BitmapFactory.decodeResource(getResources(), R.drawable.cop_alpha);
        policeTwo = BitmapFactory.decodeResource(getResources(), R.drawable.cop_alpha);

        //Find the display size for all phones, prepare to scale to screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenX = size.x;
        screenY = size.y;

        //The controllable graphic man
        controlledGraphic = BitmapFactory.decodeResource((getResources()),R.drawable.finalrunning_man);

        //Game board background
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.streetbackground);
        //Scale the Bitmap to the screen size.
        fixedGraphic = Bitmap.createScaledBitmap(backgroundImage, screenX, screenY, true);

        //Random picture graphics
        ambulanceCar = BitmapFactory.decodeResource(getResources(), R.drawable.ambulance_car);
        bomb = BitmapFactory.decodeResource(getResources(), R.drawable.bombtouse);

        //SoundPool stuffs
        loserSoundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        AssetManager assetManager = getAssets();

        try{
            AssetFileDescriptor descripter = assetManager.openFd("try_again.wav");
            loserSound = loserSoundPool.load(descripter, 1);
        }catch(IOException e)  {
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

    public void saveHighScore(){
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(String.valueOf(highScore ).getBytes());

            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed()

    {

        String currentHighScore = getObjectState();
        int cHS = Integer.parseInt(currentHighScore);


        if (score >= cHS) {
            highScore = cHS;
            saveHighScore();
        }

        super.onBackPressed();
    }


    public int getDifficultyState(){
        File myFile = getFileStreamPath(difficultFileName);

        if (myFile.exists()){

            try {
                BufferedReader in  = new BufferedReader(new FileReader(myFile));
                String isClickedOne = in.readLine();
                String isClickedTwo = in.readLine();
                String isClickedThree= in.readLine();

                in.close();

                if (isClickedOne.equals("true")){
                    return 1;
                } else if (isClickedTwo.equals("true")){
                    return 2;
                }else if (isClickedThree.equals("true")){
                    return 3;
                }
                return 0;

            }catch (IOException e){
                e.printStackTrace();
            }

        }
        return 0;


    }

    public void setDifficultyState(int difficulty){

        if (difficulty == 0){

            //if something goes wrong, difficulty is normal. Do nothing

        }else if (difficulty == 1){

            //if difficulty is "easy", slow initial guys down
            graphic1XSpeed = 6;
            graphic1YSpeed = 6;
            graphic2XSpeed = 9;
            graphic2XSpeed = 9;


        }else if (difficulty == 2){

            //if difficulty is normal, do nothing upon creation. Everything is default

            graphic1XSpeed = 14;
            graphic1YSpeed = 14;

            graphic2XSpeed = 10;
            graphic2YSpeed = 10;


        }else if (difficulty == 3){

            //if difficulty is hard, speed up policement
            graphic1XSpeed = 20;
            graphic1YSpeed = 20;

            graphic2XSpeed = 24;
            graphic2YSpeed = 24;

        }



    }




    public boolean updateHighScore(){


        String currentHS = getObjectState();
        int cHS = Integer.parseInt(currentHS);

        if (score >= cHS){
            highScore = score;
            saveHighScore();
            return true;
        }else {
            return false;
        }

    }

    public boolean onTouchEvent (MotionEvent event){

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN :

            {
                float check1X = event.getRawX();
                float check1Y = event.getRawY();

                imageSelected = GraphicTools.graphicSelected(controlledGraphic, xControl, yControl, check1X, check1Y);


                if (restart == true){

                    //User has clicked restart so the game restarts (game over is false) and we reset the scoreboard. Restart is
                    //now false as well.
                    loserSoundPool.play(loserSound, 1, 1, 0, 0, 1);

                    float xControl = 600, yControl = 1500;
                    score = 0;

                    restart = false;
                    gameOver = false;


                    newGraphics.clear();
                }



            }
            break;

            case MotionEvent.ACTION_MOVE:

            {

                if (imageSelected){
                    xControl = event.getRawX() - (controlledGraphic.getWidth() );
                    yControl = event.getRawY() - (controlledGraphic.getHeight() );
                }


            }
            break;


            case MotionEvent.ACTION_UP :
            {
                imageSelected = false;
            }

            break;

        }

        return true;
    }





    @Override
    protected void onPause(){
        super.onPause();
        gv.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gv.resume();

    }



    public class GameView extends SurfaceView implements Runnable{
        Thread ViewThread = null;
        boolean threadOK = true;
        SurfaceHolder holder;


        public GameView(Context context) {
            super(context);

            holder = this.getHolder();
        }

        @Override
        public void run() {

            while (threadOK == true){
                if (!holder.getSurface().isValid()){
                    continue;
                }

                Canvas gameCanvas = holder.lockCanvas();

                //Create the rectangles for the police officers and the controlled object
                graphic1Rect = new Rect(graphic1X, graphic1Y, graphic1X + policeOne.getWidth(), graphic1Y + policeOne.getHeight());
                graphic2Rect = new Rect(graphic2X, graphic2Y, graphic2X + policeTwo.getWidth(), graphic2Y + policeTwo.getHeight());
                controlledRect = new Rect ((int) xControl , (int) yControl, (int) (xControl + controlledGraphic.getWidth()), (int)(yControl + controlledGraphic.getHeight()));


                //If the timed graphics are populated in the array, create their rectangles as well
                if (!newGraphics.isEmpty()){

                    for (int i = 0; i < newGraphics.size(); i ++){

                        switch (i){
                            case 0:
                                amb1Rect = new Rect(newGraphics.get(i).getX(), newGraphics.get(i).getY(), newGraphics.get(i).getX() + newGraphics.get(i).getRandomPicture().getWidth(),newGraphics.get(i).getY() + newGraphics.get(i).getRandomPicture().getHeight());
                                newGraphics.get(i).setRect(amb1Rect);
                                break;
                            case 1:
                                amb2Rect = new Rect(newGraphics.get(i).getX(), newGraphics.get(i).getY(), newGraphics.get(i).getX() + newGraphics.get(i).getRandomPicture().getWidth(),newGraphics.get(i).getY() + newGraphics.get(i).getRandomPicture().getHeight());
                                newGraphics.get(i).setRect(amb2Rect);
                                break;
                            case 2:
                                amb3Rect = new Rect(newGraphics.get(i).getX(), newGraphics.get(i).getY(), newGraphics.get(i).getX() + newGraphics.get(i).getRandomPicture().getWidth(),newGraphics.get(i).getY() + newGraphics.get(i).getRandomPicture().getHeight());
                                newGraphics.get(i).setRect(amb3Rect);
                                break;
                            case 3:
                                amb4Rectt = new Rect(newGraphics.get(i).getX(), newGraphics.get(i).getY(), newGraphics.get(i).getX() + newGraphics.get(i).getRandomPicture().getWidth(),newGraphics.get(i).getY() + newGraphics.get(i).getRandomPicture().getHeight());
                                newGraphics.get(i).setRect(amb4Rectt);
                                break;
                        }

                    }

                }


                myOnDraw(gameCanvas);

                holder.unlockCanvasAndPost(gameCanvas);

            }

        }



        public void newPictureAdd(int x, int y, int place, Canvas canvas){
            Random newRandom = new Random();
            int n = newRandom.nextInt(canvas.getWidth());
            int n2 = newRandom.nextInt(canvas.getHeight());

            //Speeds
            int speedX = 0;
            int speedY = 0;

            //Picture to use! Always render ambulance car so no crashing, just in case
            Bitmap pictureToUse = ambulanceCar;

            switch (place){
                case 1:
                    amb1X = n;
                    amb1Y = n2;
                    speedX = amb1XSpeed;
                    speedY = amb1Yspeed;
                    pictureToUse = ambulanceCar;
                    break;
                case 2:
                    amb2X = n;
                    amb2Y = n2;
                    speedX = amb2XSpeed;
                    speedY = amb2Yspeed;
                    pictureToUse = ambulanceCar;
                    break;
                case 3:
                    amb3X = n;
                    amb3Y = n2;
                    speedX = amb2XSpeed;
                    speedY = amb2Yspeed;
                    pictureToUse = bomb;
                    break;
                case 4:
                    amb4X = n;
                    amb4Y = n2;
                    speedX = amb3XSpeed;
                    speedY = amb3Yspeed;
                    pictureToUse = ambulanceCar;
                    break;
            }

            RandomPictureModel newPicture = new RandomPictureModel(n, n2, speedX, speedY, pictureToUse);
            newGraphics.add(newPicture);
        }


        protected void myOnDraw(Canvas canvas){

            //draw paint setup as well as place background graphic
            drawPaint.setAlpha(255);
            canvas.drawBitmap(fixedGraphic, 0, 0, null);

            //Draw the initial patrolling policemen
            canvas.drawBitmap(policeOne, graphic1X, graphic1Y, drawPaint);
            canvas.drawBitmap(policeTwo, graphic2X, graphic2Y, drawPaint);

            //Draw the movable character, controlled by MotionEvent
            canvas.drawBitmap(controlledGraphic, xControl, yControl, drawPaint);

            //Run the game clock/score
            drawPaint.setColor(Color.WHITE);
            drawPaint.setTextSize(60);
            drawPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Scoreboard", canvas.getWidth()/10, canvas.getHeight() - (canvas.getHeight() / 12), drawPaint);
            drawPaint.setTypeface(Typeface.DEFAULT);
            canvas.drawText("Score:\t " + String.valueOf(score), canvas.getWidth() / 11, canvas.getHeight() - (canvas.getHeight() / 20), drawPaint);
          //  canvas.drawText("Level: \t " + "1", canvas.getWidth() / 10, canvas.getHeight() - (canvas.getHeight() / 25), drawPaint);

            //While the game is still playing, accumulate score. Score will be used to determine when the next picture
            //will be added to the screen rather than a timer task. That proved too difficult :)

            if (gameOver == false){
                score +=1;

                if (score == interval1){
                    newPictureAdd(amb1X, amb1Y, 1, canvas);

                } else if (score == interval2){
                    newPictureAdd(amb2X, amb2Y, 2, canvas);

                } else if (score == interval3){
                    newPictureAdd(amb3X, amb3Y, 3, canvas);

                }else if (score == interval4){
                    newPictureAdd(amb4X, amb4Y, 4, canvas);

                }

                // If my graphics array is not empty, post the graphics to the screen and update location for each as well

                if (!newGraphics.isEmpty()){

                    for (int i = 0; i < newGraphics.size(); i++){

                        //Draws the picture
                        canvas.drawBitmap(newGraphics.get(i).getRandomPicture(), newGraphics.get(i).getX(), newGraphics.get(i).getY(), drawPaint);

                        //update the graphic location by passing in the current x/y, loop #, plus canvas
                        updateNewGraphicLocation(newGraphics.get(i).getX(), newGraphics.get(i).getY(), i, canvas);


                    }

                }
            }




            //Check to see if the game has been lost
            if (gameOver == true){
                drawPaint.setTypeface(Typeface.DEFAULT);
                drawPaint.setColor(Color.WHITE);
                drawPaint.setTextSize(90);
                canvas.drawText("YOU LOST!", canvas.getWidth() / 3, canvas.getHeight() / 10, drawPaint);


                //Prepare to restart the game
                drawPaint.setTextSize(65);
                canvas.drawText("Touch anywhere to restart", (canvas.getWidth() / 6), canvas.getHeight() / 7, drawPaint);
                drawPaint.setTextSize(50);
                canvas.drawText("Hit back on your keyboard to go to the menu", canvas.getHeight() / 35, canvas.getHeight() / 5, drawPaint);
                restart = true;

                //check to see if new high score
                newHighScore = updateHighScore();

                if (newHighScore == true){

                    //canvas.drawText("NEW HIGH SCORE!!!!", 350, 500, drawPaint);

                }




            }

            drawPaint.setTextSize(100);


            //Check for all collisions. Game over if collision
            if (Rect.intersects(controlledRect, graphic1Rect)){
                gameOver = true;
            }

            if (Rect.intersects(controlledRect, graphic2Rect)){
                gameOver = true;
            }


            //If there are any random graphics in the array currently, lets grab their rectangles
            //and check to see if they intersect. Basically, further collision detection. Game over if collision
            //Always check for nulls just in case!

            if (!newGraphics.isEmpty()){

                for (int i = 0; i < newGraphics.size(); i ++){

                    if (newGraphics.get(i).getRect() !=  null){
                        if (Rect.intersects(controlledRect,newGraphics.get(i).getRect())){
                            gameOver = true;
                        }
                    }

                }


            }


            //Code from one of the lectures, kept as is non-refactored.

            graphic1X += graphic1XSpeed;
            graphic1Y += graphic1YSpeed;

            graphic2Y += graphic2YSpeed;
            graphic2X += graphic2XSpeed;

            if (graphic1X < 0 || graphic1X > canvas.getWidth()){
                graphic1XSpeed  = graphic1XSpeed * -1;
            }

            if (graphic1Y < 0 || graphic1Y > canvas.getHeight()){
                graphic1YSpeed = graphic1YSpeed * -1;
            }

            if (graphic2X < 0 || graphic2X > canvas.getWidth()){
                graphic2XSpeed  = graphic2XSpeed * -1;
            }

            if (graphic2Y < 0 || graphic2Y > canvas.getHeight()){
                graphic2YSpeed = graphic2YSpeed * -1;
            }
        }


        //pull in the X/Y coords as well as the "location" (only four potential spots), and the canvas to update
        //the graphical location of the image. This is built upon speed. I have the speed incrementing
        // faster for each image that's added...9,10,11,12, etc.

        public void updateNewGraphicLocation(int x, int y, int location, Canvas c){

            if(location == 0){

                //for the first graphic, we're only going to make it go left to right (horizontal)
                //If we encounter a wall, we want to go in the opposite direction (multiply by -1)

                if (newGraphics.get(location).getX() < 0 || newGraphics.get(location).getX() > c.getWidth()){
                    newGraphics.get(location).setxSpeed(newGraphics.get(location).getxSpeed() * -1);
                    newGraphics.get(location).setX(x += newGraphics.get(location).getxSpeed());
                }else {
                    newGraphics.get(location).setX(x += newGraphics.get(location).getxSpeed());
                }

                //Second graphic is similar path of first, but Y axis instead of X

            }else if (location == 1){
                if (newGraphics.get(location).getY() < 0 || newGraphics.get(location).getY() > c.getHeight()){
                    newGraphics.get(location).setySpeed(newGraphics.get(location).getySpeed() * -1);
                    newGraphics.get(location).setY(y += newGraphics.get(location).getySpeed());
                }else {
                    newGraphics.get(location).setY(y += newGraphics.get(location).getySpeed());
                }


            }else if (location == 2){

                //I've decided no updates for this graphic, this will be a stationary "bomb" that will only cause loss of the game
                //if you hit it via collision

            }else if (location == 3){


                if (newGraphics.get(location).getX() < 0 || newGraphics.get(location).getX() > c.getWidth()){




                    newGraphics.get(location).setxSpeed(newGraphics.get(location).getxSpeed() * -1);
                    newGraphics.get(location).setX(x += newGraphics.get(location).getxSpeed());
                }else {
                    newGraphics.get(location).setX(x += newGraphics.get(location).getxSpeed());
                }




            }

        }

        public void pause(){
            threadOK = false;

            while (true){
                try {
                    ViewThread.join();
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }

                break;
            }

            ViewThread = null;
        }


        public void resume(){
            threadOK = true;
            ViewThread = new Thread(this);
            ViewThread.start();;
        }
    }

}
