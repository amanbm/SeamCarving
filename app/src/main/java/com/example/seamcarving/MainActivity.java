package com.example.seamcarving;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Picture;
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

        cropHButton = (Button) findViewById(R.id.cropHButton);
        cropVButton = (Button) findViewById(R.id.cropVButton);
        image = (ImageView) findViewById(R.id.imageView);



        cropVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setMaxWidth(image.getWidth() - 10);

            }
        });


    }
}
