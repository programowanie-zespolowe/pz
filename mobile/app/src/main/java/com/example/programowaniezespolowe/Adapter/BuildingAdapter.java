package com.example.programowaniezespolowe.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.programowaniezespolowe.Data.Building;
import com.example.programowaniezespolowe.R;

import java.util.ArrayList;

public class BuildingAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Building> buildings;

    public BuildingAdapter(Context context, ArrayList<Building> buildings) {
        this.context = context;
        this.buildings = buildings;
    }

    @Override
    public int getCount() {
        return buildings.size();
    }

    @Override
    public Object getItem(int position) {
        return buildings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.building_raw, parent, false);
        }
        byte[] decodeString = Base64.decode(buildings.get(position).getImageBuilding(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        ImageView imageView = convertView.findViewById(R.id.buildingImage);
        imageView.setImageBitmap(bmp);
        TextView textView = convertView.findViewById(R.id.buildingName);
        textView.setText(buildings.get(position).getNameBuilding());
        return convertView;
    }
}
