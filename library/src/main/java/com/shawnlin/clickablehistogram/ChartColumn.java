package com.shawnlin.clickablehistogram;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ShawnLin on 15/10/19.
 *
 */
public class ChartColumn {
    View mView;
    private int viewHeight = 0;

    public ChartColumn(Context context, int viewWidth, int margin, int color) {
        mView = new View(context);
        mView.setBackgroundColor(color);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(viewWidth, 0);
        params.setMargins(margin, 5, margin, 0);
        mView.setLayoutParams(params);


    }

    public int getHeight() {
        return mView.getLayoutParams().height;
    }

    public void setHeight(int height) {
        mView.getLayoutParams().height = height;
        mView.requestLayout();
    }

    public View getView() {
        return mView;
    }

    public void setColumnHeight(int height) {
        viewHeight = height;
    }

    public void startAnimation(int animTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator.ofInt(this, "height", viewHeight).setDuration(animTime).start();
        }
        else {
            setHeight(viewHeight);
        }
    }
}
