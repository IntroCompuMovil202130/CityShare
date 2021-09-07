package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import main.DTOs.FilaJugador;
import main.DTOs.FilaStory;

public class StoryActivity extends AppCompatActivity {
    ListView listaPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        listaPosts = findViewById(R.id.listaPosts);
        List<FilaStory> filaStories = getFromJSON();

    }

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
    }
}