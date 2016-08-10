package com.infantil.nomaltrato;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ArnoldGustavo on 09/08/2015.
 */
public class Usuario_Ingreso extends Activity {

    TextView etemail,etpassword;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso_usuario);
        etemail=(TextView) findViewById(R.id.txt_email);
        etpassword=(TextView) findViewById(R.id.txt_password);
    }

    public void iniciar(View view){

        String email= etemail.getText().toString();
        String password= etpassword.getText().toString();

        user=new User(email,password);

        authenticate(user);


    }

    private void authenticate(User user){
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });


    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogeBuilder= new AlertDialog.Builder(Usuario_Ingreso.this);
        dialogeBuilder.setMessage("datos incorrectos");
        dialogeBuilder.setPositiveButton("OK",null);
        dialogeBuilder.show();

    }

    private void logUserIn(User returnedUser){
        UserLocalStore userLocalStore=new UserLocalStore(this);
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this, CrearReporte.class));
        this.finish();
    }

    public void recuperar(View view){
        startActivity(new Intent(this, Recover_Password.class));

    }
}
