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
 * <p>
 * it's just a custom adapter to display the data in the List view ...
 */
public class CustomAdabter extends BaseAdapter {
    Context context;
    List<String> repoName = new ArrayList<>();
    List<String> description = new ArrayList<>();
    List<String> username = new ArrayList<>();
    List<Boolean> fork = new ArrayList<>();

    public CustomAdabter(Context context, List<String> repoName, List<String> description, List<String> username, List<Boolean> fork) {
        this.context = context;
        this.repoName = repoName;
        this.description = description;
        this.username = username;
        this.fork = fork;
    }

    @Override
    public int getCount() {
        return repoName.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView repoName;
        TextView description;
        TextView userName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            Holder holder = new Holder();
            holder.repoName = (TextView) convertView.findViewById(R.id.repo_name);
            holder.repoName.setText(repoName.get(position));
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.description.setText(description.get(position));
            holder.userName = (TextView) convertView.findViewById(R.id.user);
            holder.userName.setText(username.get(position));

            //change the Background depending on the Fork statue!
            LinearLayout lLayout = (LinearLayout) convertView.findViewById(R.id.list_item_layout);
            if (fork.get(position) == true)
                lLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            else
                lLayout.setBackgroundColor(Color.parseColor("#D3D3D3"));


            convertView.setTag(holder);

        }
        convertView.getTag();
        return convertView;
    }
}
