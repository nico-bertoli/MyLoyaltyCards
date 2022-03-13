package com.simple_loyalty_cards_manager.myloyalitycards;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;

public class EditCardActivity extends EditCreateCardActivity {

    int cardId;
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //leggo id carta dall' intent
        cardId = getIntent().getIntExtra("CARD_ID",0);

        //leggo carta dal db
        SQLiteManager sqLiteManager = new SQLiteManager(this);
        Card card = null;
        try { card = sqLiteManager.getCardFromDB(cardId); }
        catch (Exception e) { e.printStackTrace(); }

        //imposto interfaccia in base ai valori della carta
        nomeAziendaText.setText(card.getNomeAzienda());
        qrCodeText.setText(card.getCode());
        pickColorButton.setBackgroundColor(card.getColor());
        pickedColor = card.getColor();

        codeType = card.getCodeType();
        if(codeType==0) {
            radioQR.setChecked(true);
            CodePrinter.stampaQR(qrCodeText.getText().toString(),qrImage);
        }
        else {
            radioBar.setChecked(true);
            CodePrinter.stampaBarCode(qrCodeText.getText().toString(),qrImage);
        }
        //----------------------------------------------------------------------------------------------
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //se l' utente ha inserito valori accettabili
                if(nomeAziendaText.getText().length()>0 && qrCodeText.getText().length()>0) {

                    int checkRadioId = radioGroup.getCheckedRadioButtonId();
                    if (checkRadioId == R.id.buttonSelectQR)
                        codeType = 0;       //selezionato qrcode
                    else codeType = 1;      //selezionato barcode

                    Card editedCard = new Card(
                            cardId,
                            nomeAziendaText.getText().toString(),
                            qrCodeText.getText().toString(),
                            pickedColor,
                            codeType
                    );
                    // modifico la carta nel db
                    SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(context);
                    sqLiteManager.updateCardInDB(editedCard);

                    finish();
                }
                // se l'utente non ha inserito valori accettabili, stampo un messaggio di errore
                else{
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.AddEditConstraintLayout), "please fill required text fields", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setDuration(800);

//                    imposto la snackbar al centro
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                    params.gravity = Gravity.CENTER;
                    params.width = 700;
                    view.setLayoutParams(params);

                    snackbar.show();
                }
            }});

    }
}