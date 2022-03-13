package com.simple_loyalty_cards_manager.myloyalitycards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CardZoomActivity extends AppCompatActivity {

    TextView nomeAzienda;
    TextView codiceQr;
    ImageView immagineQR;
    ImageButton sfondoFrontale;
    ImageButton sfondoPosteriore;

    FloatingActionButton deleteButton;
    FloatingActionButton editButton;

    Context context;
    int cardId;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_zoom);

        context=getApplicationContext();
        nomeAzienda=findViewById(R.id.nomeAzienda);
        codiceQr=findViewById(R.id.codiceQR);
        immagineQR=findViewById(R.id.immagineQR);
        deleteButton=findViewById(R.id.buttonDelete);
        editButton=findViewById(R.id.buttonEdit);
        sfondoPosteriore=findViewById(R.id.sfondoRetro);
        sfondoFrontale=findViewById(R.id.sfondoFrontale);

        //tramite un intent mi viene passato l' id della carta che devo mostrare
        cardId = getIntent().getIntExtra("CARD_ID",0);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(context);
                sqLiteManager.removeCard(cardId);
                finish();
            }});

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                launchEditView();
            }});
        }
    //----------------------------------------------------------------------------------------------
        //ogni volta che si torna alla schermata, ricarico la carta dato che potrebbe essere stata modificata
        @Override
        public void onResume() {
            super.onResume();
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(context);
            Card card = null;
            try { card = sqLiteManager.getCardFromDB(cardId); }
            catch (Exception e) { e.printStackTrace(); }
            nomeAzienda.setText(card.getNomeAzienda());
            codiceQr.setText(card.getCode());
            sfondoFrontale.setColorFilter(card.getColor());
            sfondoPosteriore.setColorFilter(card.getColor());
            CodePrinter.stampaCodiceCarta(card,immagineQR);
        }
    //----------------------------------------------------------------------------------------------
    //lancio la edit view passandole l' id della carta
    private void launchEditView(){
        Intent intent = new Intent(context,EditCardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("CARD_ID",cardId);

        context.startActivity(intent);
    }
}