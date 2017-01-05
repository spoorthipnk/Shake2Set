package com.example.spoor.wallpapersetter2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by spoor on 12/17/2016.
 */

public class GridViewAdapter extends BaseAdapter{
private Context context;
   // private int layoutResourceId;
    private ArrayList<Bitmap> imagesList = new ArrayList<Bitmap>();
   // private     int[] ImageId;
    public GridViewAdapter(Context context, ArrayList<Bitmap> imagesList){

        this.context = context;
        this.imagesList = imagesList;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(500,500));
          //  imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(4,4,4,4);
            imageView.setPadding(3,3,3,4);


        }
        else{
            imageView = (ImageView)convertView;
        }
        imageView.setImageBitmap(imagesList.get(i));
        return imageView;
    }

    public int getCount(){
        return imagesList.size();
    }

    public Object getItem(int position){
        return imagesList.get(position);
    }
}
