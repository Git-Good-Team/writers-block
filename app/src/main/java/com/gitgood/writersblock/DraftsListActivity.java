package com.gitgood.writersblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DraftsListActivity extends AppCompatActivity {
    private DraftDatabaseHelper db;
    private SimpleCursorAdapter draftCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts_list);

        db = new DraftDatabaseHelper(getApplicationContext());

        final Cursor cursor = db.getAllTitlesCursor();
        String[] titles = new String[]{"TITLE"};
        final int[] to = new int[]{android.R.id.text1};
        draftCursor = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_1, cursor, titles,to){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView = view.findViewById(android.R.id.text1);
                    textView.setTextColor(getResources().getColor(R.color.color_drafts_list_text));
                    textView.setTextSize(getResources().getDimension(R.dimen.drafts_list_item_text_size));

                    return view;
                }
        };


        final ListView lvDrafts = findViewById(R.id.drafts_list);
        lvDrafts.setAdapter(draftCursor);

        lvDrafts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {

                cursor.moveToPosition(position);

                //We add +1 to compensate for zero-indexing vs table id difference.
                long rowId = cursor.getPosition()+1;

                Intent intent = new Intent(getApplicationContext(), DraftDetailsActivity.class);
                intent.putExtra("id", rowId);
                startActivity(intent);
            }
        });

        db.close();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
