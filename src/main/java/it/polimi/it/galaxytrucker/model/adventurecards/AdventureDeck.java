package it.polimi.it.galaxytrucker.model.adventurecards;


import it.polimi.it.galaxytrucker.model.adventurecards.refactored.Planets;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdventureDeck {

    private List<AdventureCard> cards;

    public AdventureDeck() {
        this.cards = initializeAdventureCards();
    }

    private List<AdventureCard> initializeAdventureCards() {
        //TODO from JSON file

        return null;
    }

    public List<AdventureCard> getCards() {
        return cards;
    }

    public void shuffle(){
        Random random = new Random();
        List<AdventureCard> support = new ArrayList<>();
        int randIndex;

        while(cards.size() != 0){
            if(cards.size()==1){
                support.add(cards.get(0));
                cards.remove(0);
            } else {
                randIndex = random.nextInt(0, cards.size() - 1);
                support.add(cards.get(randIndex));
                cards.remove(cards.get(randIndex));
            }
        }
        cards = support;
    }

    public AdventureCard draw(){
        AdventureCard card = cards.getFirst();
        cards.remove(card);
        return card;
    }

    public List<AdventureCard> getStack(int stack){

        int unit =  cards.size()/4;
        stack--;
        List<AdventureCard> newStack = new ArrayList<AdventureCard>();

        //Stack 0 1 2
        //Unit = 2 && 6
        //0-5 6-11 12-17 18-23
        if(stack== 0 || stack ==1 || stack == 2) {
            newStack = cards.subList(stack*unit, ((stack * unit) + unit));
        }
        return newStack;
    }




}
