package com.parisa.customimageslider.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parisa.customimageslider.CustomAutoSlider;
import com.parisa.customimageslider.interfaces.CustomSliderListener;
import com.parisa.customimageslider.R;
import com.parisa.customimageslider.SliderItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;

import com.squareup.picasso.Picasso;

public class SliderFragment extends Fragment {

    private SliderItem item;
    private LinearLayout title_layout;
    private TextView title;
    private ImageView imageView;
    private boolean isStopByTouch = false;
    private boolean isLongPressed = false;

    public CustomSliderListener getCustomSliderListener() {
        return customSliderListener;
    }

    public void setCustomSliderListener(CustomSliderListener customSliderListener) {
        this.customSliderListener = customSliderListener;
    }

    private CustomSliderListener customSliderListener;

    private int position = -1;

    public SliderFragment() {
    }

    public static SliderFragment newInstance(SliderItem item, int position) {
        SliderFragment fragment = new SliderFragment();
        Bundle args = new Bundle();
        args.putParcelable("item", item);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getArguments()
                .getParcelable("item");
        position = getArguments()
                .getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.slider_item_container, container, false);
        title = rootView.findViewById(R.id.title);
        title_layout = rootView.findViewById(R.id.title_layout);
        imageView = rootView.findViewById(R.id.img);

        if (item.getTitle() != null) {
            title.setText(item.getTitle());
            title_layout.setVisibility(View.VISIBLE);
        } else {
            title_layout.setVisibility(View.GONE);
        }

        if (item.getResID() != 0) {
            Picasso.get()
                    .load(item.getResID())
                    .placeholder(item.getPlaceHolder())
                    .into(imageView);
        }

        if (item.getUrl() != null) {
         /*   Picasso.get()
                    .load(item.getUrl())
                    .placeholder(item.getPlaceHolder())
                    .into(imageView);*/
            Picasso.get()
                    .load(item.getPlaceHolder()) // thumbnail url goes here
                    //.placeholder(R.drawable.progress_animation)
                    .transform(new BlurTransformation(getContext(), 25, 1))
                    .into(imageView, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            Picasso.get()
                                    .load(item.getUrl()) // image url goes here
                                    .placeholder(R.drawable.rounded)
                                    .into(imageView);
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(item.getUrl()) // image url goes here
                                    .placeholder(R.drawable.rounded)
                                    .into(imageView);
                        }

                                         });
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customSliderListener != null) {
                    customSliderListener.onItemClick(position);
                }
            }
        });

        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPressed = true;
                CustomAutoSlider.stopHandler();
                Log.d("LongPressClick", "" + isStopByTouch);
                return false;
            }
        });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View pView, MotionEvent pEvent) {
                pView.onTouchEvent(pEvent);
                float x = pEvent.getX();
                if (pEvent.getAction() == MotionEvent.ACTION_UP && isLongPressed) {
                    CustomAutoSlider.initHandler(CustomAutoSlider.SCROLL_DIRECTION, CustomAutoSlider.getCurrentPagerPos());
                    isLongPressed = false;
                }
                return false;
            }
        });

        return rootView;
    }

    public void showTitle() {
        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottom_up);
        title_layout.startAnimation(bottomUp);
        title_layout.setVisibility(View.VISIBLE);
    }

    public void hideTitle() {
        title_layout.setVisibility(View.GONE);
    }

}
