package com.ciayiti.speed;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import static com.google.android.gms.internal.zzt.TAG;

public class Connexion extends Activity {
    private FirebaseAuth mAuth;
    private Button Connexion=null;
    private EditText Email=null;
    private EditText Pass=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);


        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent;
        if(currentUser !=null){
            String iduser=currentUser.getUid();
            intent=new Intent(Connexion.this,MainActivity.class);
        }
        else{
            Connexion=(Button)findViewById(R.id.connexion);
            Email=(EditText)findViewById(R.id.editText2);
            Pass=(EditText)findViewById(R.id.editText3);
            Connexion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn(Email.getText().toString(),Pass.getText().toString());
                }
            });
        }


    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent;
                            intent=new Intent(Connexion.this,MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Connexion.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {

                        }

                        // [END_EXCLUDE]
                    }
                });
    }
}
