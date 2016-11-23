package com.superkeepaway;

import android.graphics.Bitmap;

/**
 * Created by joshh on 7/14/2016.
 */
public class GraphicTools {



    public static boolean graphicSelected (Bitmap map, float xCurrent, float yCurrent, float xEvent, float yEvent){


        if (xEvent > xCurrent
                && xEvent < (xCurrent +  map.getWidth())
                && yEvent > yCurrent
                && yEvent < (yCurrent + map.getHeight())){
            return true;
        }else {
            return false;
        }



    }
}
