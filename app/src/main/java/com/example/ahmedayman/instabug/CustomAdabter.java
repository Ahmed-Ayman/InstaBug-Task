package com.example.ahmedayman.instabug;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Ayman on 07-Jun-16.
 */
public class CustomAdabter extends BaseAdapter {

    Context context;

List<RepoModel> item = new ArrayList<>();

    public CustomAdabter(Context context, List<RepoModel> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

 static class Holder {
        TextView repoName;
        TextView description;
        TextView userName;
        LinearLayout listItemBackGround ;
 }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    Holder viewHolder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);

            viewHolder = new Holder();
            viewHolder.repoName = (TextView) convertView.findViewById(R.id.repo_name);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.listItemBackGround = (LinearLayout) convertView.findViewById(R.id.list_item_layout);
            convertView.setTag(viewHolder);
        }
        else {
        viewHolder=(Holder)convertView.getTag();
        }

        viewHolder.repoName.setText(item.get(position).getRepoName());
        viewHolder.description.setText(item.get(position).getDescription());
        viewHolder.userName.setText(item.get(position).getUsername());

        //change the Background depending on the Fork statue!
        if (item.get(position).getFork().equals("true"))
            viewHolder.listItemBackGround.setBackgroundColor(Color.parseColor("#ffffff"));
        else
            viewHolder.listItemBackGround.setBackgroundColor(Color.parseColor("#D3D3D3"));
        convertView.setTag(viewHolder);
        return convertView;
    }
}
