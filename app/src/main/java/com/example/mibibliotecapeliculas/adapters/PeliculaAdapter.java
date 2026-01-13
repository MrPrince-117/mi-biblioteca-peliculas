package com.example.mibibliotecapeliculas.adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mibibliotecapeliculas.models.Pelicula;
import com.example.mibibliotecapeliculas.R;

import java.util.List;

public class PeliculaAdapter extends ArrayAdapter<Pelicula> {

    private final Context context;
    private final List<Pelicula> listaPeliculas;

    public PeliculaAdapter(@NonNull Context context, @NonNull List<Pelicula> listaPeliculas) {
        super(context, 0, listaPeliculas);
        this.context = context;
        this.listaPeliculas = listaPeliculas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_pelicula, parent, false);
        }

        ImageView imgPortada = convertView.findViewById(R.id.imgItemPortada);
        TextView tvTitulo = convertView.findViewById(R.id.tvItemTitulo);
        TextView tvGenero = convertView.findViewById(R.id.tvItemGenero);
        TextView tvAnio = convertView.findViewById(R.id.tvItemAnio);
        TextView tvValoracion = convertView.findViewById(R.id.tvItemValoracion);

        Pelicula pelicula = getItem(position);

        if (pelicula != null) {
            imgPortada.setImageResource(pelicula.getPortada());
            tvTitulo.setText(pelicula.getTitulo());
            tvGenero.setText("Género: " + pelicula.getGenero());
            tvAnio.setText("Año: " + pelicula.getAnio());
            tvValoracion.setText("Valoración: " + pelicula.getValoracion());
        }

        return convertView;
    }
}
