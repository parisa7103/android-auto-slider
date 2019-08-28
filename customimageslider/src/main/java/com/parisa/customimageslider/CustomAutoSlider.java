package com.parisa.customimageslider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CustomAutoSlider extends RelativeLayout {

    public static int SCROLL_DIRECTION = 1;
    public final static int SCROLL_FROM_RIGHT = 1;
    public final static int SCROLL_FROM_LEFT = -1;

    public static final int ALIGN_PARENT_RIGHT = RelativeLayout.ALIGN_PARENT_RIGHT;
    public static final int ALIGN_PARENT_LEFT = RelativeLayout.ALIGN_PARENT_LEFT;
    public static final int ALIGN_PARENT_TOP = RelativeLayout.ALIGN_PARENT_TOP;
    public static final int ALIGN_PARENT_BOTTOM = RelativeLayout.ALIGN_PARENT_BOTTOM;
    public static final int ALIGN_CENTER_HORIZONTAL = RelativeLayout.CENTER_HORIZONTAL;

    View root;
    private static ViewPager pager;
    private List<SliderItem> sliderItems = new ArrayList<>();
    private static SliderAdapter mSectionsPagerAdapter;
    private Context context;
    private static int counter = 1000;
    static int min = 1;
    static int max;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private static int active = -1;
    private CustomSliderListener customSliderListener;
    private RelativeLayout dots_container;
    private static Handler handler;
    private boolean userScrollChange = false;
    private float mStartDragX;
    private boolean isEnd;
    private boolean isStart;


    public CustomAutoSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    //private methods
    private void init(Context context, AttributeSet attrs) {
        root = inflate(context, R.layout.slider_content, this);
        pager = root.findViewById(R.id.pager);
        dotsLayout = root.findViewById(R.id.dots_layout);
        dots_container = root.findViewById(R.id.dots_container);
        RelativeLayout container = root.findViewById(R.id.container);
    }

    private void switchDots(int position, int active) {

        if (active != -1) {
            TextView oldPosition = (TextView) dotsLayout.getChildAt(active);
            TextView newPosition = (TextView) dotsLayout.getChildAt(position);
            oldPosition.setTextColor(Color.parseColor("#50ffffff"));
            newPosition.setTextColor(Color.WHITE);
        }
    }

    private void initDots(int initialPos) {
        dotsLayout.removeAllViews();
        dots = new TextView[sliderItems.size()];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(context);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            if (i != initialPos) {
                dots[i].setTextColor(Color.parseColor("#50ffffff"));

            } else {
                dots[i].setTextColor(Color.WHITE);

            }
            dotsLayout.addView(dots[i]);
        }
    }

    //protected method
    protected static void initHandler(final int gravity, int pos) {
        max = pos;
        min = pos;
        if (handler == null)
            handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "run: handler starts");

                if (gravity == SCROLL_FROM_RIGHT)
                    if (max != 0) {
                        pager.setCurrentItem(max);
                        active = max;
                        max--;
                    } else {
                        pager.setCurrentItem(0);
                        active = 0;
                        max = mSectionsPagerAdapter.sliderItems.size() - 1;
                    }
                else if (gravity == SCROLL_FROM_LEFT) {
                    if (min < mSectionsPagerAdapter.sliderItems.size()) {
                        pager.setCurrentItem(min);
                        min++;
                    } else {
                        min = 0;
                        pager.setCurrentItem(min);
                        min++;
                    }
                }
                handler.postDelayed(this, counter);
            }
        }, 0);
    }

    protected void startAutoCircle(int pos) {

        if (SCROLL_DIRECTION == SCROLL_FROM_LEFT) {
            initHandler(SCROLL_FROM_LEFT, pos);
        } else if (SCROLL_DIRECTION == SCROLL_FROM_RIGHT) {
            max = sliderItems.size() - 1;
            initHandler(SCROLL_FROM_RIGHT, pos);
        }
        initDots(pos);

    }

    protected static void stopHandler() {
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    protected static int getCurrentPagerPos() {
        return pager.getCurrentItem();
    }

    protected void changePagerPosition(int pos) {
        pager.setCurrentItem(pos);
    }

    //public methods
    @SuppressLint("ClickableViewAccessibility")
    public void setPages(final List<SliderItem> sliderItems) {
        this.sliderItems.clear();
        this.sliderItems.addAll(sliderItems);
        handler = new Handler();
        mSectionsPagerAdapter = new SliderAdapter(((android.support.v4.app.FragmentActivity) context).getSupportFragmentManager(), sliderItems, customSliderListener);
        if (sliderItems.size() > 0) {
            pager.setAdapter(mSectionsPagerAdapter);
            if (SCROLL_DIRECTION == SCROLL_FROM_RIGHT) {
                active = sliderItems.size() - 1;
                pager.setCurrentItem(active, true);
            } else {
                active = 0;
                pager.setCurrentItem(active, true);
            }
            startAutoCircle(active);
            pager.setOffscreenPageLimit(sliderItems.size());
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Log.d("setAllPos", "position: " + position + "positionOffset: " + positionOffset + "positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switchDots(position, active);
                Log.d("ShowPosActive", "firstPos: " + "pos: " + position + "active: " + active);
/*              SliderFragment newfragment = (SliderFragment) mSectionsPagerAdapter.instantiateItem(pager, position);

                if (active != -1) {
                    SliderFragment oldfragment = (SliderFragment) mSectionsPagerAdapter.instantiateItem(pager, active);
                  oldfragment.hideTitle();
                }
                newfragment.showTitle();*/

                active = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("stateStatus", "state: " + state);
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    userScrollChange = true;
                    stopHandler();

                }
                if (userScrollChange && state == ViewPager.SCROLL_STATE_IDLE) {
                    if (isEnd) {
                        pager.setCurrentItem(0, true);
                        isEnd = false;
                    }
                    if (isStart) {
                        pager.setCurrentItem(sliderItems.size() - 1, true);
                        isStart = false;
                    }
                    startAutoCircle(pager.getCurrentItem());
                    userScrollChange = false;

                }
                Log.d("stateStatus", "UserScroll: " + userScrollChange);
            }
        });
    }

    public void setDotsGravity(int horizontalVertical, @NonNull int verticalGravity) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        params.addRule(horizontalVertical);
        params.addRule(verticalGravity);

        dotsLayout.setLayoutParams(params);
    }

    public void setTimer(int timer) {
        try {
            if (timer >= 1000) {
                counter = timer;
            } else {
                throw new Exception();
            }
        } catch (Exception timer1) {
            Log.d("Slider error", "timer must be equals or greater than 1 second ");
        }

    }

    public void setOnItemClickListener(CustomSliderListener customSliderListener) {
        this.customSliderListener = customSliderListener;
    }

    public void setGravity(int gravity) {
        switch (gravity) {
            case SCROLL_FROM_LEFT:
                SCROLL_DIRECTION = SCROLL_FROM_LEFT;
                break;
            case SCROLL_FROM_RIGHT:
                SCROLL_DIRECTION = SCROLL_FROM_RIGHT;
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDragX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStartDragX < x && getCurrentPagerPos() == 0) {
                    Log.d(TAG, "onInterceptTouchEvent: outAtStart");
                    isStart = true;
                } else if (mStartDragX > x && getCurrentPagerPos() == sliderItems.size() - 1) {
                    Log.d(TAG, "onInterceptTouchEvent: outAtEnd");
                    isEnd = true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void showDots() {
        dots_container.setVisibility(VISIBLE);
    }

    public void hideDots() {
        dots_container.setVisibility(GONE);
    }
}
