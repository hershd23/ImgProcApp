package com.example.bling;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView dImageView;
    Bitmap bmp;
    int checkcam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button photoButton = (Button) findViewById(R.id.camera_button);
        dImageView = (ImageView) findViewById(R.id.dImageView);

        if(!hasCamera())
            photoButton.setEnabled(false);

    }

    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void launchCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Take a pic and pass along to on activity result;
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode ==RESULT_OK){
            //Get the data
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");
            checkcam = 0;

            dImageView.setImageBitmap(bmp);
        }

        if(requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            InputStream inputStream;

            try {
                inputStream = getContentResolver().openInputStream(imageUri);

                bmp = BitmapFactory.decodeStream(inputStream);

                checkcam = 1;
                dImageView.setImageBitmap(bmp);

            } catch(FileNotFoundException e){
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void send (View v){
        Intent i = new Intent(MainActivity.this, EditActivity.class);

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        if(checkcam ==0) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        }
        else
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, bs);
        i.putExtra("byteArray", bs.toByteArray());
        startActivity(i);

    }

    public void fromGallery(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

}
