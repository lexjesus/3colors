package com.example.cimbal_3colors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import android.graphics.Color;

public class Circle {

    int radius = 200;
    MyPoint center;
    int[] color;
    boolean isSelected;

    public Circle(int x, int y, int[] color){
        center = new MyPoint(x, y);
        this.color = color;
    }

    public int getColor(int ind){
        return color[ind];
    }

    public int getXOnY(int y){
        return (int)sqrt(pow(radius, 2) - pow(y - center.y, 2));
    }

    public void changeSel(){
        if(isSelected){isSelected = false;}
        else {isSelected = true;}
    }

}
