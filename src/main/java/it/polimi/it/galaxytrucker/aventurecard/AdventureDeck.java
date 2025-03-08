package it.polimi.it.galaxytrucker.aventurecard;


import it.polimi.it.galaxytrucker.managers.GameManager;

import java.util.List;
import java.util.Stack;

public class AdventureDeck {

    private List<AdventureCard> cards;
    private GameManager gameManager;


    public GameManager getGameManager() {
        return gameManager;
    }

    public AdventureDeck(List<AdventureCard> cards, GameManager gM) {
        this.cards = cards;
        this.gameManager = gM;
    }


    public void shuffle(){
        //this.cards
    }

    public AdventureCard draw(){
        AdventureCard card = cards.getFirst();
        cards.remove(card);
        return card;
    }

    public List<AdventureCard> getStack(int stack){

        int unity =  cards.size()/4;
        stack--;

        //Stack 0 1 2 3
        //Unity = 2 && 6
        //0-5 6-11 12-17 18-23

        List<AdventureCard> newStack = cards.subList(stack*unity, ((stack*unity)+unity)-1);
        return newStack;
    }




}
