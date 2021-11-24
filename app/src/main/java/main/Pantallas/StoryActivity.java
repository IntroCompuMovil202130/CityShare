package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import main.Adapters.FilaAdapter;
import main.Adapters.FilaStoryAdapter;
import main.Adapters.StoryPrincipalAdapter;
import main.DTOs.FilaJugador;
import main.DTOs.FilaStory;
import main.DTOs.StoryPrincipal;

public class StoryActivity extends AppCompatActivity {
    ListView listaPosts;
    StoryPrincipalAdapter adapter;
    FilaStoryAdapter filaStoryAdapter;
    List<StoryPrincipal> storiesList;
    List<FilaStory> filaStories;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        getStories();

        listaPosts = findViewById(R.id.listaPosts);
        adapter = new StoryPrincipalAdapter(storiesList,null);



    }

    private List<FilaStory> storyPrincipalToFila() {
        List<FilaStory> retornar = new ArrayList<>();
        for (StoryPrincipal story: storiesList) {
            retornar.add(new FilaStory(story.getPhoto(),"Available",story.getIntentos(),story.getPromedio()));
        }
        return retornar;
    }

    private void getStories() {
        storiesList = new ArrayList<>();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref.child("Stories").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Consulta ", "Voy a hacer el fore");
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    storiesList.add(snapshot.getValue(StoryPrincipal.class));
                }
                filaStories = storyPrincipalToFila();
                Log.d("LISTA STORY",filaStories.toString());
                filaStoryAdapter = new FilaStoryAdapter(StoryActivity.this, 0, filaStories);
                listaPosts.setAdapter(filaStoryAdapter);
                listaPosts.setClickable(true);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
/*
    private List<FilaStory> getFromJSON() {
        List<FilaStory> result = new ArrayList<>();
        //Crear un arreglo de paises del JSON
        try {
            JSONObject obj = new JSONObject(cargarJSON("stories.json"));
            JSONArray arreglo = obj.getJSONArray("stories");

            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject object = arreglo.getJSONObject(i);
                Integer pictureRef = object.getInt("picture");
                String status = object.getString("status");
                Integer guesses = object.getInt("guesses");
                Double distance = object.getDouble("distance");

                FilaStory elemento = new FilaStory(pictureRef, status, guesses, distance);
                result.add(elemento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Consigue un String del JSON
    private String cargarJSON(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }*/
}