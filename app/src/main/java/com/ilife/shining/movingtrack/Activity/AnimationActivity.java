package com.ilife.shining.movingtrack.Activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.StateListAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ilife.shining.movingtrack.R;

public class AnimationActivity extends AppCompatActivity {

    TextView textView,textView2;
    Animation animation;
    Animator animator;
    StateListAnimator animatorSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        textView = (TextView) findViewById(R.id.tv_animation);
        textView2 = (TextView) findViewById(R.id.tv_animation2);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
        textView.startAnimation(animation);

        animator = AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.animator);

        animator.setTarget(textView2);
        animator.start();
    }


}
