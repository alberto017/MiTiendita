package com.example.mitiendita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitiendita.Common.Common;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SliderWalkthrough extends AppCompatActivity {

    //Declaracion de variables
    private ViewPager viewPager;
    private IntroManager introManager;
    ViewPagerAdapter viewPagerAdapter;
    private int[] layouts;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private Button btnNext;
    private Button btnSkip;

    //Instancias firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_walkthrough);

        introManager = new IntroManager(this);
        if (!introManager.Check()) {
            introManager.setFirst(false);
            Intent intent = new Intent(SliderWalkthrough.this, MenuLateral.class);
            startActivity(intent);
            finish();
        }//introManager

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }//version

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);

        layouts = new int[]{R.layout.slider_walkthrough_0,
                R.layout.slider_walkthrough_2,
                R.layout.slider_walkthrough_3,
                R.layout.slider_walkthrough_5,
                R.layout.slider_walkthrough_6};

        addBottomDots(0);
        changeStatusBarColor();

        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }//onCreate

    private void addBottomDots(int position) {
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);
        }//addBottomDots

        if (dots.length > 0) {
            dots[position].setTextColor(colorInactive[position]);
        }//if
    }//addBottomDots

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                btnNext.setText("ANTERIOR");
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText("SIGUIENTE");
                btnSkip.setVisibility(View.VISIBLE);
            }//else
        }//onPageSelected

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }//if
    }//ChangeStatusBarColor

    public void onClick(View view) {

        //Conexion a firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("User");

        /*
        HashMap hashMap = new HashMap();
        String sliderStatus = "desactivado";
        hashMap.put("sliderStatus", sliderStatus);
         */

        switch (view.getId()) {
            case R.id.btn_next:
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {

                     /*
                    databaseReference.child(Common.currentUsuarioModel.getPhone()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            //Toast.makeText(MenuLateral.this,"Introduccion desactivada..",Toast.LENGTH_LONG).show();
                        }//onSuccess
                    });
                    */

                    Intent intent = new Intent(SliderWalkthrough.this, MenuLateral.class);
                    startActivity(intent);
                    finish();
                }//else
                break;
            case R.id.btn_skip:

                /*
                databaseReference.child(Common.currentUsuarioModel.getPhone()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        //Toast.makeText(MenuLateral.this,"Introduccion desactivada..",Toast.LENGTH_LONG).show();
                    }//onSuccess
                });
                 */

                Intent intent = new Intent(SliderWalkthrough.this, MenuLateral.class);
                startActivity(intent);
                finish();
                break;
        }//switch
    }//onClick

    private int getItem(int i) {
        return viewPager.getCurrentItem() + 1;
    }//getItem

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }//instantiateItem

        @Override
        public int getCount() {
            return layouts.length;
        }//getCount

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }//isViewFromObject

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }//destroyItem
    }//ViewPagweAdapter

}//SliderWalkthrough