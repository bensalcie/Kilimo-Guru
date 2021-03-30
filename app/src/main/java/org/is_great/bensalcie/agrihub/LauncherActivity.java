package org.is_great.bensalcie.agrihub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private IntroManager introManager;
    private int[] layouts;
    private ViewPagerAdapter viewPagerAdapter;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private Button next,skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        introManager=new IntroManager(this);
        if (!introManager.Check())
        {

           sendToLogin();
        }

        if (Build.VERSION.SDK_INT>=21)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_launcher);
        //getSupportActionBar().hide();
        viewPager=findViewById(R.id.view_pager);
        dotsLayout=findViewById(R.id.layoutDots);

        layouts=new int[]{ R.layout.activity_screen_one,R.layout.activity_screen_two,R.layout.activity_screen_three,R.layout.activity_screen_four,R.layout.activity_screen_five};

        next=findViewById(R.id.btn_next);
        skip=findViewById(R.id.btn_skip);

        addBottomDots(0);
        changeStatusBarColor();
        viewPagerAdapter=new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current=getItem(+1);
                if (current<layouts.length)
                {
                    viewPager.setCurrentItem(current);

                }else{

                    sendToLogin();
                }

            }
        });


    }

    private void sendToLogin() {
        introManager.setFirst(false);
        Intent in =new Intent(LauncherActivity.this,MainActivity.class) ;
        startActivity(in);
        finish();
    }

    private void addBottomDots(int position)
    {

        dots=new TextView[layouts.length];
        int [] colorActive=getResources().getIntArray(R.array.dot_active);
        int [] colorInactive=getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();

        for (int i=0;i<dots.length;i++)
        {//return here TO-DO
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);

        }
        if (dots.length>0)
        {
            dots[position].setTextColor(colorActive[position]);

        }

    }

    private int getItem(int i)
    {

        return viewPager.getCurrentItem()+1;
    }

    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addBottomDots(i);
            if (i==layouts.length-1)
            {
next.setText("PROCEED");
skip.setVisibility(View.GONE);

            }else {
                next.setText("NEXT");
                skip.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
    private void changeStatusBarColor()
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    public class  ViewPagerAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=layoutInflater.inflate(layouts[position],container,false);
            container.addView(v);
            return v;

        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view=(View)object;
            container.removeView(view);
        }
    }
}
