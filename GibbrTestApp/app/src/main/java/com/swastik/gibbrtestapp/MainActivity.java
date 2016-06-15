package com.swastik.gibbrtestapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.swastik.gibbrtestapp.modules.get_started.GetStarted;
import com.swastik.gibbrtestapp.modules.helper.MissingWord;
import com.swastik.gibbrtestapp.modules.paragraph_filling.InputText;
import com.swastik.gibbrtestapp.modules.paragraph_filling.StoryReview;
import com.swastik.gibbrtestapp.modules.paragraph_filling.WordFilling;
import com.swastik.gibbrtestapp.modules.your_story.YourStory;
import com.swastik.gibbrtestapp.utils.CommonUtil;

import java.lang.ref.WeakReference;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadGetStartedScreen();
    }

    public void loadGetStartedScreen() {
        GetStarted objGetStarted = new GetStarted(new WeakReference<>(this));
        objGetStarted.init();
    }

    public void loadInputStoryScreen() {
        InputText objInputText = new InputText(new WeakReference<>(this));
        objInputText.init();
    }

    public void loadWordFillingScreen(String inputStory, Map<Integer, MissingWord> missingWordStringHashMap) {
        WordFilling objWordFilling = new WordFilling(new WeakReference<>(this));
        objWordFilling.init(inputStory,missingWordStringHashMap);
    }
    public void loadStoryReviewScreen(String inputStory, Map<Integer, MissingWord> missingWordStringHashMap) {
        StoryReview objStoryReview = new StoryReview(new WeakReference<>(this));
        objStoryReview.init(inputStory, missingWordStringHashMap);
    }
    public void loadFinalStoryScreen(String formattedStory) {
        YourStory objYourStory = new YourStory(new WeakReference<>(this));
        objYourStory.init(formattedStory);
    }

    public void loadScreen(int id)
    {
//        View current_screen = findViewById(R.id.rootView);
        LayoutInflater inflater = getLayoutInflater();
        View next_screen = inflater.inflate(id,null);

//        current_screen.animate().alpha(0);
        setContentView(next_screen);
        next_screen.setAlpha(0);
        next_screen.animate().alpha(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            CommonUtil.showAlertMessage("Coming soon", "Coming soon!", MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Dialog getDialog(int id)
    {
        if(dialog==null)
        {
            dialog = new AlertDialog.Builder(MainActivity.this).show();
            dialog.setCancelable(false);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(id, null, true);
        dialog.setContentView(view);
        dialog.show();
        dialog.getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if(!backPressFlag)
                setBackPressFlag();
            else
            {
                finish();
            }
        }
        else
            super.onKeyDown(keyCode, event);

        return false;
    }

    boolean backPressFlag;
    public void setBackPressFlag()
    {
        try {
            backPressFlag = true;
            Toast.makeText(getApplicationContext(), "Tap again to exit", Toast.LENGTH_SHORT).show();
            new CountDownTimer(2700, 2700) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    backPressFlag = false;
                }
            }.start();
        }
        catch (Exception ex){
            //Toast.makeText(mContext,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
