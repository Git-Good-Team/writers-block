package com.gitgood.writersblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LinearLayout writeLayout = findViewById(R.id.write_layout);
        LinearLayout draftsLayout = findViewById(R.id.drafts_layout);

        writeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWriteLayoutClick(view);
            }
        });

        draftsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDraftsLayoutClick(view);
            }
        });

    }

    private void onWriteLayoutClick(View view) {
        Intent intent = new Intent(this, WriterActivity.class);
        startActivity(intent);

    }

    private void onDraftsLayoutClick(View view) {
        //TODO: go to drafts list activity
    }
}
