package com.gitgood.writersblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DraftsListActivity extends AppCompatActivity {
    private DraftDatabaseHelper db;

    private String[] examples = {"Beautiful Sunshine", "Fast Car", "Dirty Street","Dangerous Road",
            "Angry Mob","Vicious Beast","Doomed Saviour", "Artistic Dance", "Lovely Lady",
            "Hallowed Grove","Lost Sector", "Frightening Grimace"};

    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts_list);

        db = new DraftDatabaseHelper(getApplicationContext());

        try{
            titles = db.getAllTitles();
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this,"Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }



        ArrayAdapter<String> draftsAdapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView = view.findViewById(android.R.id.text1);
                    textView.setTextColor(getResources().getColor(R.color.color_drafts_list_text));
                    textView.setTextSize(getResources().getDimension(R.dimen.drafts_list_item_text_size));

                    return view;
                }
            };

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), DraftDetailsActivity.class);
            startActivity(intent);
            }
        };

        ListView draftsListView = findViewById(R.id.drafts_list);
        draftsListView.setOnItemClickListener(itemClickListener);
        draftsListView.setAdapter(draftsAdapter);

        db.close();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
