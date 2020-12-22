package com.example.hw1;

import java.util.List;
import java.util.Stack;

public class CardDeck {

    private Stack<Card> cards = new Stack<>();

    public static enum CardValue {TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);
        private final int value;

        CardValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static enum CardType {DIAMOND, SPADE, HEART, CLUB};

    public CardDeck(){
    }

    public CardDeck(Stack<Card> cards){
        this.cards =cards;
    }

    public Stack<Card> getCards() {
        return cards;
    }

    public Card getCard(){
        return cards.pop();
    }

    public int deckIsEmpty(){
        if(cards.empty()){
            return 0;
        }
        return 1;
    }

    public void setCards(Stack<Card> cards) {
        this.cards = cards;
    }

    public void addCard(List<Card> card) {
        this.cards.addAll(card);
    }

    @Override
    public String toString() {
        return "CardDeck{" +
                "cards=" + cards +
                '}';
    }
}
