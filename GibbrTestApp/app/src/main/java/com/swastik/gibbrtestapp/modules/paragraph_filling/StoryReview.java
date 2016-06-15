package com.swastik.gibbrtestapp.modules.paragraph_filling;

import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.swastik.gibbrtestapp.MainActivity;
import com.swastik.gibbrtestapp.R;
import com.swastik.gibbrtestapp.modules.helper.MissingWord;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by Swastik on 15-06-2016.
 */
public class StoryReview
{

    MainActivity objMainActivity;
    View.OnClickListener clickListener;
    int currentPlaceHolder;
    String inputStory;
    Map<Integer,MissingWord> missingWords;

    public StoryReview(WeakReference<MainActivity> mainActivityWeakReference) {
        objMainActivity = mainActivityWeakReference.get();
    }

    public void init(String inputStory, Map<Integer,MissingWord> missingWords)
    {
        this.inputStory = inputStory;
        this.missingWords = missingWords;

        initializeClickListener();
        objMainActivity.getSupportActionBar().setTitle("Review story");
        objMainActivity.loadScreen(R.layout.screen_story_review);
        TextView tvStory = (TextView)objMainActivity.findViewById(R.id.tvStory);
        String story_formatted = getFormattedStory();
        tvStory.setText(Html.fromHtml(story_formatted));

        objMainActivity.findViewById(R.id.btnDone).setOnClickListener(clickListener);
        createPlaceHolderButtons();
    }

    private String getFormattedStory() {
        MissingWord objMissingWord;
        String myStory = "";
        int currentIndex = 0;
        for (Map.Entry<Integer, MissingWord> entry : missingWords.entrySet())
        {
            objMissingWord = entry.getValue();
            myStory+= inputStory.substring(currentIndex, objMissingWord.startIndex - 1);
            myStory+= " <b>" + objMissingWord.userInput + "</b>";
            currentIndex = objMissingWord.endIndex+1;
        }
        myStory+=inputStory.substring(currentIndex);
        return myStory;
    }

    private void initializeClickListener() {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.btnDone:
                        {

                        }
                        break;
                    case R.id.btnOk:
                    {
                        String word = String.valueOf(((AppCompatEditText)
                                objMainActivity.dialog.findViewById(R.id.etWord)).getText());
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
                    }
                }
            }
        };
    }

    private void createPlaceHolderButtons()
    {
        int wordCount = missingWords.size();
        int cols = 3;
        int rows = wordCount/cols + 1;
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
        for (int i=wordCount+1; i<=rows*3;i++)
            objMainActivity.findViewById(i).setVisibility(View.GONE);
    }

    private void initializePlaceHolderButton(Button btn, int id)
    {
        btn.setId(id);
        btn.setOnClickListener(clickListener);
        btn.setTextColor(Color.parseColor("#47892D"));
        if(missingWords.containsKey(id))
            btn.setText("[" + String.valueOf(id) + "]" + "\n" + missingWords.get(id).userInput);
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
        rootView.animate().setInterpolator(new OvershootInterpolator(1.8f));
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
            objMissingWord.userInput = word;
            missingWords.put(currentPlaceHolder, objMissingWord);
            Button placeHolder = (Button)objMainActivity.findViewById(currentPlaceHolder);
            placeHolder.setText("[" + String.valueOf(currentPlaceHolder) + "]"
                    + "\n" + word);

            TextView tvStory = (TextView)objMainActivity.findViewById(R.id.tvStory);
            String story_formatted = getFormattedStory();
            tvStory.setText(Html.fromHtml(story_formatted));
            objMainActivity.dialog.dismiss();
        }
    }
}
