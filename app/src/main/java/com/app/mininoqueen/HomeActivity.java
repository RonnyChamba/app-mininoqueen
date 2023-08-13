package com.app.mininoqueen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mininoqueen.databinding.ActivityHomeBinding;
import com.app.mininoqueen.modelos.Cliente;
import com.app.mininoqueen.modelos.Usuario;
import com.app.mininoqueen.util.DataCard;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    private Context context;

    private FirebaseFirestore db = null;

    private FirebaseUser user;

    private static final String COLLECTION_NAME = "clientes";

    private Cliente cliente;

    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        setSupportActionBar(binding.appBarHome.toolbar);

        db = FirebaseFirestore.getInstance();
        setSupportActionBar(binding.appBarHome.toolbar);
//        binding.appBarHome.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());
//
        binding.appBarHome.fab.setVisibility(View.GONE);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_pedidos, R.id.nav_cuidado_personal)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        verifySignIn();
    }

    private void setValueDefault() {

        // Acceder al layout nav_header_home
        View navHeader = binding.navView.getHeaderView(0);

        // Acceder a los widgets dentro de nav_header_home
        TextView fullNameUser = navHeader.findViewById(R.id.textViewFullName);
        TextView emailUser = navHeader.findViewById(R.id.textViewEmail);
        fullNameUser.setText(cliente.getNombre());
        emailUser.setText(cliente.getDocumento());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void verifySignIn() {

        user = FirebaseAuth.getInstance().getCurrentUser();

        // verificar si el usuario esta logueado
        if (user != null) {

            String uidUser = user.getUid();

            String documento = Objects.requireNonNull(user.getEmail()).substring(0, user.getEmail().indexOf("@"));

            // buscar el cliente
            db.collection(COLLECTION_NAME).whereEqualTo("documento", documento).get().addOnSuccessListener(
                    documentSnapshot -> {
                        if (!documentSnapshot.isEmpty()) {
                            if (documentSnapshot.getDocuments().size() > 0) {

                                cliente = documentSnapshot.getDocuments().get(0).toObject(Cliente.class);
                                if (cliente != null) {
                                    setValueDefault();
                                    DataCard.cliente = cliente;

                                } else {
                                    Toast.makeText(context, "No se pudo obtener el cliente", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "No se pudo obtener el cliente", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
            );

        } else {

            // si no esta logueado, redirigir al login
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_view_cart) {
            navController.navigate(R.id.nav_review_card);
        }

        return super.onOptionsItemSelected(item);
    }
}