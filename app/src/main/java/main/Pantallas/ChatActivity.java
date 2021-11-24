package main.Pantallas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import main.Model.AdapterMensajes;
import main.Model.Mensaje;
import main.Model.Usuario;

public class ChatActivity extends AppCompatActivity {

    ImageView fotoP;
    TextView nombre;
    RecyclerView recyclerView;
    EditText txtMensaje;
    ImageButton send;
    private AdapterMensajes adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    FirebaseUser us;
    String name,name2;
    public static final String TAG=" Cityshare ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        inflate();
        adapter=new AdapterMensajes(this);
        LinearLayoutManager l= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter);
        Bundle b=getIntent().getExtras();
        if(b!=null){
            name=b.getString("nombre");
        }
        nombre.setText(name);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.push().setValue(new Mensaje(txtMensaje.getText().toString(),name2,"","1","00:00"));
                /*databaseReference.child("chat").child(currentFirebaseUser.getUid()).child(name).setValue(
                        new Mensaje(txtMensaje.getText().toString(),nombre.getText().toString(),"","1","00:00")
                );*/
                txtMensaje.setText("");
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollBar();
            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensaje m= snapshot.getValue(Mensaje.class);
                adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setScrollBar(){
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
    }
    private void inflate(){
        fotoP= (ImageView) findViewById(R.id.fotoPerfil);
        nombre=(TextView) findViewById(R.id.nombreUsuario);
        recyclerView=(RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje=(EditText) findViewById(R.id.txtMensaje);
        send=(ImageButton) findViewById(R.id.btnEnviar);
        database=FirebaseDatabase.getInstance();
        databaseReference= database.getReference();//Sala chat
    }
}