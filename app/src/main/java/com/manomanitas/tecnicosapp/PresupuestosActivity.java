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

public class PresupuestosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciclerview_presupuestos);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        List<presupuesto> lista_presupuestos = new ArrayList<>();
        lista_presupuestos.add(new presupuesto("0", "Electro", "Valencia", "Valencia", "asdasdasda", "¡Gratis!", "hace 3 dias"));
        lista_presupuestos.add(new presupuesto("1", "Calefaccion", "Valencia", "Valencia", "agdfhfgj", "¡Gratis!", "hace 7 dias"));
        lista_presupuestos.add(new presupuesto("2", "Telefonillo", "Valencia", "Valencia", "llkljkh", "¡Gratis!", "hace 5 dias"));
        RVAdapter adapter = new RVAdapter(lista_presupuestos);
        rv.setAdapter(adapter);

    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PresupuestoViewHolder> {

        public class PresupuestoViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView infoText;

            PresupuestoViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                infoText = (TextView) itemView.findViewById(R.id.info_text);


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
            holder.infoText.setText(presupuestos.get(position).getId());

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

