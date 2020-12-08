package com.example.hw1;

public class Card {
    /*
    * each card have Type and Value - Club_6
     */
    private CardDeck.CardType cardType;
    private CardDeck.CardValue cardValue;

    public Card() {
    }

    public Card(CardDeck.CardValue cardValue, CardDeck.CardType cardType) {
        this.cardValue = cardValue;
        this.cardType = cardType;
    }

    public int getValue() {

        return cardValue.getValue();
    }

    public void setCardType(CardDeck.CardType cardType) {
        this.cardType = cardType;
    }

    public void setCardValue(CardDeck.CardValue cardValue) {
        this.cardValue = cardValue;
    }

    public String getType() {
        return cardType.toString().toLowerCase();
    }

    @Override
    public String toString() {
        return ""+ getType()+"_"+cardValue.getValue();
    }
}