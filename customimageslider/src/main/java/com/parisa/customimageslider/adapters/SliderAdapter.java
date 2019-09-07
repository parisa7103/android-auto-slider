package com.parisa.customimageslider.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.parisa.customimageslider.interfaces.CustomSliderListener;
import com.parisa.customimageslider.fragments.SliderFragment;
import com.parisa.customimageslider.SliderItem;

import java.util.List;

public class SliderAdapter extends FragmentPagerAdapter {
    public List<SliderItem> sliderItems;
    private CustomSliderListener customSliderListener;

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

