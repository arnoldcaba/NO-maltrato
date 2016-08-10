package com.infantil.nomaltrato;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.Date;


public class CrearReporte extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    ImageView imagen, temp;
    Button captura;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_LOAD_IMAGE=1;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private LocationRequest mLocationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    Spinner spinner;
    String email, fecha, longitud, latitud, des_ubicacion, entes_control, des_reporte,m_fisico,m_emocional,m_fetal,m_sexual,m_abandono, nom_victima, nom_agresor, r_victima_victimario;
    Bitmap foto;
    int a=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reporte);
        User usuario;
        UserLocalStore consulta=new UserLocalStore(this);
        usuario=consulta.getLoggedInUser();

        String nom=usuario.nombre;
        email=usuario.email;

        TextView textView= (TextView) findViewById(R.id.currentuser);
        textView.setText("Bienvenido: "+nom);


        imagen = (ImageView) findViewById(R.id.imageView);
        captura = (Button) findViewById(R.id.btn_obtfoto);

        captura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 0);
            }
        });


        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);

        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null && a==2){
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 390, 693, true);
                imagen.setImageBitmap(bMapScaled);
                foto=bMapScaled;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Bitmap bm= (Bitmap) data.getExtras().get("data");
            Bitmap bMapScaled = Bitmap.createScaledBitmap(bm, 468, 832, true);
            foto=bMapScaled;
            imagen.setImageBitmap(bm);

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
        else {
            handleNewLocation(location);
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng (currentLatitude, currentLongitude)).zoom(16).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        longitud=Double.toString(currentLongitude);
        latitud=Double.toString(currentLatitude);


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");

        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void reportar(View view){

        tomardatos();
        Report report=new Report(email,fecha,longitud,latitud,des_ubicacion,foto,des_reporte,entes_control, nom_victima,nom_agresor, r_victima_victimario,m_fisico,m_emocional,m_fetal,m_sexual,m_abandono);

        ServerRequests serverRequests=new ServerRequests(this);
        serverRequests.storeReportDataInBackground(report, new GetReportCallback() {
            @Override
            public void done(Report returnedReport) {
                Toast.makeText(getApplicationContext(), "Reporte exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CrearReporte.this, MainActivity.class));
                CrearReporte.this.finish();
            }
        });


    }

    public void pick_photo(View view){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        //Intent galleryIntent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        a=2;
        //startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            this.finish();
            startActivity(new Intent(CrearReporte.this, MainActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }

    public void tipo(View view){
        LinearLayout ly_opcionales= (LinearLayout) findViewById(R.id.ly_opcionales);
        if (ly_opcionales.getVisibility()==View.VISIBLE) {
            ly_opcionales.setVisibility(View.GONE);
        }else {
            ly_opcionales.setVisibility(View.VISIBLE);
        }


    }

    public void relacion(View view){
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.datos_adicionales);
        if (linearLayout.getVisibility()==View.VISIBLE) {
            linearLayout.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.VISIBLE);
            spinner=(Spinner)findViewById(R.id.spiner);
            ArrayAdapter<CharSequence> adapter;
            adapter=ArrayAdapter.createFromResource(this, R.array.Parentesco,android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            spinner.setAdapter(adapter);
        }
    }



    private void tomardatos(){

        Date date=new Date();
        fecha=date.toString();

        TextView descripcion_ubicacion= (TextView) findViewById(R.id.descripcion_dir);
        des_ubicacion=descripcion_ubicacion.getText().toString();

        CheckBox control= (CheckBox) findViewById(R.id.checkBox);
        if(control.isChecked()){
            entes_control="1";
        }else {
            entes_control="0";
        }

        TextView descripcion_reporte= (TextView) findViewById(R.id.txt_detalle);
        des_reporte=descripcion_reporte.getText().toString();

        //seleccionando tipo de maltrato
        CheckBox fisico,emocional,fetal,sexual,abandono;

        fisico=(CheckBox)findViewById(R.id.fisico);
        emocional=(CheckBox)findViewById(R.id.emocional);
        fetal=(CheckBox)findViewById(R.id.fetal);
        sexual=(CheckBox)findViewById(R.id.sexual);
        abandono=(CheckBox)findViewById(R.id.abandono);

        if (fisico.isChecked()){
            m_fisico="1";
        }
        if (emocional.isChecked()){
           m_emocional="2";
        }
        if (fetal.isChecked()){
            m_fetal="3";
        }
        if (sexual.isChecked()){
            m_sexual="4";
        }
        if (abandono.isChecked()){
            m_abandono="5";
        }

        EditText nom_vic=(EditText)findViewById(R.id.nom_victima);
        nom_victima=nom_vic.getText().toString();

        EditText nom_agr=(EditText)findViewById(R.id.nom_agresor);
        nom_agresor=nom_agr.getText().toString();

        r_victima_victimario= spinner.getSelectedItem().toString();
    }

}
