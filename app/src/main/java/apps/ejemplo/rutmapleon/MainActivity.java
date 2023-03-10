package apps.ejemplo.rutmapleon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout1;
    private NavigationView navigationView1;
    private Toolbar toolbar1;
    TextView etnombre, etcorreo;
    ImageView perfil;
    String correo = null;
    int id;
    String tipo_usuario,Cooperativa;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout1 = findViewById(R.id.drawerlayout);
        navigationView1 = findViewById(R.id.navigationview1);

        toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout1,toolbar1,0,0);
        drawerLayout1.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView1.setNavigationItemSelectedListener(this);


        menu = navigationView1.getMenu();

        //obtenemos los datos para la cabecera
        View view = navigationView1.getHeaderView(0);
        etcorreo = view.findViewById(R.id.email);
        etnombre = view.findViewById(R.id.username);
        perfil = view.findViewById(R.id.circle_image);

        Bundle parametros = this.getIntent().getExtras();
        SharedPreferences preferences= getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        boolean sesion = preferences.getBoolean("sesion",false);

        if (parametros!=null){
            correo = parametros.getString("correo");
            etcorreo.setText(correo);
            validar_correo("https://rutmap.000webhostapp.com/rutmapleon/verificar_correo.php?correo="+correo);

        }else if (sesion) {
            correo = preferences.getString("correo","");
            etcorreo.setText(correo);
            validar_correo("https://rutmap.000webhostapp.com/rutmapleon/verificar_correo.php?correo="+correo);
        }



        Bundle datos = new Bundle();
        datos.putString("correo", correo);
        datos.putString("Rol", tipo_usuario);
        home_fragment home_fragment = new home_fragment();
        home_fragment.setArguments(datos);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,home_fragment).commit();

    }


    //funcion para traer los datos de la cabecera
    public void validar_correo(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject =null;
                for (int i=0;i< response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        id = jsonObject.getInt("id_usuarios");
                        etnombre.setText(jsonObject.getString("nombres"));
                        String rol = jsonObject.getString("tipo_usuario");
                       // Toast.makeText(getApplicationContext(),"no esta vacio "+id+"    "+rol, Toast.LENGTH_SHORT).show();
                        tipo_usuario = rol;
                        if (rol.equals("Usuario")) {
                            perfil.setImageResource(R.drawable.usernorma);
                            Cooperativa = "usuarios";
                                if (etnombre.getText().equals("User")){
                                MenuItem item = menu.findItem(R.id.ver_cuenta);
                                item.setVisible(false);
                                } else{
                                    MenuItem item = menu.findItem(R.id.login) ;
                                    item.setVisible(false);
                                }
                        }else if(rol.equals("Conductor De Transporte")){
                            Cooperativa = "usuarios";
                            MenuItem item = menu.findItem(R.id.login) ;
                            item.setVisible(false);
                            perfil.setImageResource(R.drawable.usuarioconductor);
                        }
                        else if( rol.equals("Administrador")){
                            perfil.setImageResource(R.drawable.administrador);
                            MenuItem item = menu.findItem(R.id.login) ;
                            item.setVisible(false);
                            String cooperativa = jsonObject.getString("Cooperativa");
                            Cooperativa = cooperativa;
                        }
                        } catch (JSONException e) {
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this);
        AlertDialog alertDialog = builder.create();
        drawerLayout1.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){

            case R.id.ver_home:
                Bundle datos = new Bundle();
                datos.putString("correo", correo);
                home_fragment home_fragment = new home_fragment();
                home_fragment.setArguments(datos);
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,home_fragment).commit();
               // getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,new home_fragment()).commit();
                return true;

            case R.id.quien_somos:

                builder.setIcon(R.drawable.quienes_somos).setTitle("??Qui??nes somos?").setMessage("El proyecto RutMap-Le??n, es una aplicaci??n nicarag??ense, dise??ada por estudiantes de Sistemas de la UNAN-Le??n, centrada en brindar informaci??n ??til a los usuarios que usan el transporte p??blico en el casco urbano del Municipio de Le??n. ")
                        .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();
                return true;
            case R.id.ayuda:

                builder.setIcon(R.drawable.informacion).setTitle("Ayuda").setMessage("Podr?? encontrar la ayuda presionando el bot??n para ir al link.")
                        .setPositiveButton("Ir al link", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse("https://drive.google.com/file/d/1REwe2KTfLWxOUOnyKbynx1Ob8z6OposC/view?usp=sharing");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();
                return true;

            case R.id.ver_cuenta:
                Bundle args = new Bundle();
                args.putString("id",Integer.toString(id));
                args.putString("tipo_usuarios",tipo_usuario);
                Ver_cuenta_frame frame = new Ver_cuenta_frame();
                frame.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,frame).commit();
                return true;

            case R.id.info_rutas:
                //toolbar1.inflateMenu(R.menu.menuagregar);

                Bundle args1 = new Bundle();
                args1.putString("tipo_usuarios",tipo_usuario);
                args1.putString("Cooperativa",Cooperativa);
                Ver_ruta ruta_frame = new Ver_ruta();
                ruta_frame.setArguments(args1);
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,ruta_frame).commit();
                return true;

            case R.id.login:

                SharedPreferences preferences = getSharedPreferences("PreferenciasLogin",Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);

                finish();
                return true;


            case R.id.salir:

                SharedPreferences preferencess = getSharedPreferences("PreferenciasLogin",Context.MODE_PRIVATE);
                preferencess.edit().clear().commit();

                startActivity( new Intent(getApplicationContext(),SplashScreen.class));


                finish();
                return true;

            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
    }

}