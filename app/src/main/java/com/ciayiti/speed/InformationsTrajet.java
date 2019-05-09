package com.ciayiti.speed;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ciayiti.speed.Localisation.ServiceGPS;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class InformationsTrajet extends Activity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    public double LongitudeActuelle;
    public double LatitudeActuelle;
    private FirebaseAuth mAuth;
    private BroadcastReceiver broadcastReceiver;
    DatabaseReference arretRef=database.getReference("arret");
    private DatabaseReference trajetRef=FirebaseDatabase.getInstance().getReference("trajets");
    private DatabaseReference ligneRef=FirebaseDatabase.getInstance().getReference("lignecontrole");

    String identificationTrajet;
    String idEnqueteur;

    private Button newArret=null;
    public Button FinTrajet=null;
    EditText nbrePersDepart;
    EditText NoteTrajet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i=getIntent();
        identificationTrajet=i.getStringExtra(MainActivity.IDTRAJET);
         mAuth=FirebaseAuth.getInstance();
         idEnqueteur=mAuth.getCurrentUser().getUid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations_trajet);
        nbrePersDepart=(EditText) this.findViewById(R.id.pers);
        NoteTrajet=(EditText) this.findViewById(R.id.Notes);
        newArret=(Button)findViewById(R.id.nArret);
        FinTrajet=(Button)findViewById(R.id.ftrajet);
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

    }

    private void Actions() {
        newArret.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final Arret a=new Arret();
                final Calendar calendrier= Calendar.getInstance();
                Date maintenant=calendrier.getTime();
                java.text.SimpleDateFormat Formatter= new java.text.SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                String Heure_debut=Formatter.format(maintenant);
                a.setIdentificationtrajet(identificationTrajet);
                a.setCoordarr(Arrays.asList(new Double[]{LatitudeActuelle,LongitudeActuelle}));
                a.setHeuredebut(Heure_debut);
                final AlertDialog.Builder mbuil= new AlertDialog.Builder(InformationsTrajet.this);
                View mview=getLayoutInflater().inflate(R.layout.activity_informations_arret,null);
                final EditText nbrePM=(EditText) mview.findViewById(R.id.persM);
                final EditText nbrePD=(EditText) mview.findViewById(R.id.persD);
                Button ok=(Button)mview.findViewById(R.id.ok);
                mbuil.setView(mview);
                final AlertDialog dialog=mbuil.create();
                dialog.show();
                ok.setOnClickListener(new View.OnClickListener(){
                    public void onClick( View view){
                        if(!nbrePM.getText().toString().isEmpty() && !nbrePD.getText().toString().isEmpty()){
                            Date maintenant=calendrier.getTime();
                            java.text.SimpleDateFormat Formatter= new java.text.SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                            String Heure_fin=Formatter.format(maintenant);
                            int M=Integer.parseInt( nbrePM.getText().toString());
                            int D=Integer.parseInt(nbrePD.getText().toString());
                            a.setPersdes(D);
                            a.setPersmon(M);
                            a.setHeurefin(Heure_fin);
                            String cle=arretRef.push().getKey();
                            arretRef.child(cle).setValue(a);
                            Toast.makeText(InformationsTrajet.this, "Arret enregistre.",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                        }

                    }
                });

            }

        });


        FinTrajet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!nbrePersDepart.getText().toString().isEmpty()){

                    Calendar d= Calendar.getInstance();
                    Date today=d.getTime();
                    java.text.SimpleDateFormat Formatter= new java.text.SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                    String ins=Formatter.format(today);
                    trajetRef.child(identificationTrajet).child("Heure_arv").setValue(ins);
                    trajetRef.child(identificationTrajet).child("Coord_arv").setValue(LongitudeActuelle);
                    trajetRef.child(identificationTrajet).child("Nbre_persD").setValue(Integer.parseInt(nbrePersDepart.getText().toString()));
                    if(!NoteTrajet.getText().toString().isEmpty()){

                        trajetRef.child(identificationTrajet).child("commentaires").setValue(NoteTrajet.getText().toString());
                    }
                    else{
                        trajetRef.child(identificationTrajet).child("commentaires").setValue(" ");
                    }

                    Toast.makeText(InformationsTrajet.this, "Trajet mis a jour.",
                            Toast.LENGTH_SHORT).show();
                    Intent recommence=new Intent(InformationsTrajet.this,MainActivity.class);
                    startActivity(recommence);
                }
                else{
                        Toast.makeText(InformationsTrajet.this, "Indiquez le nombre de personnes au depart",Toast.LENGTH_SHORT).show();
                    }
                }

            });

    }


    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
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
