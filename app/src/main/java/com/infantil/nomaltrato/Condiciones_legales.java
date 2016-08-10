package com.infantil.nomaltrato;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Condiciones_legales extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condiciones_legales);
    }

    public void aceptar(View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        this.finish();
    }


}
