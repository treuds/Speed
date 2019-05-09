package com.ciayiti.speed;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ciayiti.speed.Localisation.ServiceGPS;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private BroadcastReceiver broadcastReceiver;
    double LongitudeActuelle;
    double LatitudeActuelle;
    private Button newTrajet=null;
    private Spinner mlignes=null;
    private Button deconnexion=null;
    Spinner type_vehicule=null;
    EditText capacite_vehi=null;
    EditText prix_course=null;
    EditText nom_chauffeur=null;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseTrajet=FirebaseDatabase.getInstance().getReference("trajets");
    public static final String IDTRAJET="com.ciayiti.speed";


    @Override
    protected void onCreate(Bundle savedInstanceState) {



            mAuth = FirebaseAuth.getInstance();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            capacite_vehi= (EditText) this.findViewById(R.id.capacite);
            prix_course= (EditText) this.findViewById(R.id.prix_course);
            nom_chauffeur= (EditText) this.findViewById(R.id.nom_chauffeur);

    }

    @Override
    protected void onStart() {

        mlignes = (Spinner) findViewById(R.id.spinner);
        List<String> lignes = new ArrayList<String>();
        lignes.add("Gerald-Delmas 33");
        lignes.add("Carrefour aeroport-Petion-Ville");
        lignes.add("Delmas 75-Puit blain");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lignes);
        mlignes.setAdapter(adapter);
        type_vehicule= (Spinner) findViewById(R.id.Type_vehi);
        final ArrayAdapter<CharSequence> adapter2 =ArrayAdapter.createFromResource(this,R.array.vehicle_array,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newTrajet = (Button) findViewById(R.id.trajet);
        type_vehicule.setAdapter(adapter2);
        if(!runtime_permissions())
        {   if(!ServiceActif(ServiceGPS.class))
        {
            Intent bnbn=new Intent(getApplicationContext(), ServiceGPS.class);
            startService(bnbn);
            Actions();
        }
        else{
            Actions();
        }
        }


        deconnexion = (Button) findViewById(R.id.Deconnexion);
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent in = new Intent(MainActivity.this, Connexion.class);
                startActivity(in);
            }
        });

        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED)
            {
                Actions();
            }
            else{
                runtime_permissions();
            }
        }
    }

    private void Actions() {
        newTrajet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar d= Calendar.getInstance();
                Date today=d.getTime();
                java.text.SimpleDateFormat Formatter= new java.text.SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                String ins=Formatter.format(today);
                int capa;
                double prix;
                try {
                    capa=Integer.parseInt(capacite_vehi.getText().toString());
                    prix=Double.parseDouble(prix_course.getText().toString());
                }catch (NumberFormatException e){
                    capa=0;
                    prix=0;
                }
                String Identification_chauf=nom_chauffeur.getText().toString();

                String selec=mlignes.getSelectedItem().toString();
                String type_v=type_vehicule.getSelectedItem().toString();
                Trajet t=new Trajet(getmAuth().getCurrentUser().getUid(),selec,type_v, capa,prix,Identification_chauf,ins, Arrays.asList(new Double[]{LatitudeActuelle,LongitudeActuelle}));
                Toast.makeText(MainActivity.this,t.toString(),
                        Toast.LENGTH_SHORT).show();
                String Identification_trajet=databaseTrajet.push().getKey();
                databaseTrajet.child(Identification_trajet).setValue(t);
                Intent infTrajet = new Intent(MainActivity.this, InformationsTrajet.class);
                infTrajet.putExtra(IDTRAJET,Identification_trajet);
                startActivity(infTrajet);

            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(broadcastReceiver==null){
            broadcastReceiver=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    LongitudeActuelle=intent.getDoubleExtra("Longitude",0);
                    LatitudeActuelle=intent.getDoubleExtra("Latitude",0);

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(broadcastReceiver!=null)
        {
            unregisterReceiver(broadcastReceiver);
        }
    }

    //cheke si sevis la aktive oubyen non
    public boolean ServiceActif(Class<?> classeService){
        ActivityManager manager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service:manager.getRunningServices(Integer.MAX_VALUE)){
            if(classeService.getName().equals(service.service.getClassName())){
                return true;}
        }
        return false;
    }

}


