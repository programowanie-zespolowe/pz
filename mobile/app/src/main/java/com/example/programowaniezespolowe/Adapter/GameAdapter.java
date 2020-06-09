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

import com.example.programowaniezespolowe.Data.OutdoorGame;
import com.example.programowaniezespolowe.R;

import java.util.ArrayList;

public class GameAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OutdoorGame> games;

    public GameAdapter(Context context, ArrayList<OutdoorGame> games) {
        this.context = context;
        this.games = games;
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.game_raw, parent, false);
        }
        byte[] decodeString = Base64.decode(games.get(position).getImageGame(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        ImageView imageView = convertView.findViewById(R.id.gameImage);
        imageView.setImageBitmap(bmp);
        TextView textView = convertView.findViewById(R.id.gameName);
        textView.setText(games.get(position).getNameGame());
        return convertView;
    }
}
