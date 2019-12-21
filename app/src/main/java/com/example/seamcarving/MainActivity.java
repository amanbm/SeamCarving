package com.example.seamcarving;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private int x;
    private int y;
    Button cropHButton;
    Button cropVButton;
    ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x = 0;
        y = 0;

        cropHButton = findViewById(R.id.cropHButton);
        cropVButton = findViewById(R.id.cropVButton);
        imageView = findViewById(R.id.imageView);

        // Take new picture
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        // TODO: make crop work with seam carving
        // Scale horizontal
        cropHButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;
                imageView.setScaleX(x);
            }
        });

        // TODO: make crop work with seam carving
        // scale vertical
        cropVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                y++;
                imageView.setScaleY(y);
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

}
