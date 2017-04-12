package com.ri.keyboardanimator;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AnimationListener animListener ;

    private int mCurIndex = 0;

    private Animation mAnimBlink;

    private boolean mAnimationStarted = false;

    private long mCurrentAmount = 0;

    private TextView mAmountTV;

    private List<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animListener = new AnimationListener();

        mAmountTV = (TextView) findViewById(R.id.amountTV);
        buttons.add((Button)findViewById(R.id.digit1));
        buttons.add((Button)findViewById(R.id.digit2));
        buttons.add((Button)findViewById(R.id.digit3));
        buttons.add((Button)findViewById(R.id.digit4));
        buttons.add((Button)findViewById(R.id.digit5));
        buttons.add((Button)findViewById(R.id.digit6));
        buttons.add((Button)findViewById(R.id.digit7));
        buttons.add((Button)findViewById(R.id.digit8));
        buttons.add((Button)findViewById(R.id.digit9));
        buttons.add((Button)findViewById(R.id.digit0));

        mAnimBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        mAnimBlink.setAnimationListener(animListener);

        setupButtonAnimation(-1, 0);

    }

    private synchronized void setupButtonAnimation(final int prevIndex, final int index){

        Log.d("TAG","Setting up animation for " + prevIndex + "/" + index);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(prevIndex >=0) {
                    buttons.get(prevIndex).setAnimation(null);
                    buttons.get(prevIndex).clearAnimation();
                    buttons.get(prevIndex).invalidate();
                }
                buttons.get(index).invalidate();
                buttons.get(index).startAnimation(mAnimBlink);
            }
        }, 200);// delay in milliseconds (200)
    }

    private class AnimationListener implements Animation.AnimationListener  {
        @Override
        public void onAnimationStart(Animation animation) {
            mAnimationStarted = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d("TAG", "Called onAnimationEnd for " + mCurIndex);
            if(mAnimationStarted) {
                mAnimationStarted = false;
                int newIndex = (mCurIndex + 1) % buttons.size();
                setupButtonAnimation(mCurIndex, newIndex);
                mCurIndex = newIndex;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    public void selectDigitClicked(View view){
        Button currentButton = buttons.get(mCurIndex);
        String digit = (String) currentButton.getTag();
        mCurrentAmount = mCurrentAmount * 10 + Integer.valueOf(digit);
        mAmountTV.setText(getFormatedAmount(mCurrentAmount));
    }

    private String getFormatedAmount(double amount){
        return "$" + NumberFormat.getNumberInstance(Locale.US).format(amount) + ".00";
    }
}
