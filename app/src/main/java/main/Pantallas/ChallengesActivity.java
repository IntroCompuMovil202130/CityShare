package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;
import main.Adapters.ListUsersAdapter;
import main.Model.Usuario;

public class ChallengesActivity extends AppCompatActivity {

    ListView listUsers;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    static final String PATH_USERS="Users/";
    public static final String TAG=" Cityshare ";
    StorageReference storageReference;
    ListUsersAdapter listUsersAdapter;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        //ListView
        listUsers = findViewById(R.id.challengesList);
        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PATH_USERS);
        storageReference = FirebaseStorage.getInstance().getReference();
        //Adapter
        List<Usuario> users = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String nombre = child.getValue(Usuario.class).getName().toString();
                    String username = child.getValue(Usuario.class).getUserName().toString();
                    String email = child.getValue(Usuario.class).getEmail().toString();
                    String code=child.getKey();
                    Log.i(TAG,"Entre a datasnapshot for");
                    Usuario user = new Usuario(nombre,username,email,code);
                    //String search = "gs://cityshare-5a480.appspot.com/images/" +code+"/"+"contactImage";
                    /*String search="gs://cityshare-5a480.appspot.com/images/1oiPE2vTpiVuo2ok80caSwIo1B32/contactImage";

                    //gs://cityshare-5a480.appspot.com/images/Kc7D0O8DAWM5rwsVpUg2XiS2Dk63/ contactImage
                    Log.i(TAG,"Search URL "+search);
                    StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(search);

                    final long ONE_MEGABYTE = 1024 * 1024;
                    gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            user.setImage(bytes);
                            Log.i(TAG,"Pipo");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG,"PipoF");
                            Log.d(TAG,"Exception image: " + e);
                        }
                    });
                    Log.d(TAG,"See if reference: " + gsReference);

                    for(Usuario u:users) {
                        Log.d(TAG, "Valores de mierda2 "+u);
                    }*/
                    users.add(user);
                    listUsersAdapter = new ListUsersAdapter(ChallengesActivity.this,0,users);
                    listUsers.setAdapter(listUsersAdapter);
                    listUsers.setClickable(true);
                    listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Usuario selected=(Usuario) listUsers.getItemAtPosition(position);
                            Intent i= new Intent(ChallengesActivity.this,ChatActivity.class);
                            i.putExtra("nombre",selected.getUserName());
                            i.putExtra("code",selected.getCode());
                            startActivity(i);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}