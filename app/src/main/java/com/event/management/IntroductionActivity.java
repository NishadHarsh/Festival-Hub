package com.event.management;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class IntroductionActivity extends AppCompatActivity {

    ViewPager vp_slider;
    IntroductionAdapter sliderPagerAdapter;

    String[] stringsName = {"0", "1", "2", "3"};
    int[] images = {R.drawable.payment_s1, R.drawable.notification_s2, R.drawable.attendance_s3, R.drawable.attendance_s3};

    DotsIndicator dotsIndicator;
    CountDownTimer countDownTimer;
    SharedPreferences sp;

    CardView skipCard;
    RelativeLayout skipRelative;
    TextView skipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        vp_slider = findViewById(R.id.introduction_slider);
        dotsIndicator = findViewById(R.id.introduction_dots);

        skipCard = findViewById(R.id.introduction_skip_card);
        skipRelative = findViewById(R.id.introduction_skip_relative);
        skipText = findViewById(R.id.introduction_skip_text);

        skipCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(ConstantSp.INTRODUCTION, "Yes").commit();
                new ToastIntentClass(IntroductionActivity.this, LoginActivity.class);
            }
        });

        skipRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(ConstantSp.INTRODUCTION, "Yes").commit();
                new ToastIntentClass(IntroductionActivity.this, LoginActivity.class);
            }
        });

        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(ConstantSp.INTRODUCTION, "Yes").commit();
                new ToastIntentClass(IntroductionActivity.this, LoginActivity.class);
            }
        });

        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                sp.edit().putString(ConstantSp.INTRODUCTION, "Yes").commit();
                new ToastIntentClass(IntroductionActivity.this, LoginActivity.class);
            }
        };

        sliderPagerAdapter = new IntroductionAdapter(IntroductionActivity.this, images, stringsName);
        dotsIndicator.setViewPager(vp_slider);
        vp_slider.setAdapter(sliderPagerAdapter);


        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (vp_slider.getCurrentItem() == 3) {
                    countDownTimer.start();
                } else if (vp_slider.getCurrentItem() == 4) {
                    countDownTimer.cancel();
                    sp.edit().putString(ConstantSp.INTRODUCTION, "Yes").commit();
                    new ToastIntentClass(IntroductionActivity.this, LoginActivity.class);
                } else {
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private class IntroductionAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        Context context;
        int[] images;
        String[] stringsName;

        public IntroductionAdapter(IntroductionActivity introductionActivity, int[] images, String[] stringsName) {
            this.context = introductionActivity;
            this.images = images;
            this.stringsName = stringsName;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.custom_introduction, container, false);
            ImageView im_slider = view.findViewById(R.id.custom_introduction_iv);
            Glide.with(context)
                    .load(images[position])
                    .into(im_slider);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
