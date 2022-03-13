package com.simple_loyalty_cards_manager.myloyalitycards;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

//oggetto che si frappone tra il recycler view e i dati che deve mostrare
//praticamente fa da datasource per il recycler view
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    Context context;
    CardList cardList;

    //------------------------------------------------------------------costruttore
    public RecyclerViewAdapter(Context ct,CardList cardList){
        this.context=ct;
        this.cardList = cardList;
    }

    //------------------------------------------------------------------onCreateViewHolder
    //definisce la view generale mostrata dal recyvler view, ovver cardo_front_menu_iniziale
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_front_menu_iniziale,parent,false);
        return new MyViewHolder(view);
    }
    //------------------------------------------------------------------onBindViewHolder
    //definisce come inizializzare ogni carta
    // holder -> elemento mostrato dal recycler view, e' una classe definita sotto
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int listPosition) {
        Card card = cardList.getCard(listPosition);
        holder.nomeAzienda.setText(card.getNomeAzienda());
        holder.cardButton.setColorFilter(card.getColor());

        //quando l' utente preme sulla carta, voglio che venga lanciata l' activity di zoom
        holder.cardButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openCardZoom(card.getId());
            }});
    }

    public void openCardZoom(int id){
        Intent intent = new Intent(context,CardZoomActivity.class);
        intent.putExtra("CARD_ID",id);
        context.startActivity(intent);
    }

    //------------------------------------------------------------------getItemCount
    @Override
    public int getItemCount() {
        return cardList.getSize();
    }

    //-------------------------------------------------------------------MyViewHolder
    //classe elemento mostrato dal view holder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout card;
        TextView nomeAzienda;
        ImageButton cardButton;

        //costruttore
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            nomeAzienda = itemView.findViewById(R.id.nomeAzienda);
            cardButton = itemView.findViewById(R.id.sfondoFrontale);
        }
    }

}
