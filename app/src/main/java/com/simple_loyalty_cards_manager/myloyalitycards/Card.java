package com.simple_loyalty_cards_manager.myloyalitycards;

import java.util.ArrayList;

public class Card {
    private int id;
    private String nomeAzienda;
    private String code;
    private int color;
    private int codeType; //0 = qr, 1 = bar
    //----------------------------------------------------------------------------------------------
//    costruttore senza id
    public Card(String nomeAzienda, String code,int color,int codeType){
        this.nomeAzienda = nomeAzienda;
        this.code = code;
        this.color=color;
        this.codeType=codeType;
    }
    //----------------------------------------------------------------------------------------------
    //costruttore con id
    public Card(int id,String nomeAzienda, String code,int color,int codeType){
        this(nomeAzienda,code,color,codeType);
        this.id=id;

    }
    //----------------------------------------------------------------------------------------------
    //modifica i valori di una carta
    public void setCard(int id,String nomeAzienda, String code,int color,int codeType){
        this.id=id;
        this.nomeAzienda = nomeAzienda;
        this.code = code;
        this.color=color;
        this.codeType = codeType;
    }
    //----------------------------------------------------------------------------------------------
    public String getNomeAzienda(){return nomeAzienda;}
    public String getCode(){return code;}
    public int getId(){return id;}
    public int getColor(){return color;}
    public int getCodeType(){return codeType;}
}
