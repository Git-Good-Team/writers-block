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


        TextView contentTextView = findViewById(R.id.content_text_view);


        String[] data = db.getContent(id);

        contentTextView.setText(data[1]);

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, DraftsListActivity.class);
        startActivity(intent);
    }



}
