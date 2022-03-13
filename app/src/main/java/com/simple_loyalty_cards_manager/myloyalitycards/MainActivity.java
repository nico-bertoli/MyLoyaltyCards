package com.simple_loyalty_cards_manager.myloyalitycards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // contenitore di elementi che possono essere scrollati in modo efficiente
    RecyclerView recyclerView;
    //bottone per aggiungere carte
    FloatingActionButton addButton;

    CardList cardList;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.buttonAdd);

        //inizializzo card list
        cardList = new CardList();

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addCard();
            }});
    }
    //----------------------------------------------------------------------------------------------
    //ogni volta che ricarico la view, ricarico la lista delle carte in modo da mostrarla aggiornata
    @Override
    protected void onResume() {
        super.onResume();
        //carico la lista di carte dal database
        loadFromDBToMemory();
        //oggetto che si frappone tra il recycler view e i dati che deve mostrare
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,cardList );
        // imposto un grid layout manager come layout manager del recycler view, in modo da avere 2 colonne
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(recyclerViewAdapter);
    }
    //----------------------------------------------------------------------------------------------
    //quando viene premuto il bottone add, lancio l' activity che gestisce l' aggiunta di una carta
    public void addCard(){
        Intent intent = new Intent(this,NewCardActivity.class);
        startActivity(intent);
    }
    //----------------------------------------------------------------------------------------------
    private void loadFromDBToMemory()
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
//        sqLiteManager.populateCardListArray(cardList);
        cardList = sqLiteManager.loadCardListFromDB();
    }
}