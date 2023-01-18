package apps.ejemplo.rutmapleon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class Registrarse extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[A-Z]"  +      // comience con mayuscula
                    "(?=.*[0-9])" +          // al menos un numero
                    "(?=\\S+$)" +            // Sin espacios en blancos
                    ".{4,}" +                // Al menos 4 caracteres
                    "$");

    EditText nombre,telefono,correo,contraseña;
    Spinner tipo;
    Button btn_ir_inicioSesion,crear_cuenta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        nombre=findViewById(R.id.et_nombre);
        telefono=findViewById(R.id.et_telefono);
        correo=findViewById(R.id.et_correo);
        contraseña=findViewById(R.id.et_contrasena);
        tipo = findViewById(R.id.spn);

        btn_ir_inicioSesion=findViewById(R.id.btn_ir_inicioSesion);
        crear_cuenta=findViewById(R.id.crear_cuenta);



        btn_ir_inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registrarse.this,Login.class));
                finish();
            }
        });

        crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validarcorreo() | !validatePassword()) {
                    return;
                }
                registrar_usuario("https://rutmap.000webhostapp.com/rutmapleon/insertar_usuario.php");
                startActivity(new Intent(Registrarse.this,Login.class));
                finish();

                String input = "Email: " + correo.getText().toString();
                input += "\n";
                input += "Password: " + contraseña.getText().toString();
                //Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();

            }
        });
    }


    public boolean validarcorreo()
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
            correo.setError("Por favor, ingrese un correo electrónico válido");
            return false;
        } else {
            correo.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String contraseñaInput = contraseña.getText().toString().trim();
        // if password field is empty
        // it will display error message "Field can not be empty"
        if (contraseñaInput.isEmpty()) {
            contraseña.setError("El campo no puede estar vacío");
            return false;
        }

        // if password does not matches to the pattern
        // it will display an error message "Password is too weak"
        else if (!PASSWORD_PATTERN.matcher(contraseñaInput).matches()) {
            contraseña.setError("La contraseña es demasiado débil");
            return false;
        } else {
            contraseña.setError(null);
            return true;
        }
    }


    public void registrar_usuario(String URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Registro Exitoso", Toast.LENGTH_SHORT).show();
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
                parametros.put("nombre",nombre.getText().toString());
                parametros.put("telefono",telefono.getText().toString());
                parametros.put("correo",correo.getText().toString());
                parametros.put("tipo",tipo.getSelectedItem().toString());
                parametros.put("contraseña",contraseña.getText().toString());
                return parametros;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

}