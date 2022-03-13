package com.simple_loyalty_cards_manager.myloyalitycards;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import petrov.kristiyan.colorpicker.ColorPicker;
//classe madre che genera EditCardActivity e NewCardActivity
public class EditCreateCardActivity extends AppCompatActivity {

    ImageView qrImage;
    FloatingActionButton cancelButton;
    FloatingActionButton confirmButton;
    TextInputEditText nomeAziendaText;
    TextInputEditText qrCodeText;
    Button pickColorButton;
    RadioGroup radioGroup;
    RadioButton radioQR;
    RadioButton radioBar;
    ImageButton scanButton;
    TextInputLayout codeInputLayout;
    TextInputLayout companyInputLayout;

    Context context;

    int pickedColor;
    int codeType;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_card);

        qrImage = findViewById(R.id.imgQR);
        cancelButton = findViewById(R.id.buttonCancel);
        confirmButton = findViewById(R.id.buttonConfirm);
        nomeAziendaText = findViewById(R.id.textNomeAzienda);
        qrCodeText = findViewById(R.id.textQR);
        pickColorButton = findViewById(R.id.buttonPickColor);
        radioGroup = findViewById(R.id.radioGroup);
        radioBar = findViewById(R.id.buttonSelectBarCode);
        radioQR = findViewById(R.id.buttonSelectQR);
        scanButton = findViewById(R.id.scanButton);
        codeInputLayout = findViewById(R.id.codeInputLayout);
        companyInputLayout = findViewById(R.id.companyNameInputLayout);

        //-----------------------------------------------
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }});
        //-----------------------------------------------
        pickColorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openColorPicker();
            }});

        //-----------------------------------------------
        //gestisce la stampa del codice e dei messaggi helper a runtime
        qrCodeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

                //se l' utente cancella imposto lo sfondo bianco (altrimenti scompare l' immagine)
                if(qrCodeText.getText().length()==0){
                    qrImage.setImageBitmap(null);
                    qrImage.setBackgroundColor(Color.parseColor("#ffffff"));
                    //stampo messaggio di testo mancante
                    codeInputLayout.setHelperText("required");
                }
                //altrimenti stampo il barcode/qrcode
                else{
                    //cancello il messaggio di testo mancante
                    codeInputLayout.setHelperText("");
                    if(radioQR.isChecked())
                        CodePrinter.stampaQR(qrCodeText.getText().toString(),qrImage);
                    else {
                        if (nomeAziendaText.getText().length() == 0)
                            qrImage.setBackgroundColor(Color.parseColor("#ffffff"));
                        CodePrinter.stampaBarCode(qrCodeText.getText().toString(), qrImage);
                    }
                }
            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
        //-----------------------------------------------------
        //gestisco la stampa di "required" nel campo dove va inserito il nome dell' azienda
        nomeAziendaText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if(nomeAziendaText.getText().length()==0){
                    companyInputLayout.setHelperText("required");
                }
                else{
                    companyInputLayout.setHelperText("");
                }
            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
        //------------------------------------------------------------------------------------------
        //quando l' utente switcha tipo di code, l' interfaccia deve essere modificata
        radioBar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(nomeAziendaText.getText().length()==0)
                    qrImage.setBackgroundColor(Color.parseColor("#ffffff"));
                CodePrinter.stampaBarCode(qrCodeText.getText().toString(),qrImage);
            }});

        radioQR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                CodePrinter.stampaQR(qrCodeText.getText().toString(),qrImage);
            }});
        //------------------------------------------------------------------------------------------
        scanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                scanCode();
            }});
    }
    //----------------------------------------------------------------------------------------------
    //lettura di un codice tramite fotocamera
    protected void scanCode(){
        if(nomeAziendaText.getText().length()==0)qrImage.setBackgroundColor(Color.parseColor("#ffffff"));
        IntentIntegrator integrator = new IntentIntegrator(this);
        //utilizza la classe capture activity
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }
    //----------------------------------------------------------------------------------------------
    //codice eseguito dopo che la fotocamera ha letto un codice
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()!=null){
                qrCodeText.setText(result.getContents());
                if(result.getFormatName().equals("QR_CODE")){
                    radioQR.setChecked(true);
                    //aggiorno qr code
                    CodePrinter.stampaQR(qrCodeText.getText().toString(),qrImage);
                }
                else {
                    radioBar.setChecked(true);
                    if(nomeAziendaText.getText().length()==0)
                        qrImage.setBackgroundColor(Color.parseColor("#ffffff"));
                    CodePrinter.stampaBarCode(qrCodeText.getText().toString(),qrImage);
                }
                System.out.println("ho letto un codice di tipo: "+result.getFormatName());
            }
            else{
                Toast.makeText(this,"No Results", Toast.LENGTH_LONG).show();
            }
        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
    //----------------------------------------------------------------------------------------------
    //gestione del color picker
    protected void openColorPicker(){
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colors = new ArrayList<>();

        colors.add("#ff9696");
        colors.add("#ffc496");
        colors.add("#fff696");
        colors.add("#ccff96");
        colors.add("#96ff9d");
        colors.add("#96ffd9");
        colors.add("#96eeff");
        colors.add("#969aff");
        colors.add("#c596ff");
        colors.add("#ff96cc");

        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true);

        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                pickColorButton.setBackgroundColor(color);
                pickedColor=color;
            }

            @Override
            public void onCancel(){
                // put code
            }
        });

        //imposto il colore dei bottoni "ok" e "cancel" nel popup
        colorPicker.getPositiveButton().setTextColor(Color.parseColor("#000000"));
        colorPicker.getNegativeButton().setTextColor(Color.parseColor("#000000"));
//        colorPicker.getPositiveButton().setTextColor(Color.parseColor("#FFFFFF"));
//        colorPicker.getNegativeButton().setTextColor(Color.parseColor("#FFFFFF"));
        colorPicker.show();
    }

}