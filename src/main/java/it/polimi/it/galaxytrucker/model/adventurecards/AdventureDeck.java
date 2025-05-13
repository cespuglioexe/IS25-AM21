package it.polimi.it.galaxytrucker.model.adventurecards;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class AdventureDeck {
    private List<AdventureCard> cards;

    public void initializeAdventureCards(List<AdventureCard> cards) {
        this.cards = cards;
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

    /**
     * Draws the top card of the adventure card deck.
     *
     * @return the first card of the deck, as a {@code AdventureCard} interface.
     * @throws NoSuchElementException if the deck is empty, so a card can't be drawn.
     */
    public AdventureCard draw() throws NoSuchElementException {
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
