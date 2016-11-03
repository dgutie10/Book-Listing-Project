package com.example.android.booklisting;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by diegog on 11/3/2016.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> content){super(context,0, content);}

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book current = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(current.getTitle());

        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(current.getAuthor());

        ImageView cover = (ImageView) convertView.findViewById(R.id.book_thumbnail);
        cover.setImageResource(current.getThumbnail());

        return convertView;
    }
}
