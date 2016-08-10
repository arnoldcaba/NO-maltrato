package com.infantil.nomaltrato;

/**
 * Created by ArnoldGustavo on 24/09/2015.
 */
public class User {

    String nombre, celular, email, password;


    public User(String nombre, String celular, String email, String password){

        this.nombre=nombre;
        this.celular=celular;
        this.email=email;
        this.password=password;
    }
    public  User(String email, String password){

        this.email=email;
        this.password=password;
        this.celular="";
        this.nombre="";
    }
}
