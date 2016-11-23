package com.superkeepaway;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by joshh on 7/15/2016.
 */
public class RandomPictureModel {

    Rect rect;

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    int x;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public void setRandomPicture(Bitmap randomPicture) {
        this.randomPicture = randomPicture;
    }

    int y;
    int xSpeed;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public Bitmap getRandomPicture() {
        return randomPicture;
    }

    int ySpeed;
    Bitmap randomPicture;


    public RandomPictureModel(int xLocation, int yLocation, int xSpeed, int ySpeed, Bitmap picture){

        x = xLocation;
        y = yLocation;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.randomPicture = picture;



    }


}
