package com.example.bling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class EditActivity extends AppCompatActivity {

    ImageView imv;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        imv = (ImageView)findViewById(R.id.imageView2);

        if(getIntent().hasExtra("byteArray")) {
            bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
            imv.setImageBitmap(bitmap);
        }
    }
}
