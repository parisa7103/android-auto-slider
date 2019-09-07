package com.parisa.androidcustomslider;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parisa.customimageslider.CustomAutoSlider;
import com.parisa.customimageslider.interfaces.CustomSliderListener;
import com.parisa.customimageslider.SliderItem;
import com.parisa.customimageslider.model.CustomDirection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomAutoSlider customAutoSlider = findViewById(R.id.slider);

        //1. set a clickListener
        customAutoSlider.setOnItemClickListener(new CustomSliderListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "this is position" + position, Toast.LENGTH_SHORT).show();
            }
        });

        //2. how to set gravity. To scroll rtl : CustomAutoSlider.SCROLL_FROM_RIGHT
        customAutoSlider.setGravity(CustomDirection.SCROLL_FROM_RIGHT);

        //3. how to set interval in millisecond
        customAutoSlider.setTimer(3000);

        //4. how to add list items. now we need to add lists after all configuration
        //5. how to add texts to sliders if necessary
        //6. You can add drawables from local too
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem("Hi",
                "https://filemanager.bonakchi.com/api/media/c689077b-ebc8-40df-b773-3d292cb4f8e0",R.drawable.placeholder));
        sliderItems.add(new SliderItem("Welcome",
                "https://filemanager.bonakchi.com/api/media/55021937-4414-4c0e-a28f-0f38f10c9816",R.drawable.placeholder));
        sliderItems.add(new SliderItem("ایران",
                "https://filemanager.bonakchi.com/api/media/d76104c8-c83a-4b8d-8748-da97fe22d706",R.drawable.placeholder));
        sliderItems.add(new SliderItem("خانه",
                "https://filemanager.bonakchi.com/api/media/54837319-1d3e-4dfe-b1b8-165da5054268",R.drawable.placeholder));

        customAutoSlider.setPages(sliderItems);

        customAutoSlider.setDotsGravity(CustomAutoSlider.CENTER_HORIZONTAL,CustomAutoSlider.ALIGN_PARENT_BOTTOM);
    }
}
