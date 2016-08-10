package com.infantil.nomaltrato;

import android.graphics.Bitmap;

/**
 * Created by ArnoldGustavo on 04/10/2015.
 */
public class Report {
    String email,fecha,longitud, latitud,des_ubicacion,des_reporte,nom_victima, nom_agresor, r_victima_victimario,fisico, emocional, fetal, sexual,abandono;
    String entes_control;
    Bitmap foto;
    public Report(String email,String fecha, String longitud, String latitud,String des_ubicacion,Bitmap foto,String des_reporte, String entes_control,String nom_victima, String nom_agresor,String r_victima_victimario,String fisico, String emocional, String fetal, String sexual,String abandono){
        this.email=email;
        this.fecha=fecha;
        this.longitud=longitud;
        this.latitud=latitud;
        this.des_ubicacion=des_ubicacion;
        this.foto=foto;
        this.des_reporte=des_reporte;
        this.entes_control=entes_control;
        this.nom_victima=nom_victima;
        this.nom_agresor=nom_agresor;
        this.r_victima_victimario=r_victima_victimario;
        this.fisico=fisico;
        this.emocional=emocional;
        this.fetal=fetal;
        this.sexual=sexual;
        this.abandono=abandono;
    }
}
