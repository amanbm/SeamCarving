package com.example.seamcarving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class MainActivity extends AppCompatActivity {


    Bitmap currBitmap;
    Button cropHButton;
    Button cropVButton;
    Button captureButton;
    TextView textView;
    EditText seamCropNum;
    ImageView imageView;
    AStarSeamCarver sc;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    String currentPhotoPath;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_IMAGE_CAPTURE
            );
        }


        int permissionC = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permissionC != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_IMAGE_CAPTURE
            );
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cropHButton = findViewById(R.id.cropHButton);
        cropVButton = findViewById(R.id.cropVButton);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.resultOfSeamCarve);
        captureButton = findViewById(R.id.captureButton);
        seamCropNum = findViewById(R.id.editTextNumCrop);

        verifyStoragePermissions(this);

        //
        //Bitmap b = BitmapFactory.decodeResource(this.getResources(), R.drawable.chameleon);
        //sc = new AStarSeamCarver(b);


        // Take new picture
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");

                //testCode TODO: Change back to picture taken from device storage
                /*currBitmap = BitmapFactory.decodeResource(MainActivity.super.getResources(), R.drawable.chameleon);
                sc = new AStarSeamCarver(currBitmap);
                imageView.setImageBitmap(currBitmap);*/



                dispatchTakePictureIntent();
            }
        });

        //TODO: Make crop FasST
        cropHButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currBitmap == null){
                    textView.setText("Please input picture");
                } else {
                    int numSeams;
                    if(seamCropNum.getText().toString().equals("")){
                        numSeams = 2;
                    } else {
                        numSeams = Integer.parseInt(seamCropNum.getText().toString());
                    }

                    // loop over edit text num seams to delete
                    for(int i = 0; i < numSeams; i++) {
                        int[] horizontalRes = sc.findHorizontalSeam();
                        Bitmap newBitmap = Bitmap.createBitmap(currBitmap.getWidth(), currBitmap.getHeight() - 1, currBitmap.getConfig());
                        for(int j = 0; j < newBitmap.getWidth(); j++){
                            for(int k = 0; k < horizontalRes[j]; k++){
                                newBitmap.setPixel(j, k, Color.argb(Color.alpha(currBitmap.getPixel(j, k)), Color.red(currBitmap.getPixel(j, k)), Color.green(currBitmap.getPixel(j, k)), Color.blue(currBitmap.getPixel(j, k))));
                            }
                            for(int k = horizontalRes[j]+1; k < newBitmap.getHeight(); k++){
                                newBitmap.setPixel(j, k-1, Color.argb(Color.alpha(currBitmap.getPixel(j, k)), Color.red(currBitmap.getPixel(j, k)), Color.green(currBitmap.getPixel(j, k)), Color.blue(currBitmap.getPixel(j, k))));
                            }
                        }
                        currBitmap = newBitmap;
                        sc = new AStarSeamCarver(currBitmap);
                    }
                    imageView.setImageBitmap(currBitmap);

                    if (numSeams > 1) {
                        textView.setText("Deleted " + numSeams + " horizontal seams");
                    } else {
                        textView.setText("Deleted " + numSeams + " horizontal seam");
                    }

                }
            }
        });



        // TODO: make crop FaST
        cropVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currBitmap == null){
                    textView.setText("Please input picture");
                } else {
                    int numSeams;
                    if(seamCropNum.getText().toString().equals("")){
                        numSeams = 2;
                    } else {
                        numSeams = Integer.parseInt(seamCropNum.getText().toString());
                    }

                    // loop over edit text num seams to delete
                    for(int i = 0; i < numSeams; i++) {
                        int[] verticalRes = sc.findVerticalSeam();
                        Bitmap newBitmap = Bitmap.createBitmap(currBitmap.getWidth() - 1, currBitmap.getHeight(), currBitmap.getConfig());
                        for(int j = 0; j < newBitmap.getHeight(); j++){
                            for(int k = 0; k < verticalRes[j]; k++){
                                newBitmap.setPixel(k, j, Color.argb(Color.alpha(currBitmap.getPixel(k, j)), Color.red(currBitmap.getPixel(k, j)), Color.green(currBitmap.getPixel(k, j)), Color.blue(currBitmap.getPixel(k, j))));
                            }
                            for(int k = verticalRes[j]+1; k < newBitmap.getWidth(); k++){
                                newBitmap.setPixel(k-1, j, Color.argb(Color.alpha(currBitmap.getPixel(k, j)), Color.red(currBitmap.getPixel(k, j)), Color.green(currBitmap.getPixel(k, j)), Color.blue(currBitmap.getPixel(k, j))));
                            }
                        }
                        currBitmap = newBitmap;
                        sc = new AStarSeamCarver(currBitmap);
                    }
                    imageView.setImageBitmap(currBitmap);

                    if (numSeams > 1) {
                        textView.setText("Deleted " + numSeams + " vertical seams");
                    } else {
                        textView.setText("Deleted " + numSeams + " vertical seam");
                    }

                }
            }
        });
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { // if we have an app that can resolve our intent
            File photoFile = null;
            try {
                photoFile = createImageFile();

                // Continue only if the File was successfully created

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO); // creates camera

            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("AYAH");
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            imageView.setImageBitmap(bitmap);
            currBitmap = bitmap;
            sc = new AStarSeamCarver(currBitmap);
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "png_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* filename */
                ".png",   /* filetype */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
