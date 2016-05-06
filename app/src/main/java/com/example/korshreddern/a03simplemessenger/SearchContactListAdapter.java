package com.example.korshreddern.a03simplemessenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Korshreddern on 06-May-16.
 */
public class SearchContactListAdapter extends BaseAdapter {
    Context context;
    String[] searchContact;

    public SearchContactListAdapter(Context context, String[] searchContact) {
        this.context = context;
        this.searchContact = searchContact;
    }

    @Override
    public int getCount() {
        return searchContact.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = mInflater.inflate(R.layout.search_contact_listview, parent, false);

        TextView textView_username = (TextView)convertView.findViewById(R.id.textview_search_contact);
        textView_username.setText(searchContact[position]);
        return convertView;
    }
}
