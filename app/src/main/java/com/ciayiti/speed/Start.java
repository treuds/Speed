package com.ciayiti.speed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;
        if (user != null) {
            // User is signed in
            String iduser=user.getUid();
            intent=new Intent(Start.this,MainActivity.class);
        } else {
            // No user is signed in
            intent=new Intent(Start.this,Connexion.class);
        }
        startActivity(intent);
        finish();
    }
}
