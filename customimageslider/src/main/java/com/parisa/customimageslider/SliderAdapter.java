package com.parisa.customimageslider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class SliderAdapter extends FragmentPagerAdapter {
    List<SliderItem> sliderItems;
    CustomSliderListener customSliderListener;

    public SliderAdapter(FragmentManager fm, List<SliderItem>  sliderItems, CustomSliderListener customSliderListener) {
        super(fm);
        this.sliderItems = sliderItems;
        this.customSliderListener = customSliderListener;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = SliderFragment.newInstance(sliderItems.get(position),position);
        ((SliderFragment)fragment).setCustomSliderListener(new CustomSliderListener() {
            @Override
            public void onItemClick(int position) {
                if(customSliderListener!=null)
                    customSliderListener.onItemClick(position);
            }
        });
        return fragment;
    }

    @Override
    public int getCount() {
        return sliderItems.size();
    }
}

