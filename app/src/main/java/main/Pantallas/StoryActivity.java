package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class StoryActivity extends AppCompatActivity {
    ListView listaPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        listaPosts = findViewById(R.id.listaPosts);
    }
}