package com.app.mininoqueen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.mininoqueen.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    private EditText txtDocument;

    AlertDialog loginDialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSigIn.setOnClickListener(this);

        txtDocument = binding.txtEmail;

    }

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

        loginDialogo = createLoginDialogo();

        loginDialogo.show();

        //Intent signInIntent = new Intent(this, HomeActivity.class);
        //startActivity(signInIntent);
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
        Button signin = (Button) v.findViewById(R.id.entrar_boton);

        signin.setOnClickListener(
                v1 -> {

                    Intent signInIntent = new Intent(this, HomeActivity.class);
                    startActivity(signInIntent);
                    loginDialogo.dismiss();
                }

        );

        return builder.create();
    }
}