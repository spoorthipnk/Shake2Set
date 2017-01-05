package com.example.spoor.wallpapersetter2;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class PictureActivity extends AppCompatActivity implements SensorEventListener {
    static final int IMAGE_CAPTURE = 1;
    static final int GALLERY_IMAGE = 2;
    private float x1, x2, x3;
    private static final float ERROR = (float) 7.0;
    private ImageButton camera;
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private boolean init = false;
    private int counter = 0;
    private Bitmap photo;
    ArrayList<Bitmap> imageList;
    Cursor cursor;
    Button btn_gallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        imageList = new ArrayList<>();

        //camera intent to click pictures
        camera = (ImageButton)findViewById(R.id.imgBtn_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePicture.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePicture, IMAGE_CAPTURE);
                }
            }
        });

        //gallery intent to select images from gallery
        btn_gallery = (Button)findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,GALLERY_IMAGE);*/

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(chooserIntent, GALLERY_IMAGE);
            }
        });


        //setting up the sensor manager and registering listener
        sensorManager  = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        //setting grid view
        GridView gridView = (GridView)findViewById(R.id.gridview_photos);
        gridView.setNumColumns(3);
        gridView.setAdapter(new GridViewAdapter(getApplicationContext(),imageList));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageList.add(photo);
        }

        if(requestCode == GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data!=null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageList.add(BitmapFactory.decodeFile(picturePath));
        }
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x,y,z;
        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];

        if(!init){
            x1 = x;
            x2 = y;
            x3 = z;
            init = true;
        }
        else{
            float diffX = Math.abs(x1 - x);
            float diffY = Math.abs(x2 - y);
            float diffZ = Math.abs(x3 - z);

            //Handling ACCELEROMETER Noise
            if (diffX < ERROR) {

                diffX = (float) 0.0;
            }
            if (diffY < ERROR) {
                diffY = (float) 0.0;
            }
            if (diffZ < ERROR) {

                diffZ = (float) 0.0;
            }

            x1 = x;
            x2 = y;
            x3 = z;

            //Horizontal shake detected
            if(diffX>diffY){
                counter++;
                Random random = new Random();
                int pos = random.nextInt(imageList.size());
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try{
                    Toast.makeText(PictureActivity.this, "Shake Detected!"+counter, Toast.LENGTH_SHORT).show();
                    wallpaperManager.setBitmap(imageList.get(pos));
                }
                catch (Exception e){
                     e.printStackTrace();
                }

            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
