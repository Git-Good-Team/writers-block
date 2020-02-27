package com.gitgood.writersblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DraftDetailsActivity extends AppCompatActivity {
    DraftDatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_details);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id",0);
        db = new DraftDatabaseHelper(getApplicationContext());

        System.out.println("Rowid on details: "+id);

        TextView tvTitle = findViewById(R.id.subject_text_view);
        TextView tvContent = findViewById(R.id.content_text_view);

        String[] data = db.getContent(id);
        String title = data[0];
        String content = data[1];

        tvTitle.setText(title);
        tvContent.setText(content);


    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, DraftsListActivity.class);
        startActivity(intent);
    }



}
