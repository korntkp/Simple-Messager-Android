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
public class ChatMessageListAdapter extends BaseAdapter {
    Context context;
    String[] username;
    String[] message;

    public ChatMessageListAdapter(Context context, String[] username, String[] message) {
        this.context = context;
        this.username = username;
        this.message = message;
    }

    @Override
    public int getCount() {
        return username.length;
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
            convertView = mInflater.inflate(R.layout.chat_message_listview, parent, false);

        TextView textView_username = (TextView)convertView.findViewById(R.id.listview_chat_username);
        String message_settest = username[position] + ":";
        textView_username.setText(message_settest);

        TextView textView_message = (TextView)convertView.findViewById(R.id.listview_chat_message);
        textView_message.setText(message[position]);
        return convertView;
    }
}
