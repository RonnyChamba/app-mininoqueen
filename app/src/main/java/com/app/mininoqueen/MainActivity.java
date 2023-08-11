package com.app.mininoqueen;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.app.mininoqueen.databinding.ActivityMainBinding;
import com.app.mininoqueen.modelos.AuthCredential;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    private EditText txtDocument;

    AlertDialog loginDialogo;

    ProgressBar loadingProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSigIn.setOnClickListener(this);

        txtDocument = binding.txtEmail;
        loadingProgressBar = binding.loading;


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            //Toast.makeText(this, "Usuario logeado", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onStart: " + currentUser.getDisplayName());
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
//
//        } else {
//
//            //Toast.makeText(this, "Usuario No logeado", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onStart: No logeado");
//        }
//
//
//    }

    @Override
    public void onClick(View v) {

        if (v.getId() == binding.btnSigIn.getId()) {
            signIn();
        }
    }

    private void signIn() {


        if (!validInput()) {
            return;
        }

        String document = txtDocument.getText().toString();
        // aqui comparar con la base de datos

        String email = document + "@gmail.com";
        String password = "123456";

        signIn(new AuthCredential(email, password));


    }

    private boolean validInput() {

        String document = txtDocument.getText().toString();

        if (document.isEmpty()) {
            txtDocument.setError("El documento es requerido");
            return false;
        }

        if (document.length() != 10) {
            txtDocument.setError("El documento  debe tener 10 dígitos");
            return false;
        }
        return true;
    }

    public AlertDialog createLoginDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.layout_modal, null);
        builder.setView(v);

        EditText txtCodeInterno = (EditText) v.findViewById(R.id.code_intermediario);


        Button signin = (Button) v.findViewById(R.id.entrar_boton);
        Button cancelar = (Button) v.findViewById(R.id.cancelar_boton);

        signin.setOnClickListener(
                v1 -> {
                    if (txtCodeInterno.getText().toString().isEmpty()) {
                        txtCodeInterno.setError("El código es requerido");
                        return;
                    }

                    Intent signInIntent = new Intent(this, HomeActivity.class);
                    startActivity(signInIntent);
                    loginDialogo.dismiss();
                }

        );

        cancelar.setOnClickListener(
                v1 -> {

                    FirebaseAuth.getInstance().signOut();
                    loginDialogo.dismiss();
                }

        );

        return builder.create();
    }

    private void signIn(AuthCredential authCredential) {

        mAuth.signInWithEmailAndPassword(authCredential.getEmail(), authCredential.getPassword())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SignIn", "signInWithEmail:success");
                        loginDialogo = createLoginDialogo();
                        loginDialogo.setCanceledOnTouchOutside(false);

                        loginDialogo.show();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("SignIn", "signInWithEmail:failure", task.getException());

                        Toast.makeText(getBaseContext(), "Error, documento incorrecto ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}