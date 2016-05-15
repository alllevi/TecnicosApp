package com.manomanitas.tecnicosapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.manomanitas.tecnicosapp.PresupuestosPackage.presupuesto;

import java.util.ArrayList;
import java.util.List;

public class PresupuestosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciclerview_presupuestos);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        // Esto hacerlo dentro de la clase presupuesto
        List<presupuesto> lista_presupuestos = new ArrayList<>();
        lista_presupuestos.add(new presupuesto("0", "Calefaccion", "Valencia", "(Valencia)", "Necesito instalar un Aire acondicionado de 4000 Frigorias, donde la maquina interior de la exterior las separa un tabique, y la maquina exterior ademas va situada en un balcon en un primer piso, en Xirivella", "¡Gratis!", "3.2 Km", "hace 3 dias"));
        lista_presupuestos.add(new presupuesto("1", "Fontanero", "Albal", "(Valencia)", "Fuga en un tubo de cobre. La fuga está a la vista, puesto que al hacer un agujero con el taladro, fue taladrado dicho tubo", "¡Gratis!", "10.3 Km", "hace 7 dias"));
        lista_presupuestos.add(new presupuesto("2", "Electricista", "Manises", "(Valencia)", "necesito el boletin electrico e instalacion de luz en una cochera. Llevaria un punto de luz y un enchufe.", "¡Gratis!", "0.8 Km", "hace 5 dias"));

        RVAdapter adapter = new RVAdapter(lista_presupuestos);
        rv.setAdapter(adapter);

    }


    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PresupuestoViewHolder> {

        public class PresupuestoViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView tw_categoria;
            TextView tw_municipio;
            TextView tw_provincia;
            TextView tw_averia;
            TextView tw_precio;
            TextView tw_kilometros;
            TextView tw_hacedias;
            Button bt_enviarPropuesta;

            PresupuestoViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                tw_categoria = (TextView) itemView.findViewById(R.id.idCategoria);
                tw_municipio = (TextView) itemView.findViewById(R.id.idMunicipio);
                tw_provincia = (TextView) itemView.findViewById(R.id.idProvincia);
                tw_averia = (TextView) itemView.findViewById(R.id.idAveria);
                tw_precio = (TextView) itemView.findViewById(R.id.idPrecio);
                tw_kilometros = (TextView) itemView.findViewById(R.id.idKilometros);
                tw_hacedias = (TextView) itemView.findViewById(R.id.idHaceDias);
                bt_enviarPropuesta = (Button) itemView.findViewById(R.id.enviarPropuesta_bt);

            }
        }

        List<presupuesto> presupuestos;

        RVAdapter(List<presupuesto> presupuestos) {
            this.presupuestos = presupuestos;
        }

        @Override
        public PresupuestoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_presupuestos, parent, false);
            PresupuestoViewHolder pvh = new PresupuestoViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(PresupuestoViewHolder holder, int position) {
            holder.tw_categoria.setText(presupuestos.get(position).getCategoria());
            holder.tw_municipio.setText(presupuestos.get(position).getMunicipio());
            holder.tw_provincia.setText(presupuestos.get(position).getProvincia());
            holder.tw_averia.setText(presupuestos.get(position).getAveria());
            holder.tw_kilometros.setText(presupuestos.get(position).getKilometros());
            holder.tw_precio.setText(presupuestos.get(position).getPrecio());
            holder.tw_hacedias.setText(presupuestos.get(position).getHaceDias());

            holder.bt_enviarPropuesta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enviarP();
                }
            });


        }

        private void enviarP() {
            Intent intent = new Intent(com.manomanitas.tecnicosapp.PresupuestosActivity.this, DetallePresupuestoActivity.class);
            startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return presupuestos.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }

}

