package com.felipe.levez.listadefilmes.activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.felipe.levez.listadefilmes.R;
import com.felipe.levez.listadefilmes.fragments.ListaFilmesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback  {


    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private static final String ARG_TIPO_LISTA = "ARG_TIPO_LISTA";
    private static final int ARG_TIPO_BUSCA = 0;
    private static final int ARG_TIPO_FAV = 1;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
    private Bundle saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.saved = savedInstanceState;
        checkPermissions();

    }
    private void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<>();
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            final String[] permissions = missingPermissions
                    .toArray(new String[0]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }


    private void setupOnCreate(){
        setContentView(R.layout.activity_main);
        setupFindViewbyIds();
        setupToobar();
        setupNavigationDrawer();
        if(saved == null){
            inflaFragmentComArg(ARG_TIPO_BUSCA);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        alertDialogMsgErroPermissao();
                        return;
                    }
                }
                setupOnCreate();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alertDialogMsgErroPermissao(){
        new AlertDialog.Builder(this).setTitle(R.string.msg_titulo_permissao_negada).
                setMessage(R.string.msg_permissao_negada)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void setupFindViewbyIds(){
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
    }

    private void setupToobar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Lista de Favoritos");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.drawer_buscar:
                inflaFragmentComArg(ARG_TIPO_BUSCA);
                break;

            case R.id.drawer_favoritos:
                inflaFragmentComArg(ARG_TIPO_FAV);
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupNavigationDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.abrir_drawer, R.string.fechar_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void inflaFragmentComArg(int tipo_lista){
        ListaFilmesFragment frag = ListaFilmesFragment.newInstance();
        Bundle arg = new Bundle();
        arg.putInt(ARG_TIPO_LISTA, tipo_lista);
        frag.setArguments(arg);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

}
