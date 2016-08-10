package com.infantil.nomaltrato;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Recover_Password extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover__password);

        Button recuperar=(Button)findViewById(R.id.btn_recuperar);
        recuperar.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Toast toast= Toast.makeText(getApplicationContext(),"Se ha enviado a su correo un link de restablecimiento de contraseña",Toast.LENGTH_LONG);
        toast.show();
    }
}
