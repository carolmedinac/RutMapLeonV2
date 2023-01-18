package com.example.rutmapleon;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rutmapleon.clases.Rutasdibujar;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class home_fragment extends Fragment implements OnMapReadyCallback {


    // Estado del Settings de verificación de permisos del GPS
    private static final int REQUEST_CHECK_SETTINGS = 102;

    // La clase FusedLocationProviderClient
    private FusedLocationProviderClient fusedLocationClient;

    // La clase LocationCallback se utiliza para recibir notificaciones de FusedLocationProviderApi
    // cuando la ubicación del dispositivo ha cambiado o ya no se puede determinar.
    private LocationCallback mlocationCallback;

    // La clase LocationSettingsRequest.Builder extiende un Object
    // y construye una LocationSettingsRequest.
    private LocationSettingsRequest.Builder builder;

    // La clase LocationRequest sirve para  para solicitar las actualizaciones
    // de ubicación de FusedLocationProviderApi
    public LocationRequest mLocationRequest;

    // Marcador para la ubicación del usuario
    Marker markerusuario;

    // Mapa de Google
    private GoogleMap mMap;

    String latitude, longitude;
    String correo,rol;
    int id,posicionspinner,posicionanterior=0;
    Handler handler;
    List<Polyline> polyline_final;
    List<Marker> marker;
    Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        Rutasdibujar rutasdibujar = new Rutasdibujar();
        
        polyline_final = new ArrayList<Polyline>();
        marker = new ArrayList<Marker>();
        // Inicializamos la vista
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        spinner = view.findViewById(R.id.spinner);
        spinner.setSelection(0);
        posicionanterior=spinner.getSelectedItemPosition();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(),Integer.toString(position+1),Toast.LENGTH_SHORT).show();
                switch (position) {
                   case 0:
                        posicionspinner = spinner.getSelectedItemPosition();
                        if (!polyline_final.isEmpty()) {
                            for (Polyline line : polyline_final){
                                line.remove();
                            }
                        }
                   {
                       for (String s : rutasdibujar.getSANJERONIMO()) {
                           trazarrutas(s,Color.RED);
                       }
                       for (String s: rutasdibujar.getCAMPUSMEDICO()) {
                           trazarrutas(s,Color.BLUE);
                       }
                   }
                         return;
                    case 1:
                        posicionspinner = spinner.getSelectedItemPosition();
                        if (!polyline_final.isEmpty()){
                            for (Polyline line : polyline_final){
                                line.remove();
                            }
                        }
                          {
                              for (String s: rutasdibujar.getALTOSDEVERACRUZ()) {
                                  trazarrutas(s,Color.RED);
                              }
                              for (String M: rutasdibujar.getRPTOLOSPOETAS()) {
                                  trazarrutas(M,Color.BLUE);
                              }
                          }
                        return;

                    case 2:
                        posicionspinner = spinner.getSelectedItemPosition();
                        if (!polyline_final.isEmpty()){
                            for (Polyline line : polyline_final){
                                line.remove();
                            }
                        }
                    {
                        for (String s: rutasdibujar.getDEMOCRACIA()) {
                            trazarrutas(s,Color.RED);
                        }
                        for (String s: rutasdibujar.getPRADERASNUEVOLEON()) {
                            trazarrutas(s,Color.BLUE);
                        }
                    }
                        return;

                    case 3:
                        posicionspinner = spinner.getSelectedItemPosition();
                        if (!polyline_final.isEmpty()){
                            for (Polyline line : polyline_final){
                                line.remove();
                            }
                        }
                    {
                        for (String s: rutasdibujar.getWILLIAMFOSECA()) {
                            trazarrutas(s, Color.RED);
                        }
                        for (String s: rutasdibujar.getVILLASOBERANA()) {
                            trazarrutas(s,Color.BLUE);
                        }
                    }
                    return;

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (getContext()!=null){

        //-----------------------------------LLamamos a verificar los Datos-----------------------------------------
        correo = getArguments().getString("correo");
        validar_correo("https://rutmap.000webhostapp.com/rutmapleon/verificar_correo.php?correo="+correo);
        //----------------------------------------------------------------------------------------------------------
        }
        SupportMapFragment mapFragment =(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        fusedLocationClient =LocationServices.getFusedLocationProviderClient(getActivity());

        handler = new Handler();

        //obtenermiultimaubicacion();

        return view;
    }


    //funcion que me obtiene la ubicacion cada cierto tiempo
    public void actualizarubicacion(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getView() != null) {
                    handler.postDelayed(this, 5000);
                    getcurrentlocation();
                } else {
                    return;
                }

            }
        },5000);
    }

    public void getubicacionrutatarea(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getView()!= null) {
                    Log.d("mi tag",String.valueOf(spinner.getSelectedItemPosition()));
                    handler.postDelayed(this, 5000);
                    getubicacionruta("https://rutmap.000webhostapp.com/rutmapleon/getubicacionruta.php?id=" + posicionspinner);
                }else{
                    return;
                }
            }
        },5000);
    }

    public void getcurrentlocation(){
        mlocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {

               if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    agregarMarcador(location.getLatitude(),location.getLongitude());
                }

            };
        };

        mLocationRequest = createLocationRequest();

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        // Verificamos la configuración de los permisos de ubicación
        checkLocationSetting(builder);
    }

    public void eliminarmarker(){
        for (Marker mLocationMarker: marker) {
            mLocationMarker.remove();
        }
        marker.clear();
    }


    public void agregarmarcadorruta(double lat, double lng){
        LatLng coordenadas = new LatLng(lat, lng);
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bus);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100 , false);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(coordenadas)
                .title("Ubicacion Bus")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        Marker mLocationMarker = mMap.addMarker(markerOptions); // add the marker to Map
        marker.add(mLocationMarker);
        //mMap.animateCamera(miUbicacion);
    }


    private void agregarMarcador(double lat, double lng) {
        latitude = Double.toString(lat);
        longitude = Double.toString(lng);

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.user);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100 , false);
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 15);
        if (markerusuario != null) markerusuario.remove();
        markerusuario = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Mi ubicación")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        mMap.animateCamera(miUbicacion);
        if (  rol.equals("Usuario")) {
            getubicacionrutatarea();
           // insert_ubicacionruta("https://rutmap.000webhostapp.com/rutmapleon/insertar_ubicacion.php");
        }else if(rol.equals("Conductor De Transporte")){
            actualizar_ubicacionruta("https://rutmap.000webhostapp.com/rutmapleon/updateubicacionruta.php");
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        LatLng Leon = new LatLng(12.4378700, -86.8780400);
        float zoomlevel = (float) 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Leon, zoomlevel));
    }


    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setSmallestDisplacement(30);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void checkLocationSetting(LocationSettingsRequest.Builder builder) {

        /*
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        */

        builder.setAlwaysShow(true);

        // Dentro de la variable 'cliente' iniciamos LocationServices, para los servicios de ubicación
        SettingsClient cliente = LocationServices.getSettingsClient(getActivity());

        // Creamos una task o tarea para verificar la configuración de ubicación del usuario
        Task<LocationSettingsResponse> task = cliente.checkLocationSettings(builder.build());

        // Adjuntamos OnSuccessListener a la task o tarea
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                // Si la configuración de ubicación es correcta,
                // se puede iniciar solicitudes de ubicación del usuario
                // mediante el método iniciarActualizacionesUbicacion() que crearé más abajo.
                iniciarActualizacionesUbicacion();
                //return;
            }
        });

        // Adjuntamos addOnCompleteListener a la task para gestionar si la tarea se realiza correctamente
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {

            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // En try podemos hacer 'algo', si la configuración de ubicación es correcta,

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            // La configuración de ubicación no está satisfecha.
                            // Le mostramos al usuario un diálogo de confirmación de uso de GPS.
                            try {
                                // Transmitimos a una excepción resoluble.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;

                                // Mostramos el diálogo llamando a startResolutionForResult()
                                // y es verificado el resultado en el método onActivityResult().
                                resolvable.startResolutionForResult(
                                        getActivity(),
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignora el error.
                            } catch (ClassCastException e) {
                                // Ignorar, aca podría ser un error imposible.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Si la configuración de ubicación no está satisfecha
                            // podemos hacer algo.
                            break;
                    }
                }
            }
        });

    }

    public void iniciarActualizacionesUbicacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                return;
            }
        }

        // Obtenemos la ubicación más reciente
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                mlocationCallback,
                null /* Looper */);
    }



    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mlocationCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

                // Se cumplen todas las configuraciones de ubicación.
                // La aplicación envía solicitudes de ubicación del usuario.
                iniciarActualizacionesUbicacion();

        }else {
            checkLocationSetting(builder);
        }
    }

    private void dialogoSolicitarPermisoGPS(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
    }


    private void trazarRuta(JSONObject jso, int color) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = jso.getJSONArray("routes");
            for (int i=0; i<jRoutes.length();i++){

                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");

                for (int j=0; j<jLegs.length();j++){

                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k<jSteps.length();k++){

                        String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end",""+polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);

                          polyline_final.add(mMap.addPolyline(new PolylineOptions().addAll(list).color(color).width(5)));

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//***************************************************PETICIONES AL WEB SERVICES***************************************************************************************************
/*public void insert_ubicacionruta(String URL) {
    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
       //     Toast.makeText(getActivity(), "Registro Exitosa", Toast.LENGTH_SHORT).show();
            getubicacionrutatarea();
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(),"Error de conexión",Toast.LENGTH_SHORT).show();
        }
    }){
        @Override
        protected Map<String, String> getParams(){
            Map<String,String> parametros = new HashMap<String,String>();
            parametros.put("latitude",latitude);
            parametros.put("longitude",longitude);
            parametros.put("id_usuario",Integer.toString(id));
            parametros.put("tabla",rol);
            return parametros;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
    requestQueue.add(stringRequest);
}*/

    public void validar_correo(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject =null;
                for (int i=0;i< response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        id = jsonObject.getInt("id_usuarios");
                        rol = jsonObject.getString("tipo_usuario");
                       // Toast.makeText(getActivity(),"no esta vacio "+id+"    "+rol, Toast.LENGTH_SHORT).show();
                        if (  rol.equals("Usuario")) {
                            actualizarubicacion();
                            getcurrentlocation();
                        }else if(rol.equals("Conductor De Transporte")){
                            actualizarubicacion();
                            spinner.setSelection(jsonObject.getInt("origen"));
                            spinner.setEnabled(false);
                        }else
                            {
                                actualizarubicacion();
                                getcurrentlocation();
                            }
                    } catch (JSONException e) {
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void actualizar_ubicacionruta(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(getActivity(), "Registro Exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            //    Toast.makeText(getActivity(),"Error de conexión",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("latitude",latitude);
                parametros.put("longitude",longitude);
                parametros.put("id_usuario",Integer.toString(id));
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void getubicacionruta( String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject =null;
                eliminarmarker();
                for (int i=0;i< response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        agregarmarcadorruta(Double.valueOf(jsonObject.getString("Latitude")), Double.valueOf(jsonObject.getString("Longitude")));
                    } catch (JSONException e) {
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    //petición a la api de google map

    public void trazarrutas(String URL,int color){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                   JSONObject jso = new JSONObject(response);
                    trazarRuta(jso,color);
                    Log.i("jsonRuta: ",""+response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }


}