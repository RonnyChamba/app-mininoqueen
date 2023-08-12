package com.app.mininoqueen;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.app.mininoqueen.databinding.ActivityMainBinding;
import com.app.mininoqueen.modelos.AuthCredential;
import com.app.mininoqueen.modelos.Usuario;
import com.app.mininoqueen.util.DataCard;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private FirebaseFirestore db = null;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        context = this;

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
                    verifyCodeIntermediario(txtCodeInterno.getText().toString());
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


    private void verifyCodeIntermediario(String codeIntermediario) {


        // buscar el intermediario

        db.collection("usuarios").whereEqualTo("codigo", codeIntermediario).get().addOnSuccessListener(
                documentSnapshot -> {
                    if (!documentSnapshot.isEmpty()) {
                        if (documentSnapshot.getDocuments().size() > 0) {

                            Usuario usuario = documentSnapshot.getDocuments().get(0).toObject(Usuario.class);
                            if (usuario != null) {

                                // guardar el usuario intermedio
                                DataCard.usuario = usuario;
                                Intent signInIntent = new Intent(this, HomeActivity.class);
                                startActivity(signInIntent);
                                //Toast.makeText(context, "Bienvenido " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
                                loginDialogo.dismiss();

                            } else {
                                loginDialogo.dismiss();
                                Toast.makeText(context, "No se pudo obtener el intermediario", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            loginDialogo.dismiss();
                            Toast.makeText(context, "No se pudo obtener el intermediario", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(context, "Intermediario no registrado", Toast.LENGTH_SHORT).show();
                }
        );

    }
}