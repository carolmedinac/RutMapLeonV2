package apps.ejemplo.rutmapleon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText et_correo,et_contraseña;
    Button btn_ir_crearCuenta,iniciar_sesion,bt_recuperarpass;
    String correo,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_ir_crearCuenta=findViewById(R.id.btn_ir_crearCuenta);
        iniciar_sesion=findViewById(R.id.iniciar_sesion);
        et_correo = findViewById(R.id.et_correolo);
        et_contraseña = findViewById(R.id.et_contrasenalo);
        bt_recuperarpass= findViewById(R.id.bt_recuperarpassword);

        recuperarPreferencias();

        bt_recuperarpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                LayoutInflater inflater= Login.this.getLayoutInflater();
                View V =inflater.inflate(R.layout.verificar_correo,null);

                builder.setView(V);

                builder.create();

                AlertDialog dialog = builder.show();
                EditText correo = V.findViewById(R.id.et_correorecuplo);
                Button enviar = V.findViewById(R.id.enviar);
                enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!validarcorreo(correo)) {
                            return;
                        }
                        validar_correo("https://rutmap.000webhostapp.com/rutmapleon/verificar_correo.php?correo="+correo.getText(),dialog);
                    }
                });

            }
        });

        btn_ir_crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Registrarse.class));
                finish();
            }
        });

        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correo = et_correo.getText().toString();
                password = et_contraseña.getText().toString();
                if (!correo.isEmpty() && !password.isEmpty()) {
                    validar_usuario("https://rutmap.000webhostapp.com/rutmapleon/iniciar_sesion.php");
                }
                else{
                    Toast.makeText(Login.this,"No se permiten campos vacios",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validarcorreo(EditText correo)
    {
        // Extraemos la entrada del EditText
        String correoInput = correo.getText().toString().trim();

        // si la entrada del correo esta vacio
        if (correoInput.isEmpty()) {
            correo.setError("Error: No puede dejar vacía este Campo");
            return false;
        }

        // Hacer coincidir el correo electrónico de entrada con un patrón de correo electrónico predefinido
        else if (!Patterns.EMAIL_ADDRESS.matcher(correoInput).matches()) {
            correo.setError("Por favor, ingrese un correo electrónico válido "+correo.getText());
            return false;
        } else {
            correo.setError(null);
            return true;
        }
    }

    private void guardarpreferencias(){
        SharedPreferences preferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo",correo);
        editor.putString("contraseña",password);
        editor.putBoolean("sesion",true);
        editor.commit();
    }

    public void validar_correo(String URL, AlertDialog dialog){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject =null;
                for (int i=0;i< response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("id_usuarios");
                        String tipo_usuarios= jsonObject.getString("tipo_usuario");
                        Intent intent = new Intent(Login.this, recuperar_contrasena.class);
                        intent.putExtra("id_usuario",Integer.toString(id));
                        intent.putExtra("tipo_usuario",tipo_usuarios);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(Login.this, "Correo no Valido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Usuario no valido",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(jsonArrayRequest);
    }

    private void recuperarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("PreferenciasLogin",Context.MODE_PRIVATE);
        et_correo.setText(preferences.getString("correo",""));
        et_contraseña.setText(preferences.getString("contraseña",""));
    }

    public void validar_usuario(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    guardarpreferencias();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("correo", et_correo.getText().toString());
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(Login.this,"Usuario no Válido",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("correo",correo);
                parametros.put("contraseña",password);
                return parametros;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}