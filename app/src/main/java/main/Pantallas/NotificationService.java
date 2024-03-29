package main.Pantallas;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import main.DTOs.StoryPrincipal;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class NotificationService extends IntentService {

    private static final String TAG = "Servicio";
    private static final String CHANNEL_ID = "servicio_notificaciones" ;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;
    private Map<String, Integer> mapa = new HashMap<>();
    private int notificationId = 001;
    private String myKey;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;


    public NotificationService() {
        super("Servicio de notificaciones");
        Log.i(TAG, "Constructor del servicio ");

    }


    public void start() {

        Log.i(TAG, "Entre a start Service");
        ref = database.getReference().child("Stories").child(currentFirebaseUser.getUid());
        Log.i(TAG, "UID Usuario actual: " + currentFirebaseUser.getUid());

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                 StoryPrincipal u = dataSnapshot.getValue(StoryPrincipal.class);
                 Log.i(TAG, "Agregando " + u.getNombre() + " al servicio");
                 mapa.put(dataSnapshot.getKey(), u.getIntentos());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

                Log.i(TAG, "escuché un cambio");
                    StoryPrincipal changedStory = dataSnapshot.getValue(StoryPrincipal.class);
                    Log.i(TAG, "onChildChanged: ");
                    Log.i(TAG, String.valueOf(mapa.get(dataSnapshot.getKey())));
                    Log.i(TAG, String.valueOf(changedStory.getIntentos()));
                    if (mapa.get(dataSnapshot.getKey()) < changedStory.getIntentos()) {
                        // muestra notificacion
                        Log.i(TAG, "Mostrar notificacion de " + changedStory.getNombre());

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                        mBuilder.setSmallIcon(R.drawable.notifications);
                        mBuilder.setContentTitle("Adivinaron tu historia!");
                        mBuilder.setContentText("Tu historia ha sido adivinada");
                        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);


                        Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                        intent.putExtra("uID", dataSnapshot.getKey());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setAutoCancel(true); //Remueve la notificación cuando se toca

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        // notificationId es un entero unico definido para cada notificacion que se lanza
                        notificationManager.notify(notificationId, mBuilder.build());
                        notificationId++;
                    }
                    mapa.put(dataSnapshot.getKey(), changedStory.getIntentos());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onChildRemoved: ");
            }


            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.i(TAG, "onChildMoved: ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: ");
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        Log.i(TAG, "Entre a createNotificationChannel Service");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //IMPORTANCE_MAX MUESTRA LA NOTIFICACIÓN ANIMADA
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        myKey = intent.getStringExtra("myKey");
        createNotificationChannel();
        Log.i(TAG, "Entre a onHandleIntent Notification Service");
        start();
    }
}
