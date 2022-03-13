package com.simple_loyalty_cards_manager.myloyalitycards;

import com.simple_loyalty_cards_manager.myloyalitycards.Card;

import java.util.ArrayList;

public class CardList {
    private ArrayList<Card> cardList;

    public CardList(){
        cardList = new ArrayList<>();
    }

    public int getSize(){
        return cardList.size();
    }

    public void add(Card card){
        cardList.add(card);
    }

    public Card getCard(int position){
        return cardList.get(position);
    }
}
