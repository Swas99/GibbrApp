package com.swastik.gibbrtestapp.modules.your_story;

import android.graphics.Point;
import android.text.Html;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.swastik.gibbrtestapp.MainActivity;
import com.swastik.gibbrtestapp.R;
import com.swastik.gibbrtestapp.utils.CommonUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Swastik on 15-06-2016.
 */
public class YourStory
{

    MainActivity objMainActivity;

    public YourStory(WeakReference<MainActivity> _MainActivityWeakReference)
    {
        objMainActivity = _MainActivityWeakReference.get();
    }

    public void init(final String finalStory)
    {
        animateFinalScreen();

        objMainActivity.getSupportActionBar().setTitle("Your story");
        TextView tvFinalStory =(TextView)objMainActivity.findViewById(R.id.tvStory);
        tvFinalStory.setText(Html.fromHtml(finalStory));
        objMainActivity.findViewById(R.id.btnReplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objMainActivity.loadGetStartedScreen();
            }
        });
    }

    private void animateFinalScreen() {
        objMainActivity.setContentView(R.layout.screen_your_story);

        View rootView = objMainActivity.findViewById(R.id.rootView);
        rootView.setScaleX(0);
        rootView.setScaleY(0);
        Point screenSize = CommonUtil.getWindowSize(objMainActivity);
        rootView.setX(screenSize.x + screenSize.x/2);
        rootView.setY(-screenSize.y/2);
        rootView.animate().setInterpolator(new OvershootInterpolator(1.08f));
        rootView.animate()
                .x(0).y(0)
                .scaleX(1)
                .scaleY(1);
    }
}
