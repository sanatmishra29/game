package com.android.colorgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.colorgame.databinding.ActivityMainBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    private static final String TAG = MainActivity.class.getSimpleName();

    Handler handler = new Handler();

    Runnable runnable;

    Timer t = new Timer();

    boolean timeRunning = false;

    String currentBox, red = "Red", blue = "Blue", green = "Green", yellow = "Yellow", timestamp;

    int delay = 1000, score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (SharedPrefs.getBoolean(getApplicationContext(), SharedPrefsKey.UserPlayed)){

            addOnColorTouch();

            startGame();

            afterOneSecond();

        }
        else{
            SharedPrefs.putBoolean(getApplicationContext(), SharedPrefsKey.UserPlayed, true);
            showGameInstruction();
        }

    }

    private void startGame(){

        handler.postDelayed( runnable = () -> {

            changeColorToGray();

            Log.e(TAG, "Start Game...");

            handler.postDelayed(runnable, delay);
        }, delay);

    }

    private void changeColorToGray(){

        Random random = new Random();

        int number = random.nextInt(5-1) + 1;

        if (number == 1){

            activityMainBinding.boxRed.setBackgroundColor(getResources().getColor(R.color.gray));
            activityMainBinding.boxBlue.setBackgroundColor(getResources().getColor(R.color.blue));
            activityMainBinding.boxYellow.setBackgroundColor(getResources().getColor(R.color.yellow));
            activityMainBinding.boxGreen.setBackgroundColor(getResources().getColor(R.color.green));

            currentBox = red;
        }

        else if(number == 2){

            activityMainBinding.boxBlue.setBackgroundColor(getResources().getColor(R.color.gray));
            activityMainBinding.boxRed.setBackgroundColor(getResources().getColor(R.color.red));
            activityMainBinding.boxYellow.setBackgroundColor(getResources().getColor(R.color.yellow));
            activityMainBinding.boxGreen.setBackgroundColor(getResources().getColor(R.color.green));

            currentBox = blue;
        }

        else if(number == 3){

            activityMainBinding.boxYellow.setBackgroundColor(getResources().getColor(R.color.gray));
            activityMainBinding.boxRed.setBackgroundColor(getResources().getColor(R.color.red));
            activityMainBinding.boxBlue.setBackgroundColor(getResources().getColor(R.color.blue));
            activityMainBinding.boxGreen.setBackgroundColor(getResources().getColor(R.color.green));

            currentBox = yellow;
        }

        else if(number == 4){

            activityMainBinding.boxGreen.setBackgroundColor(getResources().getColor(R.color.gray));
            activityMainBinding.boxRed.setBackgroundColor(getResources().getColor(R.color.red));
            activityMainBinding.boxBlue.setBackgroundColor(getResources().getColor(R.color.blue));
            activityMainBinding.boxYellow.setBackgroundColor(getResources().getColor(R.color.yellow));

            currentBox = green;
        }

        Log.e(TAG, "Current - "+currentBox);

        Log.e(TAG, "Number - "+number);

    }

    private void addOnColorTouch(){

        activityMainBinding.boxRed.setOnClickListener(v -> {

            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            timestamp = format.format(today);

            if (currentBox.equals(red)){
                score = score + 1;

                activityMainBinding.gameScore.setText(String.valueOf(score));

            }
            else{
                gameOver();
            }

        });

        activityMainBinding.boxBlue.setOnClickListener(v -> {

            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            timestamp = format.format(today);

            if (currentBox.equals(blue)){
                score = score + 1;

                activityMainBinding.gameScore.setText(String.valueOf(score));

            }
            else{
                gameOver();
            }
        });

        activityMainBinding.boxYellow.setOnClickListener(v -> {

            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            timestamp = format.format(today);

            if (currentBox.equals(yellow)){
                score = score + 1;

                activityMainBinding.gameScore.setText(String.valueOf(score));

            }
            else{
                gameOver();
            }
        });

        activityMainBinding.boxGreen.setOnClickListener(v -> {

            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            timestamp = format.format(today);

            if (currentBox.equals(green)){

                score = score + 1;

                activityMainBinding.gameScore.setText(String.valueOf(score));

            }
            else{
                gameOver();
            }
        });

    }

    private void afterOneSecond(){

        if (!timeRunning){
            t = new Timer();
        }

        t.scheduleAtFixedRate(new TimerTask() {

          @Override
          public void run() {

              timeRunning = true;
              Date today = new Date();
              SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
              String newTimeStamp = format.format(today);

              if (timestamp != null){
                  Date d1 = null;
                  Date d2 = null;
                  try {
                      d1 = format.parse(timestamp);
                      d2 = format.parse(newTimeStamp);
                  } catch (ParseException e) {
                      e.printStackTrace();
                      Log.e(TAG, "Parse Exception - "+e.getMessage());
                  }

                  long diff = Objects.requireNonNull(d2).getTime() - Objects.requireNonNull(d1).getTime();
                  
                  long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

                  if (seconds == 2){
                      t.cancel();
                      timeRunning = false;
                      runOnUiThread(() -> gameOver());
                  }

                  Log.e(TAG, "Seconds - "+seconds);

              }

          }

      }, 500, 1000);

    }

    private void showGameInstruction(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.game_instruction);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        Button yesButton = dialog.findViewById(R.id.token_ok);

        yesButton.setOnClickListener(v -> {
            dialog.dismiss();
            addOnColorTouch();
            startGame();
            afterOneSecond();
        });

        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void gameOver(){

        Log.e(TAG, "Game Over");

        if (handler!=null){
            handler.removeCallbacks(runnable);
        }

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.game_over);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        Button yesButton = dialog.findViewById(R.id.over_ok);
        TextView textView = dialog.findViewById(R.id.over_desc);

        textView.setText(getResources().getString(R.string.over_desc)+" "+ score);

        yesButton.setOnClickListener(v -> {
            dialog.dismiss();
            score = 0;
            activityMainBinding.gameScore.setText("0");
            addOnColorTouch();
            startGame();
            afterOneSecond();
        });

        dialog.show();

    }

    public void onStop(){
        super.onStop();
        Log.e(TAG, "Stopping");

        if (handler!=null){
            handler.removeCallbacks(runnable);
        }

        if (timeRunning){
            t.cancel();
        }

    }

    public void onDestroy(){
        super.onDestroy();
    }

}
