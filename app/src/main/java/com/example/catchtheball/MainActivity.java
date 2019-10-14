package com.example.catchtheball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView totorobox;
    private ImageView acorn;
    private ImageView mushroom;
    private ImageView sootsprite;

    //size
    private  int frameHeight;
    private int boxSize;
    private  int screenWidth;
    private  int screenHeight;

    //position
    private int boxY;
    private int acornX;
    private int acornY;
    private int mushroomX;
    private int mushroomY;
    private int sootX;
    private int sootY;

    private int score = 0;


    //initialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //status check
    private boolean action_flg = false;
    private boolean start_flg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        totorobox = (ImageView) findViewById(R.id.totoroBox);
        acorn = (ImageView) findViewById(R.id.acorn);
        mushroom = (ImageView) findViewById(R.id.mushroom);
        sootsprite = (ImageView) findViewById(R.id.sootsprite);


        //get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;


        //moves them out of the screen
        acorn.setX(-80);
        acorn.setY(-80);
        mushroom.setX(-80);
        mushroom.setY(-80);
        sootsprite.setX(-80);
        sootsprite.setY(-80);

        scoreLabel.setText("Score : 0");

    }


    public void changePos() {

        hitCheck();

        //acorn
        acornX -= 12;
        if (acornX < 0) {
            acornX = screenWidth + 20;
            acornY = (int) Math.floor(Math.random() * (frameHeight = acorn.getHeight()));
        }
        acorn.setX(acornX);
        acorn.setY(acornY);

        //sootsprite
        sootX -= 16;
        if (sootX < 0) {
            sootX = screenWidth + 10;
            sootY = (int) Math.floor(Math.random() * (frameHeight = sootsprite.getHeight()));
        }
        sootsprite.setX(sootX);
        sootsprite.setY(sootY);

        //mushroom
        mushroomX -= 20;
        if (mushroomX < 0) {
            mushroomX = screenWidth + 5000;
            mushroomY = (int) Math.floor(Math.random() * (frameHeight = mushroom.getHeight()));
        }
        mushroom.setX(mushroomX);
        mushroom.setY(mushroomY);



        //MoveBox
        if (action_flg == true) {
            // Touching
            boxY -= 20;
        } else {
            // Releasing
            boxY  +=20;
        }

        // Check box position.
        if (boxY < 0) boxY = 0;

        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        totorobox.setY(boxY);

        scoreLabel.setText("Score : " + score);

    }

    public void hitCheck() {
       //if the ball gets into the center of totoro it counts as a hit

       //acorn
       int acornCenterX = acornX + acorn.getWidth() / 2;
       int acornCenterY = acornY + acorn.getHeight() / 2;

       if (0 <= acornCenterX && acornCenterX <= boxSize &&
            boxY <= acornCenterY && acornCenterY <= boxY + boxSize) {

           score += 10;
           acornX = -10;

       }

        //mushroom
        int mushroomCenterX = mushroomX + mushroom.getWidth() / 2;
        int mushroomCenterY = mushroomY + mushroom.getHeight() / 2;

        if (0 <= mushroomCenterX && mushroomCenterX <= boxSize &&
                boxY <= mushroomCenterY && mushroomCenterY <= boxY + boxSize) {

            score += 30;
            mushroomX = -10;

        }

        //sootsprite
        int sootCenterX = sootX + sootsprite.getWidth() / 2;
        int sootCenterY = sootY + sootsprite.getHeight() / 2;

        if (0 <= sootCenterX && sootCenterX <= boxSize &&
                boxY <= sootCenterY && sootCenterY <= boxY + boxSize) {

            //stop the timer
            timer.cancel();
            timer = null;


            //show results
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }

    }

    public boolean onTouchEvent(MotionEvent me) {

        if (start_flg == false) {

            start_flg = true;

            // Why get frame height and box height here?
            // Because the UI has not been set on the screen in OnCreate()!!

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int)totorobox.getY();

            // The box is a square.(height and width are the same.)
            boxSize = totorobox.getHeight();


            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);


        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }

        return true;
    }

    @Override
    public  boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

}
