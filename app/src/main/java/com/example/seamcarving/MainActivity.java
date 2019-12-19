package com.example.seamcarving;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    Button cropHButton;
    Button cropVButton;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cropHButton = findViewById(R.id.cropHButton);
        cropVButton = findViewById(R.id.cropVButton);
        image = findViewById(R.id.imageView);
        image.setImageResource(R.drawable.sig);
        image.setScaleY(10);


        // Crop horizontal
        cropHButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setMaxHeight((int) Math.round((image.getHeight() * 9.0) / 10.0));
            }
        });

        // Crop vertical
        cropVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setMaxWidth((int) Math.round((image.getWidth() * 9.0) / 10.0));
            }
        });

    }
}
