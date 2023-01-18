package com.example.rutmapleon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class recuperar_contrasena extends AppCompatActivity {
    Button guardar,iniciarsesion;
    EditText contraseña1,contraseña2;
    String id,tipo_usuario;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[A-Z]"  +      // comience con mayuscula
                    "(?=.*[0-9])" +          // al menos un numero
                    "(?=\\S+$)" +            // Sin espacios en blancos
                    ".{4,}" +                // Al menos 4 caracteres
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros!=null){

            id = parametros.getString("id_usuario");
            tipo_usuario = parametros.getString("tipo_usuario");
            Toast.makeText(getApplicationContext(),"no esta vacio "+id+ " "+tipo_usuario, Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_recuperar_contrasena);
        contraseña1=findViewById(R.id.et_contrasenarecupo);
        contraseña2=findViewById(R.id.et_contrasena2recuplo);
        guardar=findViewById(R.id.Guardarrecup);
        iniciarsesion=findViewById(R.id.btn_ir_crearCuentarecu);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (!validatePassword1() | !validatePassword2()) {
                return;
            }

            if (contraseña1.getText().equals(contraseña2.getText())){
                Toast.makeText(getApplicationContext(),contraseña1.getText().toString() + "  "+ contraseña2.getText().toString(), Toast.LENGTH_SHORT).show();
                contraseña2.setError("Contraseña no coincide con la de arriba");
                return;
            }
                registrar_usuario("https://rutmap.000webhostapp.com/rutmapleon/recuperar_contrasenia.php");
                startActivity(new Intent(recuperar_contrasena.this,Login.class));
                finish();
            }
        });

        iniciarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(recuperar_contrasena.this,Login.class));
                finish();
            }
        });
    }

    private boolean validatePassword1() {
        String contraseñaInput = contraseña1.getText().toString().trim();
        // if password field is empty
        // it will display error message "Field can not be empty"
        if (contraseñaInput.isEmpty()) {
            contraseña1.setError("El campo no puede estar vacío");
            return false;
        }

        // if password does not matches to the pattern
        // it will display an error message "Password is too weak"
        else if (!PASSWORD_PATTERN.matcher(contraseñaInput).matches()) {
            contraseña1.setError("La contraseña es demasiado débil");
            return false;
        } else {
            contraseña1.setError(null);
            return true;
        }
    }

    private boolean validatePassword2() {
        String contraseñaInput = contraseña2.getText().toString().trim();
        // if password field is empty
        // it will display error message "Field can not be empty"
        if (contraseñaInput.isEmpty()) {
            contraseña2.setError("El campo no puede estar vacío");
            return false;
        }

        // if password does not matches to the pattern
        // it will display an error message "Password is too weak"
        else if (!PASSWORD_PATTERN.matcher(contraseñaInput).matches()) {
            contraseña2.setError("La contraseña es demasiado débil");
            return false;
        } else {
            contraseña2.setError(null);
            return true;
        }
    }

    public void registrar_usuario(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Registro Exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error de conexión",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("contraseña",contraseña1.getText().toString());
                parametros.put("id_usuarios",id);
                parametros.put("tipo_usuarios",tipo_usuario);
                return parametros;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
