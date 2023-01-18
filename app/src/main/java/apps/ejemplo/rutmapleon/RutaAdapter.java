package apps.ejemplo.rutmapleon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import apps.ejemplo.rutmapleon.clases.Ruta;

public class RutaAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Ruta> items;
    private int layout;

    public RutaAdapter(Context ctx, int layout, ArrayList<Ruta> items) {
        this.ctx = ctx;
        this.items = items;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Ruta getItem(int arg0) {
        return items.get(arg0);
    }
    @Override
    public long getItemId(int arg0) {
        return items.get(arg0).getId();
    }
    @Override
    public View getView(int arg0, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(layout, null);
        }

        Ruta r = getItem(arg0);
        ImageView img = (ImageView) v.findViewById(R.id.icon);
        TextView numero= (TextView) v.findViewById(R.id.numero);
        TextView origen = (TextView) v.findViewById(R.id.origen);
        TextView destino = (TextView) v.findViewById(R.id.destino);
        TextView conductor =(TextView) v.findViewById(R.id.conductor);
        TextView cooperativa =(TextView) v.findViewById(R.id.cooperativa);

        if (img != null)
            img.setImageResource(R.drawable.logo1);
        if (numero != null)
            numero.setText(r.getNumero());
        if (origen != null)
            origen.setText(r.getNombreorigen());
        if (destino != null)
            destino.setText(String.valueOf(r.getNombredestino()));
        if (conductor != null)
            conductor.setText(String.valueOf(r.getNombreconductor()));
        if (cooperativa != null)
            cooperativa.setText(String.valueOf(r.getCooperativa()));
        return v;
    }
}
