package com.simple_loyalty_cards_manager.myloyalitycards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteManager extends SQLiteOpenHelper {

    //db manager statico IMPORTANTE
    private static SQLiteManager sqLiteManager;

    private static final String DB_NAME = "CARTE_DB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "CARTE";


    private static final String NOME_AZIENDA = "nomeAzienda";
    private static final String QR_CODE = "qrCode";
    private static final String ID_FIELD = "id";
    private static final String COLOR = "color";
    private static final String CODE_TYPE = "CODE_TYPE";


    //----------------------------------------------------------------------------------------------
    //costruttore
    public SQLiteManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    //----------------------------------------------------------------------------------------------
    // inizializza il db manager
    public static SQLiteManager instanceOfDatabase(Context context){
        //se non ho già inizializzato il db manager, lo inizializzo
        if(sqLiteManager==null)
            sqLiteManager = new SQLiteManager(context);
        //ritorno il db manager
        return sqLiteManager;
    }
    //----------------------------------------------------------------------------------------------
    // Costruisce il database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("create table ")
                .append(TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(NOME_AZIENDA)
                .append(" TEXT, ")
                .append(QR_CODE)
                .append(" TEXT, ")
                .append(COLOR)
                .append(" INT, ")
                .append(CODE_TYPE)
                .append(" INT);")
        ;
        sqLiteDatabase.execSQL(sql.toString());
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    //blank
    }
    //----------------------------------------------------------------------------------------------
    //aggiunge una carta al db
    public int addCardToDB(Card card){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contenValues = new ContentValues();

        //contenValues.put(ID_FIELD,card.getId());
        contenValues.put(NOME_AZIENDA,card.getNomeAzienda());
        contenValues.put(QR_CODE,card.getCode());
        contenValues.put(COLOR,card.getColor());
        contenValues.put(CODE_TYPE,card.getCodeType());

        sqLiteDatabase.insert(TABLE_NAME,null,contenValues);
        String[] a = {ID_FIELD};

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,a,null, null, null, null, null);
        cursor.moveToLast();
        System.out.println("creatied card with id: "+cursor.getInt(0));
        return cursor.getInt(0);
    }
    //----------------------------------------------------------------------------------------------
    //rimuove la carta dal db
    public void removeCard(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        StringBuilder sql;
        sql = new StringBuilder()
                .append("DELETE FROM ")
                .append(TABLE_NAME)
                .append(" WHERE ")
                .append(ID_FIELD)
                .append(" = ")
                .append(id)
                .append(";");
        System.out.println("deleting card with id: "+id);
        sqLiteDatabase.execSQL(sql.toString());
    }
    //----------------------------------------------------------------------------------------------
    //aggiorna i valori di una carta nel db
    public void updateCardInDB(Card card)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME_AZIENDA, card.getNomeAzienda());
        contentValues.put(QR_CODE, card.getCode());
        contentValues.put(COLOR,card.getColor());
        contentValues.put(CODE_TYPE,card.getCodeType());

        System.out.println("updating card with id: "+card.getId());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(card.getId())});
    }
    //----------------------------------------------------------------------------------------------
    //restituisce la carta avente un certo id
    public Card getCardFromDB(int id) throws Exception {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null))
        {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int resultId = result.getInt(0);
                    if(resultId == id){
                        String nomeAzienda = result.getString(1);
                        String codiceQR = result.getString(2);
                        int color = result.getInt(3);
                        int codeType = result.getInt(4);
                        Card card = new Card(id,nomeAzienda,codiceQR,color,codeType);
                        return card;
                    }
                }
            }
        }
        throw new Exception("cant find card in DB");
    }
    //----------------------------------------------------------------------------------------------
    //costruisce una nuova cardList a partire dai dati del db
    public CardList loadCardListFromDB(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        CardList cardList = new CardList();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null))
        {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(0);
                    String nomeAzienda = result.getString(1);
                    String codiceQR = result.getString(2);
                    int color = result.getInt(3);
                    int codeType = result.getInt(4);
                    Card card = new Card(id,nomeAzienda,codiceQR,color,codeType);
                    cardList.add(card);
                }
            }
        }
        return cardList;
    }

}
