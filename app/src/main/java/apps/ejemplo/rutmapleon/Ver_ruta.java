package apps.ejemplo.rutmapleon;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apps.ejemplo.rutmapleon.clases.Ruta;
import apps.ejemplo.rutmapleon.clases.Rutas;


public class Ver_ruta extends Fragment {

    public static ArrayList<Ruta> alp = new ArrayList<>();
    ListView listaruta;
    RutaAdapter rutaAdapter;

    EditText numero ;
    Spinner origen ;
    Spinner destino ;
    EditText cooperativa ;
    Spinner conductor,conductoraux;
    String tipo_usuarios,Cooperativa;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tipo_usuarios = getArguments().getString("tipo_usuarios");
        Cooperativa = getArguments().getString("Cooperativa");

      //  Toast.makeText(getContext(),"no esta vacio "+tipo_usuarios+"  "+Cooperativa, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ver_ruta, container, false);
        listaruta = (ListView) view.findViewById(R.id.listRutas);

        Actualizar();
        View v = inflater.inflate(R.layout.createeditruta, null);
        conductor = (Spinner) v.findViewById(R.id.conductorcreateEdit);

        getusuario("https://rutmap.000webhostapp.com/rutmapleon/getusuariospinner.php");



        if (tipo_usuarios.contentEquals( "Administrador"))
        {
             registerForContextMenu(listaruta);
             setHasOptionsMenu(true);
        }
        return view;
    }
    private void Actualizar(){
        new getRuta().execute();
    }

    private void Actualizaragregar(AlertDialog dialog){
        new getRuta().execute();
        dialog.dismiss();
    }

    private class getRuta extends AsyncTask<Object, Object, Rutas>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Cargando datos...", true);
        }

        @Override
        protected Rutas doInBackground(Object... objects) {
            try {
                return WSJasonRutas.getRuta(Cooperativa);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Rutas result) {
            super.onPostExecute(result);

            if(result != null) {
                alp = result;
                rutaAdapter = new RutaAdapter(getActivity(), R.layout.lista_rutas, alp);
                listaruta.setAdapter(rutaAdapter);
            }
            else {
                Toast.makeText(getActivity(), "Error al cargar los datos..", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

    }

    public void getusuario(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<String> usuarios = new ArrayList<>();
                String nombre ;
                JSONObject jsonObject =null;
                for (int i=0;i< response.length();i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        nombre = jsonObject.getString("id_usuarios") +" "+ jsonObject.getString("nombres");
                        usuarios.add(nombre);
                    } catch (JSONException e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Correo no Valido", Toast.LENGTH_SHORT).show();
                    }
                    ArrayAdapter<String> list = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, usuarios);
                    conductor.setAdapter(list);
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


    private class editarRuta extends AsyncTask<Object,Object,Boolean>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"","Enviando datos...", true);
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            Ruta ruta = (Ruta) params[0];
            if (ruta==null) return false;
            try {
                return WSJasonRutas.editarRuta(ruta);
            }catch (IOException e){
                e.printStackTrace();
                return false;
            }catch (JSONException e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if(result) {
                Actualizar();
            }
            else{
                Toast.makeText(getActivity(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class eliminarRuta extends AsyncTask<Object, Object,Boolean>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"","Enviando datos...", true);
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            Ruta ruta = (Ruta) params[0];
            if (ruta==null) return false;
            try {
                return WSJasonRutas.EliminarRuta(ruta);
            }catch (IOException e){
                e.printStackTrace();
                return false;
            }catch (JSONException e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if(result) {
                Actualizar();
            }
            else{
                Toast.makeText(getActivity(), "Error al Eliminar los datos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menuagregar,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        switch (item.getItemId()) {
            case R.id.itemNuevo:

                LayoutInflater inflater = this.getLayoutInflater();
                View v = inflater.inflate(R.layout.createeditruta, null);
                builder.setView(v);
                builder.create();
                AlertDialog dialog = builder.show();

                conductoraux= (Spinner) v.findViewById(R.id.conductorcreateEdit);

                conductoraux.setAdapter(conductor.getAdapter());
                Button actualizar = (Button) v.findViewById(R.id.actualizar);
                numero = (EditText) v.findViewById(R.id.numerocreateEdit);
                origen = (Spinner) v.findViewById(R.id.origencreateEdit);
                destino = (Spinner) v.findViewById(R.id.destinocreateEdit);
                cooperativa = (EditText) v.findViewById(R.id.cooperativacreateEdit);
                cooperativa.setText(Cooperativa);
                actualizar.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validar(numero,cooperativa)==true)
                                {
                                    registrar_transporte("https://rutmap.000webhostapp.com/rutmapleon/Insertar_transporte.php");
                                    Actualizaragregar(dialog);
                                }
                            }
                        }
                );

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void registrar_transporte(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getApplicationContext(),"Registro Exitoso", Toast.LENGTH_SHORT).show();
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
                int posicion = conductoraux.getSelectedItem().toString().indexOf(" ");
                parametros.put("numero",numero.getText().toString());
                parametros.put("origen",String.valueOf(origen.getSelectedItemPosition()));
                parametros.put("destino",String.valueOf(destino.getSelectedItemPosition()));
                parametros.put("conductor",conductoraux.getSelectedItem().toString().substring(0,posicion));
                parametros.put("cooperativa",cooperativa.getText().toString());
                parametros.put("nombreconductor",conductoraux.getSelectedItem().toString().substring(2));
                parametros.put("nombreorigen",origen.getSelectedItem().toString());
                parametros.put("nombredestino",destino.getSelectedItem().toString());
                return parametros;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        if(v.getId() == R.id.listRutas) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getActivity().getMenuInflater() ;
            inflater.inflate(R.menu.menucontext, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        int index = info.position;
        switch(item.getItemId())
        {
            case R.id.itemEditar:


                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.createeditruta, null);
                builder.setView(v);
                builder.create();
                AlertDialog dialog = builder.show();

                conductoraux= (Spinner) v.findViewById(R.id.conductorcreateEdit);
                conductoraux.setAdapter(conductor.getAdapter());

                //Toast.makeText(getContext(),conductoraux.getSelectedItem().toString().substring(2), Toast.LENGTH_SHORT).show();

                TextView cabecera = (TextView) v.findViewById(R.id.cabecera);
                cabecera.setText("Editar Rutas");

                Button actualizar = (Button) v.findViewById(R.id.actualizar);
                numero = (EditText) v.findViewById(R.id.numerocreateEdit);
                numero.setText(alp.get(index).getNumero());
                origen = (Spinner) v.findViewById(R.id.origencreateEdit);
                origen.setSelection(Integer.parseInt(alp.get(index).getOrigen()));
                destino = (Spinner) v.findViewById(R.id.destinocreateEdit);
                destino.setSelection(Integer.parseInt(alp.get(index).getDestino()));
                conductoraux.setSelection(getposicionspinner(alp.get(index).getConductor()));
                cooperativa= (EditText) v.findViewById(R.id.cooperativacreateEdit);
                cooperativa.setText(String.valueOf(alp.get(index).getCooperativa()));

                actualizar.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validar(numero, cooperativa)==true)
                                {
                                    int posicion = conductoraux.getSelectedItem().toString().indexOf(" ");
                                    Ruta ruta = new Ruta();
                                    ruta.setId(alp.get(index).getId());
                                    ruta.setNumero(numero.getText().toString());
                                    ruta.setOrigen(String.valueOf(origen.getSelectedItemPosition()));
                                    ruta.setConductor(conductoraux.getSelectedItem().toString().substring(0,posicion));
                                    ruta.setCooperativa(cooperativa.getText().toString());
                                    ruta.setNombreconductor(conductoraux.getSelectedItem().toString().substring(2));
                                    ruta.setDestino(String.valueOf(destino.getSelectedItemPosition()));
                                    ruta.setNombredestino(destino.getSelectedItem().toString());
                                    ruta.setNombreorigen(origen.getSelectedItem().toString());
                                    new editarRuta().execute(ruta);
                                    dialog.dismiss();
                                }
                            }
                        }
                );


                return true;
            case R.id.itemEliminar:
                builder.setIcon(R.drawable.ic_baseline_delete_24).setTitle("Eliminar").setMessage(" ¿Desea eliminar el registro? ")
                        .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Ruta ruta = new Ruta();
                                ruta.setId(alp.get(index).getId());
                                new eliminarRuta().execute(ruta);
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public int getposicionspinner(String idusuario){
        for (int i = 0;conductor.getCount() >= i; i++){
            conductor.setSelection(i);
            int posicion = conductor.getSelectedItem().toString().indexOf(" ");
            if (conductor.getSelectedItem().toString().substring(0,posicion).equals(idusuario)) {
                //Toast.makeText(getActivity().getApplicationContext(),idusuario +"  "+conductor.getSelectedItem().toString().substring(0,1),Toast.LENGTH_SHORT).show();
                return i;
            }
        }
    return 0;
    }

    public boolean validar(EditText numero, EditText cooperativa){

        if (numero.getText().toString().length() == 0){
            Toast.makeText(getActivity().getApplicationContext(),"Tiene que poner numero",Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(cooperativa.getText().toString().length() == 0){
            Toast.makeText(getActivity().getApplicationContext(),"Tiene que poner un Conducto",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}