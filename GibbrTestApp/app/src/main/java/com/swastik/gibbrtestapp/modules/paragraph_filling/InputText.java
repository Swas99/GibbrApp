package com.swastik.gibbrtestapp.modules.paragraph_filling;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.swastik.gibbrtestapp.MainActivity;
import com.swastik.gibbrtestapp.R;
import com.swastik.gibbrtestapp.modules.helper.MissingWord;
import com.swastik.gibbrtestapp.utils.CommonUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Swastik on 15-06-2016.
 */
public class InputText
{
    MainActivity objMainActivity;
    String sample_text;

    public InputText(WeakReference<MainActivity> _MainActivityWeakReference)
    {
        objMainActivity = _MainActivityWeakReference.get();
        sample_text= objMainActivity.getString(R.string.sample_story);
        init();
    }

    public void init()
    {
        objMainActivity.getSupportActionBar().setTitle("Input story");
        objMainActivity.loadScreen(R.layout.screen_input_text);
        AppCompatEditText etStory = (AppCompatEditText)objMainActivity.findViewById(R.id.etInputStory);
        etStory.setText(sample_text);

        objMainActivity.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStory = String.valueOf(((AppCompatEditText) objMainActivity.findViewById(R.id.etInputStory)).getText());
                Map<Integer,MissingWord> missingWordStringHashMap = generateMissingWords(inputStory);

                if(missingWordStringHashMap.size()>0)
                    objMainActivity.loadWordFillingScreen(inputStory, missingWordStringHashMap);
                else
                {
                    CommonUtil.showAlertMessage("Oops..",
                            "Your story has no place holders\n" +
                                    "Please write a story with place holders",
                            objMainActivity);
                    TextInputLayout inputLayoutInputStory =
                            ((TextInputLayout)objMainActivity.findViewById(R.id.inputLayoutInputStory));
                    inputLayoutInputStory.setError("Please write a story with place holders");
                }
            }
        });

    }

    private Map<Integer,MissingWord> generateMissingWords(String inputStory) {
        Map<Integer,MissingWord> missingWords = new TreeMap<Integer, MissingWord>();

        char startChar = '<';
        char endChar = '>';
        int startIndex = inputStory.indexOf(startChar);
        int endIndex = inputStory.indexOf(endChar);
        int id=1;
        String wordType;

        while (startIndex>0 && endIndex>0)
        {
            wordType = inputStory.substring(startIndex+1,endIndex);
            MissingWord objMissingWord = new MissingWord();
            objMissingWord.wordType = wordType;
            objMissingWord.startIndex = startIndex;
            objMissingWord.endIndex = endIndex;
            missingWords.put(id++,objMissingWord);

            startIndex = inputStory.indexOf(startChar,endIndex);
            endIndex = inputStory.indexOf(endChar,startIndex+1);
        }
        return missingWords;
    }

}
