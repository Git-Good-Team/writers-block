package com.gitgood.writersblock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout writeLayout = findViewById(R.id.write_layout);
        LinearLayout draftsLayout = findViewById(R.id.drafts_layout);
        LinearLayout dailyWordLayout = findViewById(R.id.daily_word_layout);

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

        dailyWordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDailyWordLayoutClick(view);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    private void onWriteLayoutClick(View view) {
        Intent intent = new Intent(this, WriterActivity.class);
        startActivity(intent);

    }

    private void onDraftsLayoutClick(View view) {
        Intent intent = new Intent(this, DraftsListActivity.class);
        startActivity(intent);
    }

    private void onDailyWordLayoutClick(View view) {
        Intent intent = new Intent(this, DailyWordActivity.class);
        startActivity(intent);
    }

    public void onSettingsAction(MenuItem menuItem) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
