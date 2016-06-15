package com.swastik.gibbrtestapp.modules.paragraph_filling;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.swastik.gibbrtestapp.MainActivity;
import com.swastik.gibbrtestapp.R;
import com.swastik.gibbrtestapp.modules.helper.MissingWord;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by Swastik on 15-06-2016.
 */
public class WordFilling
{
    MainActivity objMainActivity;
    View.OnClickListener clickListener;
    int wordsRemaining;
    int currentPlaceHolder;
    String inputStory;
    float targetX;
    Map<Integer,MissingWord> missingWords;

    public WordFilling(WeakReference<MainActivity> mainActivityWeakReference) {
        objMainActivity = mainActivityWeakReference.get();
    }

    public void init(String inputStory, Map<Integer,MissingWord> missingWords)
    {
        targetX = -1;
        this.inputStory = inputStory;
        this.missingWords = missingWords;

        initializeClickListener();
        objMainActivity.getSupportActionBar().setTitle("Gibbr fill");
        objMainActivity.loadScreen(R.layout.screen_word_filling);
        TextView tvWordCount = (TextView)objMainActivity.findViewById(R.id.tvWordsRemaining);
        tvWordCount.setText(missingWords.size() + " words remaining");
        objMainActivity.findViewById(R.id.btnNext).setOnClickListener(clickListener);
        wordsRemaining=missingWords.size();
        createPlaceHolderButtons();
    }

    private void initializeClickListener() {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.btnNext:
                        if(wordsRemaining>0)
                            showErrorMessage();
                        else
                        {
                            objMainActivity.loadStoryReviewScreen(inputStory,missingWords);
                        }
                        break;
                    case R.id.btnOk:
                    {
                        String word = String.valueOf(((AppCompatEditText)
                                objMainActivity.dialog.findViewById(R.id.etWord)).getText()).trim();
                        processWord(word);
                        break;
                    }
                    case R.id.btnCancel:
                        objMainActivity.dialog.dismiss();
                        break;

                    default:
                    {
                        currentPlaceHolder = v.getId();
                        OpenWordFillerDialog();
                        TextView tvWordCount = (TextView)objMainActivity.findViewById(R.id.tvWordsRemaining);
                        tvWordCount.setTextColor(Color.DKGRAY);
                    }
                }
            }
        };
    }

    private void showErrorMessage() {
        Toast.makeText(objMainActivity.getApplicationContext(),
                "Please fill all the words to continue", Toast.LENGTH_SHORT).show();

        final TextView tvWordCount = (TextView)objMainActivity.findViewById(R.id.tvWordsRemaining);
        tvWordCount.setTextColor(Color.RED);
        tvWordCount.animate().setInterpolator(new OvershootInterpolator(2.7f));
        final long duration = tvWordCount.animate().getDuration();
        if(targetX==-1)
            targetX = tvWordCount.getX();
        new CountDownTimer(duration,duration/4)
        {

            float delta[] = { 100,-200,150,-90};
            int index = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                    tvWordCount.animate().xBy(delta[index++]);
            }

            @Override
            public void onFinish() {
                tvWordCount.animate().x(targetX);
            }
        }.start();
    }

    private void createPlaceHolderButtons()
    {
        int cols = 3;
        int rows = wordsRemaining/cols + 1;
        int id = 1;

        ViewGroup region_placeholders = (ViewGroup)objMainActivity.findViewById(R.id.region_place_holder_buttons);

        for (int i=0;i<rows;i++)
        {
            View place_holder_row = objMainActivity.getLayoutInflater().inflate(R.layout.view_place_holder_row,region_placeholders,false);
            Button btn1 = (Button)place_holder_row.findViewById(R.id.btn1);
            Button btn2 = (Button)place_holder_row.findViewById(R.id.btn2);
            Button btn3 = (Button)place_holder_row.findViewById(R.id.btn3);
            initializePlaceHolderButton(btn1, id++);
            initializePlaceHolderButton(btn2, id++);
            initializePlaceHolderButton(btn3, id++);
            region_placeholders.addView(place_holder_row);
        }
        for (int i=wordsRemaining+1; i<=rows*3;i++)
            objMainActivity.findViewById(i).setVisibility(View.GONE);
    }

    private void initializePlaceHolderButton(Button btn, int id)
    {
        btn.setId(id);
        btn.setOnClickListener(clickListener);
        btn.setText("[" + String.valueOf(id) + "]" + "\n" + "<click me>");
    }

    private void OpenWordFillerDialog() {
        objMainActivity.dialog = objMainActivity.getDialog(R.layout.dialog_word_filler);
        objMainActivity.dialog.findViewById(R.id.btnOk).setOnClickListener(clickListener);
        objMainActivity.dialog.findViewById(R.id.btnCancel).setOnClickListener(clickListener);
        MissingWord objMissingWord = missingWords.get(currentPlaceHolder);
        AppCompatEditText etWord = (AppCompatEditText) objMainActivity.dialog.findViewById(R.id.etWord);
        etWord.setHint(objMissingWord.wordType);
        if(objMissingWord.userInput!=null && !objMissingWord.userInput.isEmpty())
            etWord.setText(objMissingWord.userInput);

        TextView tvHeader = (TextView) objMainActivity.dialog.findViewById(R.id.tvHeader);
        String vowel_check = "a ";
        if(objMissingWord.wordType.startsWith("a") || objMissingWord.wordType.startsWith("e") ||
                objMissingWord.wordType.startsWith("i") || objMissingWord.wordType.startsWith("o") ||
                objMissingWord.wordType.startsWith("u"))
            vowel_check = "an ";

        tvHeader.setText("Please enter " + vowel_check + objMissingWord.wordType);

        View rootView = objMainActivity.dialog.findViewById(R.id.rootView);
        rootView.setScaleX(0);
        rootView.setScaleY(0);
        rootView.animate().setInterpolator(new OvershootInterpolator(1.2f));
        rootView.animate().scaleX(1).scaleY(1);
    }

    private void processWord(String word) {
        MissingWord objMissingWord = missingWords.get(currentPlaceHolder);

        if(word.isEmpty())
        {
            TextInputLayout inputLayoutInputStory =
                    ((TextInputLayout)objMainActivity.dialog.findViewById(R.id.inputLayoutWord));
            inputLayoutInputStory.setError("Please enter a valid " + objMissingWord.wordType);
        }else {
            if(objMissingWord.userInput==null || objMissingWord.userInput.isEmpty())
                wordsRemaining--;
            objMissingWord.userInput = word;
            missingWords.put(currentPlaceHolder, objMissingWord);
            Button placeHolder = (Button)objMainActivity.findViewById(currentPlaceHolder);
            placeHolder.setTextColor(Color.parseColor("#47892D"));
            placeHolder.setText("[" + String.valueOf(currentPlaceHolder) + "]"
                    + "\n" + word);
            TextView tvWordCount = (TextView)objMainActivity.findViewById(R.id.tvWordsRemaining);
            if(wordsRemaining==0)
                tvWordCount.setText("Done!\nClick next to review your story");
            else
                tvWordCount.setText(wordsRemaining + " words remaining");
            objMainActivity.dialog.dismiss();
        }
    }


}
