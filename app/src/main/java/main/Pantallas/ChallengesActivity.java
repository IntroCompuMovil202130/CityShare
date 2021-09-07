package main.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import main.DTOs.FilaJugador;
import main.DTOs.FilaStory;

public class ChallengesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        List<FilaJugador> filaJugadors = getFromJSON();
    }

    private List<FilaJugador> getFromJSON() {
        List<FilaJugador> result = new ArrayList<>();
        //Crear un arreglo de paises del JSON
        try {
            JSONObject obj = new JSONObject(cargarJSON("challenges.json"));
            JSONArray arreglo = obj.getJSONArray("challenges");

            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject object = arreglo.getJSONObject(i);
                Integer pictureRef = object.getInt("picture");
                String name = object.getString("name");
                String content = object.getString("content");

                FilaJugador elemento = new FilaJugador(pictureRef, name, content);
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