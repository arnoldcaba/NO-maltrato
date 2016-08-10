package com.infantil.nomaltrato;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ArnoldGustavo on 09/08/2015.
 */
public class Usuario_Registro extends Activity {
    UserLocalStore userLocalStore;
    EditText etnombre,etcelular,etemail,etpassword,etpassword2;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);

        etnombre= (EditText) findViewById(R.id.txt_nombre);
        etcelular= (EditText) findViewById(R.id.txt_celular);
        etemail= (EditText) findViewById(R.id.txt_email);
        etpassword= (EditText) findViewById(R.id.txt_password);
        etpassword2=(EditText) findViewById(R.id.txt_password2);
        userLocalStore=new UserLocalStore(this);
    }

    @Override
    protected void onStart(){
    super.onStart();
        if (authenticate()==true){
            Intent i = new Intent(this,CrearReporte.class);
            startActivity(i);
            this.finish();
        }
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    public void inicio(View view){
        Intent i = new Intent(this,Usuario_Ingreso.class);
        startActivity(i);
    }

    public void registro(View view){
        String nombre= etnombre.getText().toString();
        String celular= etcelular.getText().toString();
        String email= etemail.getText().toString();
        String password= etpassword.getText().toString();
        String password2= etpassword2.getText().toString();

        String validacion=validate(email,celular,password,password2);
        if (validacion=="ok"){
            user=new User(nombre,celular,email,password);
            ValidateEmail(user);
        } else {
            Toast toast= Toast.makeText(getApplicationContext(),validacion,Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    private void ValidateEmail(User inuser){
        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.validateEmailDataInBackground(inuser, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    registerUser(user);
                } else {
                    showErrorMessage();
                }
            }
        });
    }

    private void registerUser(User inuser){

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.storeUserDataInBackground(inuser, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                userLocalStore.storeUserData(user);
                userLocalStore.setUserLoggedIn(true);
                startActivity(new Intent(Usuario_Registro.this, CrearReporte.class));
                Usuario_Registro.this.finish();
            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogeBuilder= new AlertDialog.Builder(Usuario_Registro.this);
        dialogeBuilder.setMessage("Email ya registrado");
        dialogeBuilder.setPositiveButton("OK",null);
        dialogeBuilder.show();
    }

    private String validate(String email, String celular,String password, String password2){
        MailValidator validacion=new MailValidator();
        if (validacion.validateEmail(email)) {
            if (celular.length()==10){
                if (password.equals(password2)){
                    if(password.length()>=7){
                        return "ok";
                    }else {
                        return "Contraseña demasiado corta";
                    }
                }else{
                    return "Contraseñas no coinciden";
                }
            }else {
                return "El numero celular debe tener 10 dígitos";
            }
        }else {
            return "Formato email no valido";
        }
    }
}
