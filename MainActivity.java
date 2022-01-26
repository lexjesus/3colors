package com.example.cimbal_3colors;


import static java.lang.Math.*;
import static java.util.Collections.sort;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawView drawView;
    Canvas canvas = new Canvas();
    Circle c1 = new Circle(600, 400, new int[]{255, 0, 0});
    Circle c2 = new Circle(800, 400, new int[]{0, 255, 0});
    Circle c3 = new Circle(600, 500, new int[]{0, 0, 255});
    ArrayList<Circle> circles = new ArrayList<Circle>(3);
    //Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
    //        .getDefaultDisplay();


    //int height = display.getWidth();
    //int width = display.getHeight();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout rel = (ConstraintLayout) findViewById(R.id.drawCont);
        drawView = new DrawView(this);
        rel.addView(drawView);
        circles.add(c1);
        c1.isSelected = true;
        circles.add(c2);
        circles.add(c3);
        Button b = (Button)findViewById(R.id.switch1);
        b.setBackgroundColor(Color.rgb(c1.color[0], c1.color[1], c1.color[2]));
    }

    class DrawView extends View {
        Paint p1 = new Paint();
        Paint p2 = new Paint();
        Paint p3 = new Paint();
        ArrayList<Paint> ps;
        public void paintReset(){
            p1.setColor(Color.rgb(c1.getColor(0), c1.getColor(1),c1.getColor(2)));
            p2.setColor(Color.rgb(c2.getColor(0), c2.getColor(1),c2.getColor(2)));
            p3.setColor(Color.rgb(c3.getColor(0), c3.getColor(1),c3.getColor(2)));
        }

        public DrawView(Context context) {
            super(context);
            ps = new ArrayList<>();
            ps.add(p1);
            ps.add(p2);
            ps.add(p3);
        }

        //@Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paintReset();
            canvas.drawRGB(0, 0, 0);
            for(int i = 0; i < 3; i++){
                for(int y = circles.get(i).center.y - circles.get(i).radius;
                    y <= circles.get(i).center.y + circles.get(i).radius; y++){
                    ArrayList<Integer> points = new ArrayList<>();
                    int x = circles.get(i).getXOnY(y);
                    int x1 = circles.get(i).center.x - x;
                    int x2 = circles.get(i).center.x + x;
                    points.add(x1);
                    for(int j = 0; j < 3; j++){
                        if(i==j){continue;}
                        int xi = circles.get(j).getXOnY(y);
                        int xi1 = circles.get(j).center.x - xi;
                        int xi2 = circles.get(j).center.x + xi;
                        if(x1 <= xi1 && xi1 <= x2 ){
                            points.add(xi1);
                        }
                        if(xi2 <= x2 && xi2 >= x1){
                            points.add(xi2);
                        }
                    }
                    points.add(x2);
                    sort(points);
                    int[] color;
                    for (int k = 0; k < points.size() - 1; k++){
                        color = circles.get(i).color.clone();
                        for (int f = 0; f < 3; f++){
                            if(f==i){continue;}
                            if(round(pow(((points.get(k) + 1) - circles.get(f).center.x), 2)
                                    + pow((y - circles.get(f).center.y), 2)) <= pow(circles.get(f).radius, 2)){
                                color[0] = min(color[0] + circles.get(f).color[0], 255);
                                color[1] = min(color[1] + circles.get(f).color[1], 255);
                                color[2] = min(color[2] + circles.get(f).color[2], 255);
                            }
                        }
                        ps.get(i).setColor(Color.rgb(color[0], color[1], color[2]));
                        canvas.drawLine(points.get(k), y, points.get(k + 1), y, ps.get(i));
                    }
                }
            }
            invalidate();
        }
    }


    public void switchCircle(View view){
        Button btn = (Button)findViewById(R.id.switch1);
        for(int i = 0; i < 3; i ++){
            if(circles.get(i).isSelected){
                circles.get(i).changeSel();
                if(i == 2){
                    circles.get(0).changeSel();
                    btn.setBackgroundColor(Color.rgb(circles.get(0).color[0],
                            circles.get(0).color[1], circles.get(0).color[2]));
                }
                else{
                    circles.get(i + 1).changeSel();
                    btn.setBackgroundColor(Color.rgb(circles.get(i + 1).color[0],
                            circles.get(i + 1).color[1], circles.get(i + 1).color[2]));
                }
                break;
            }
        }
    }

    @SuppressLint("WrongCall")
    public void down(View view) {
        Circle c = null;
        for(int i = 0; i < 3; i++){
            if(circles.get(i).isSelected){
                c = circles.get(i);
                break;
            }
        }
        if(c.center.y >= 2000 - 15){
            c.center.y = 2000;
        }
        else if(c.center.y < 2000 - 15) {
            c.center.y += 15;
        }
        drawView.onDraw(canvas);
    }

    @SuppressLint("WrongCall")
    public void up(View view){
        Circle c = null;
        for(int i = 0; i < 3; i++){
            if(circles.get(i).isSelected){
                c = circles.get(i);
                break;
            }
        }
        if(c.center.y <= 205 + 15){
            c.center.y = 205;
        }
        else if(c.center.y > 205) {
            c.center.y -= 15;
        }
        drawView.onDraw(canvas);
    }

    @SuppressLint("WrongCall")
    public void left(View view){
        Circle c = null;
        for(int i = 0; i < 3; i++){
            if(circles.get(i).isSelected){
                c = circles.get(i);
                break;
            }
        }
        if(c.center.x <= 15){
            c.center.x = 0;
        }
        else if(c.center.x > 15) {
            c.center.x -= 15;
        }
        drawView.onDraw(canvas);
    }

    @SuppressLint("WrongCall")
    public void right(View view){
        Circle c = null;
        for(int i = 0; i < 3; i++){
            if(circles.get(i).isSelected){
                c = circles.get(i);
                break;
            }
        }
        if(c.center.x >= 1024 - 15){
            c.center.x = 1024;
        }
        else if(c.center.x < 1024 - 15) {
            c.center.x += 15;
        }
        drawView.onDraw(canvas);
    }
}

