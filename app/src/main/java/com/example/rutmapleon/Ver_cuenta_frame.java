package com.example.rutmapleon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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


public class Ver_cuenta_frame extends Fragment {

    String id,tipo_cuentas;
    TextView nombre, telefono, correo ;
    EditText et_nombre, et_telefono,et_correo;
    public Ver_cuenta_frame() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_cuenta_frame, container, false);
        Button editar = view.findViewById(R.id.editar);
        Button Eliminar = view.findViewById(R.id.eliminar);
        nombre = view.findViewById(R.id.nombre);
        telefono = view.findViewById(R.id.telefono) ;
        correo = view.findViewById(R.id.correo);




        id = getArguments().getString("id");
        tipo_cuentas = getArguments().getString("tipo_usuarios");
        if (tipo_cuentas.equals("Administrador")) {
            editar.setEnabled(false);
            Eliminar.setEnabled(false);
            editar.setVisibility(View.INVISIBLE);
            Eliminar.setVisibility(View.INVISIBLE);
        }
        getusuario("https://rutmap.000webhostapp.com/rutmapleon/getusuarios.php?ID="+id+"&tipo_usuarios="+tipo_cuentas);

        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar_usuario("https://rutmap.000webhostapp.com/rutmapleon/eliminar_usuarios.php");
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View ed = inflater.inflate(R.layout.editar_cuenta, null);
                builder.setView(ed);
                builder.create();

                et_nombre = ed.findViewById(R.id.et_nombre);
                et_nombre.setText(nombre.getText());
                et_telefono = ed.findViewById(R.id.et_telefono);
                et_telefono.setText(telefono.getText());
                et_correo = ed.findViewById(R.id.et_correo) ;
                et_correo.setText(correo.getText());
                AlertDialog dialog = builder.show();

                Button actualizar = ed.findViewById(R.id.bt_aceptar);

                actualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editar_usuario("https://rutmap.000webhostapp.com/rutmapleon/editar_usuario.php");
                        getusuario("https://rutmap.000webhostapp.com/rutmapleon/getusuarios.php?ID="+id+"&tipo_usuarios="+tipo_cuentas);
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    public void eliminar_usuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.drawable.ic_baseline_delete_24).setTitle("Eliminar").setMessage("Desea Eliminar el elemento ")
                        .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity().getApplicationContext(),"Cuenta eliminada con Exito", Toast.LENGTH_SHORT).show();
                                SharedPreferences preferences = getActivity().getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                                preferences.edit().clear().commit();
                                Intent intent = new Intent(getActivity().getApplicationContext(),Login.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Error de conexión",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("ID",id);
                return parametros;
            }
        };
        Volley.newRequestQueue( getContext()).add(stringRequest);
    }

    public void getusuario(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject =null;
                for (int i=0;i< response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        nombre.setText(jsonObject.getString("nombres"));
                        telefono.setText( Integer.toString(jsonObject.getInt("telefono")));
                        correo.setText(jsonObject.getString("correoelectronico"));

                    } catch (JSONException e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Correo no Valido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void editar_usuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getApplicationContext(),"Registro Exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Error de conexión",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("Nombre",et_nombre.getText().toString());
                parametros.put("Correo",et_correo.getText().toString());
                parametros.put("Telefono",et_telefono.getText().toString());
                parametros.put("id_usuarios",id);
                parametros.put("tipo_usuarios",tipo_cuentas);
                return parametros;
            }
        };
        Volley.newRequestQueue( getContext()).add(stringRequest);
    }

}