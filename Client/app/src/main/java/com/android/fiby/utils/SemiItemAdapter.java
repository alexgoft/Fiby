package com.android.fiby.utils;

import android.app.Activity;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.fiby.R;
import com.android.fiby.structures.SemiItem;

import java.util.ArrayList;


public class SemiItemAdapter extends BaseAdapter {
    ArrayList<SemiItem> list;
    Activity activity;
    TextView txtFirst;

    public SemiItemAdapter(Activity activity, ArrayList<SemiItem> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();

        if(view == null){

            view = inflater.inflate(R.layout.colmn_row, null);

            txtFirst = (TextView) view.findViewById(R.id.col_a);
        }


        txtFirst.setText(list.get(i).getDisplayName());
        txtFirst.setTag(list.get(i));
        return view;
    }
}
