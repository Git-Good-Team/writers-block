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

import java.util.ArrayList;

public class DraftsListActivity extends AppCompatActivity {
    private DraftDatabaseHelper db;
    private SimpleCursorAdapter draftCursor;

    private String[] examples = {"Beautiful Sunshine", "Fast Car", "Dirty Street","Dangerous Road",
            "Angry Mob","Vicious Beast","Doomed Saviour", "Artistic Dance", "Lovely Lady",
            "Hallowed Grove","Lost Sector", "Frightening Grimace"};

    private ArrayList<String> titles;

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


        System.out.println(db.getAllTitles());
        final ListView lvDrafts = findViewById(R.id.drafts_list);
        lvDrafts.setAdapter(draftCursor);

        lvDrafts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {

                cursor.moveToPosition(position);
                long rowId = cursor.getPosition()+1;

                System.out.println(rowId);

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
