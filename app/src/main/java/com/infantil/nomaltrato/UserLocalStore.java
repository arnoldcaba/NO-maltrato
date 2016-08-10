package com.infantil.nomaltrato;

import android.content.Context;
import android.content.SharedPreferences;



/**
 * Created by ArnoldGustavo on 24/09/2015.
 */
public class UserLocalStore {

    public static final String SP_NAME="userDetails";
    SharedPreferences UserLocalDatabase;

    public UserLocalStore(Context context){
        UserLocalDatabase=context.getSharedPreferences(SP_NAME,0);
    }


    public void storeUserData(User user){
        SharedPreferences.Editor spEditor= UserLocalDatabase.edit();
        spEditor.putString("nombre",user.nombre);
        spEditor.putString("celular",user.celular);
        spEditor.putString("email",user.email);
        spEditor.putString("password",user.password);
        spEditor.commit();
    }

    public User getLoggedInUser(){
        String nombre=UserLocalDatabase.getString("nombre", "");
        String celular=UserLocalDatabase.getString("celular","");
        String email=UserLocalDatabase.getString("email","");
        String password=UserLocalDatabase.getString("password","");

        User storedUser=new User(nombre,celular,email,password);

        return storedUser;
    }

    public void setUserLoggedIn(Boolean loggedIn){
        SharedPreferences.Editor spEditor= UserLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor= UserLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if (UserLocalDatabase.getBoolean("loggedIn",false)==true){
            return true;
        }else {
            return false;
        }

    }
}
