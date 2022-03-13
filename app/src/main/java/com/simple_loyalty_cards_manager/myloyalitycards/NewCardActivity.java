package com.simple_loyalty_cards_manager.myloyalitycards;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;

public class NewCardActivity extends EditCreateCardActivity {

    //----------------------------------------------------------------------------------------------onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        codeType = 0;//di default utilizzo il qr code

        radioQR.setChecked(true);

        //gestisco il bottone di conferma
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //se l' utente ha inserito dei campi accettabili
                if(nomeAziendaText.getText().length()>0 && qrCodeText.getText().length()>0){
                    //capisco se l' utente vuole il barcode o il qrcode
                    if(radioGroup.getCheckedRadioButtonId() == R.id.buttonSelectQR) codeType=0;       //selezionato qrcode
                    else codeType=1;                                        //selezionato barcode

                    //creo la nuova carta
                    Card card = new Card(
//                          Card.getNextId(),
                            nomeAziendaText.getText().toString(),
                            qrCodeText.getText().toString(),
                            pickedColor,
                            codeType
                    );

                    //la aggiungo al db
                    SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(context);
                    int id = sqLiteManager.addCardToDB(card);
                    finish();
                }
                //se l' utente non ha inserito campi accettabili, stampo un messaggio di errore
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