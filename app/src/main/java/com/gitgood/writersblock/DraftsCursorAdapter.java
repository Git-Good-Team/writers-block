package com.gitgood.writersblock;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DraftsCursorAdapter extends CursorAdapter {
    public DraftsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView tvTitle = view.findViewById(android.R.id.text1);
        tvTitle.setTextColor(context.getResources().getColor(R.color.color_drafts_list_text));
        tvTitle.setTextSize(10);

        String title = cursor.getString(cursor.getColumnIndex("TITLE"));

        tvTitle.setText(title);
    }
}
