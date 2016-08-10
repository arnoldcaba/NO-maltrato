package com.infantil.nomaltrato;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by ArnoldGustavo on 27/09/2015.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT=1000 * 15;
    public static final String SERVER_ADDRESS="http://arklan.host56.com/";

    public ServerRequests(Context context){
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("please wait...");
    }

    public void validateEmailDataInBackground(User user,GetUserCallback userCallback){
        progressDialog.show();
        new validateEmailDataAsyncTask(user, userCallback).execute();
    }

    public void storeUserDataInBackground(User user,GetUserCallback usercallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, usercallback).execute();
    }

    public void fetchUserDataInBackground(User user,GetUserCallback callback){
        progressDialog.show();
        new fetchUserDataAsyncTask(user,callback).execute();
    }

    public void storeReportDataInBackground(Report report,GetReportCallback reportCallback){
        progressDialog.show();
        new StoreReportDataAsyncTask(report, reportCallback).execute();
    }

    public class validateEmailDataAsyncTask extends AsyncTask<Void, Void, User >{
        User user;
        GetUserCallback userCallback;
        public validateEmailDataAsyncTask(User inuser,GetUserCallback usercallback){
            this.user=inuser;
            this.userCallback=usercallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email", user.email));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client= new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"Validate.php");

            User returnedUser=null;

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse=client.execute(post);

                HttpEntity entity=httpResponse.getEntity();
                String result= EntityUtils.toString(entity);
                JSONObject jObject= new JSONObject(result);

                if (jObject.length()==0){
                    returnedUser=null;
                }else{
                    String nombre =jObject.getString("nombre");
                    String celular =jObject.getString("celular");
                    String password =jObject.getString("password");

                    returnedUser=new User(nombre,celular,user.email,password);
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }


    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void >{

        User user;
        GetUserCallback userCallback;
        public StoreUserDataAsyncTask(User inuser,GetUserCallback usercallback){
            this.user=inuser;
            this.userCallback=usercallback;
        }
        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("nombre", user.nombre));
            dataToSend.add(new BasicNameValuePair("celular", user.celular));
            dataToSend.add(new BasicNameValuePair("email", user.email));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client= new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"Register.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void,Void,User >{

        User user;
        GetUserCallback userCallback;
        public fetchUserDataAsyncTask(User inuser,GetUserCallback usercallback){
            this.user=inuser;
            this.userCallback=usercallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend=new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email", user.email));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client= new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"FetchUserData.php");

            User returnedUser=null;

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse=client.execute(post);

                HttpEntity entity=httpResponse.getEntity();
                String result= EntityUtils.toString(entity);
                JSONObject jObject= new JSONObject(result);

                if (jObject.length()==0){
                    returnedUser=null;
                }else{
                    String nombre =jObject.getString("nombre");
                    String celular =jObject.getString("celular");

                    returnedUser=new User(nombre,celular,user.email,user.password);
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

    public class StoreReportDataAsyncTask extends AsyncTask<Void, Void, Void >{
        Report report;
        GetReportCallback reportCallback;
        public StoreReportDataAsyncTask(Report inreport,GetReportCallback reportCallback){
            this.report=inreport;
            this.reportCallback=reportCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            String encodedImage;
            if (report.foto==null){
                encodedImage="sin foto";
            }else {
            report.foto.compress(Bitmap.CompressFormat.PNG, 100,byteArrayOutputStream);
            encodedImage= Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
            }

            ArrayList<NameValuePair> dataToSend=new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("email", report.email));
            dataToSend.add(new BasicNameValuePair("fecha", report.fecha));
            dataToSend.add(new BasicNameValuePair("longitud", report.longitud));
            dataToSend.add(new BasicNameValuePair("latitud", report.latitud));
            dataToSend.add(new BasicNameValuePair("des_ubicacion", report.des_ubicacion));
            dataToSend.add(new BasicNameValuePair("foto",encodedImage));
            dataToSend.add(new BasicNameValuePair("entes_control",report.entes_control));
            dataToSend.add(new BasicNameValuePair("des_reporte", report.des_reporte));
            dataToSend.add(new BasicNameValuePair("nom_victima",report.nom_victima));
            dataToSend.add(new BasicNameValuePair("nom_agresor",report.nom_agresor));
            dataToSend.add(new BasicNameValuePair("r_victima_victimario", report.r_victima_victimario));
            dataToSend.add(new BasicNameValuePair("fisico",report.fisico));
            dataToSend.add(new BasicNameValuePair("emocional", report.emocional));
            dataToSend.add(new BasicNameValuePair("fetal",report.fetal));
            dataToSend.add(new BasicNameValuePair("sexual",report.sexual));
            dataToSend.add(new BasicNameValuePair("abandono", report.abandono));



            HttpParams httpRequestParams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client= new DefaultHttpClient(httpRequestParams);
            HttpPost post=new HttpPost(SERVER_ADDRESS+"Report.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            reportCallback.done(null);
            super.onPostExecute(aVoid);
        }


    }
}
