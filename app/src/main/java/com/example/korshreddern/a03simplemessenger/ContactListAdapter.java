package com.example.korshreddern.a03simplemessenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Korshreddern on 21-Apr-16.
 */
public class ContactListAdapter extends BaseAdapter {
    Context mContext;
    String[] strName;
    int resId;

    public ContactListAdapter(Context context, String[] strName, int resId) {
        this.mContext= context;
        this.strName = strName;
        this.resId = resId;
    }

    public int getCount() {
        return strName.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.contact_listview, parent, false);

        TextView textView = (TextView)view.findViewById(R.id.contact_list_username);
        textView.setText(strName[position]);

        ImageView imageView = (ImageView)view.findViewById(R.id.imageView1);
        imageView.setBackgroundResource(resId);

        return view;
    }
}