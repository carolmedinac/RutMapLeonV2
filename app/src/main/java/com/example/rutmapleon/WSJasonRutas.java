package com.example.rutmapleon;

import com.example.rutmapleon.clases.Ruta;
import com.example.rutmapleon.clases.Rutas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WSJasonRutas extends WSJason{
    public static Rutas getRuta(String cooperativa) throws IOException, JSONException {
        Rutas result = null;
        String Url = "https://rutmap.000webhostapp.com/rutmapleon/getTransporte.php?Cooperativa="+cooperativa;
        JSONObject jsonObj = getJson(Url);
        if (jsonObj != null) {

            JSONArray rutas = jsonObj.getJSONArray("transporte");
            result = new Rutas();

            for (int i = 0; i < rutas.length(); i++) {
                JSONObject c = rutas.getJSONObject(i);

                Ruta temp = new Ruta();
                temp.setId(c.getInt("id"));
                temp.setNumero(c.getString("numero"));
                temp.setConductor(Integer.toString(c.getInt("Id_conductor")));
                temp.setNombreconductor(c.getString("Conductor"));
                temp.setCooperativa(c.getString("Cooperativa"));
                temp.setOrigen(c.getString("Origen"));
                temp.setNombreorigen(c.getString("nombre_origen"));
                temp.setDestino(c.getString("destino"));
                temp.setNombredestino(c.getString("nombre_destino"));

                result.add(temp);
            }
        }
        return result;
    }


    public static boolean editarRuta(Ruta ruta) throws IOException,JSONException{
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", ruta.getId());
        jsonParam.put("numero", ruta.getNumero());
        jsonParam.put("Conductor", ruta.getConductor());
        jsonParam.put("Cooperativa", ruta.getCooperativa());
        jsonParam.put("Origen", ruta.getOrigen());
        jsonParam.put("destino", ruta.getDestino());
        jsonParam.put("nombre_conductor",ruta.getNombreconductor());
        jsonParam.put("nombre_origen",ruta.getNombreorigen());
        jsonParam.put("nombre_destino",ruta.getNombredestino());

        JSONObject jsonResult = sendJson("https://rutmap.000webhostapp.com/rutmapleon/Update_transporte.php", jsonParam);

        if(jsonResult == null) return false;

        String estado = jsonResult.getString("estado");

        if(estado.compareTo("1")==0) return true;
        else return  false;
    }

    public static boolean EliminarRuta(Ruta ruta) throws IOException,JSONException{
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", ruta.getId());

        JSONObject jsonResult = sendJson("https://rutmap.000webhostapp.com/rutmapleon/delete_ruta.php", jsonParam);

        if(jsonResult == null) return false;

        String estado = jsonResult.getString("estado");

        if(estado.compareTo("1")==0) return true;
        else return  false;
    }

}
