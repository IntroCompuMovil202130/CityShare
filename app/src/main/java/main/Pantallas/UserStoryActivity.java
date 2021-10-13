package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class UserStoryActivity extends AppCompatActivity {

    ImageView story;
    ImageView principalStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_story);


        story = findViewById(R.id.userStory);
        try {
            Bundle extras = getIntent().getExtras();
            byte[] byteArray = extras.getByteArray("picture");

            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            story.setImageBitmap(bmp);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}