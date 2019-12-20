package com.example.seamcarving;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private int x;
    private int y;
    Button cropHButton;
    Button cropVButton;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x = 0;
        y = 0;
        cropHButton = findViewById(R.id.cropHButton);
        cropVButton = findViewById(R.id.cropVButton);
        image = findViewById(R.id.imageView);
        image.setImageResource(R.drawable.howard);


        // Crop horizontal
        cropHButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;
                image.setScaleX(x);
            }
        });

        // Crop vertical
        cropVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                y++;
                image.setScaleY(y);
            }
        });
    }
}
