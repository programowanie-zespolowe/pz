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

import com.example.programowaniezespolowe.Data.Group;
import com.example.programowaniezespolowe.R;

import java.util.ArrayList;

public class AdapterGroup extends BaseAdapter {
    private Context context;
    private ArrayList<Group> groups;

    public AdapterGroup(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public int getCount() {
       return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.category_image, parent, false);
        }
        byte[] decodeString = Base64.decode(groups.get(0).getImageGroup(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        ImageView imageView = convertView.findViewById(R.id.iconCategory);
        imageView.setImageBitmap(bmp);
        TextView textView = convertView.findViewById(R.id.categoryName);
        textView.setText(groups.get(position).getNameGroup());
        return convertView;
    }
}
