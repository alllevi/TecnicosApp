package com.manomanitas.tecnicosapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manomanitas.tecnicosapp.PresupuestosPackage.presupuesto;

import java.util.ArrayList;
import java.util.List;

public class CompradosActivity extends AppCompatActivity {

    private List<presupuesto> lista_comprados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciclerview_presupuestos_comprados);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rvc);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        obtenerPresupuestosComprados();

        RVAdapterComprados adapter = new RVAdapterComprados(lista_comprados);
        rv.setAdapter(adapter);
    }

    public class RVAdapterComprados extends RecyclerView.Adapter<RVAdapterComprados.CompradosViewHolder> {

        public class CompradosViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView tw_categoria;
            TextView tw_municipio;
            TextView tw_provincia;
            TextView tw_averia;
            TextView tw_kilometros;
            TextView tw_hacedias;
            TextView tw_nombre;
            TextView tw_telefono;
            TextView tw_email;

            CompradosViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cvc);
                tw_categoria = (TextView) itemView.findViewById(R.id.idCategoriaComprados);
                tw_municipio = (TextView) itemView.findViewById(R.id.idMunicipioComprados);
                tw_provincia = (TextView) itemView.findViewById(R.id.idProvinciaComprados);
                tw_nombre = (TextView) itemView.findViewById(R.id.idNombreComprados);
                tw_telefono = (TextView) itemView.findViewById(R.id.idTelefonoComprados);
                tw_email = (TextView) itemView.findViewById(R.id.idEmailComprados);

                tw_averia = (TextView) itemView.findViewById(R.id.idAveriaComprados);
                tw_kilometros = (TextView) itemView.findViewById(R.id.idKilometrosComprados);
                tw_hacedias = (TextView) itemView.findViewById(R.id.idHaceDiasComprados);

            }
        }

        List<presupuesto> comprados;

        RVAdapterComprados(List<presupuesto> list_comprados) {
            this.comprados = list_comprados;
        }

        @Override
        public CompradosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_presupuestos_comprados, parent, false);
            CompradosViewHolder pvh = new CompradosViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(CompradosViewHolder holder, int position) {
            holder.tw_categoria.setText(comprados.get(position).getCategoria());
            holder.tw_municipio.setText(comprados.get(position).getMunicipio());
            holder.tw_provincia.setText(comprados.get(position).getProvincia());
            holder.tw_averia.setText(comprados.get(position).getAveria());
            holder.tw_nombre.setText(comprados.get(position).getNombre());
            holder.tw_telefono.setText(comprados.get(position).getTelefono());
            holder.tw_email.setText(comprados.get(position).getEmail());
            holder.tw_kilometros.setText(comprados.get(position).getKilometros());
            holder.tw_hacedias.setText(comprados.get(position).getHaceDias());


        }

        @Override
        public int getItemCount() {
            return comprados.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }

    private void obtenerPresupuestosComprados() {
        lista_comprados.add(new presupuesto("0", "Electro", "Valencia", "(Valencia)", "asdasdasda", "0.6 Km", "hace 3 dias", "Pepe Perez", "676589898", "peperez@gmail.com"));
        lista_comprados.add(new presupuesto("1", "Calefaccion", "Valencia", "(Valencia)", "agdfhfgj", "1.2 Km", "hace 7 dias", "Manolo perales", "676512354", "maper@gmail.com"));
        lista_comprados.add(new presupuesto("2", "Telefonillo", "Valencia", "(Valencia)", "llkljkh", "3.0 Km", "hace 5 dias", "Florentino", "677899898", "floren@gmail.com"));


    }
}
