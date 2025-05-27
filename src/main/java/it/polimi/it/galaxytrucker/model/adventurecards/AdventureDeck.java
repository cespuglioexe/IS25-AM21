package it.polimi.it.galaxytrucker.model.adventurecards;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.*;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.FlightBoardFlightRules;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AdventureDeck {
    private List<AdventureCard> cards = new ArrayList<>();
    private Stack<AdventureCard> drawnCards = new Stack<>();
    private HashMap<Integer,List<AdventureCard>> stacks = new HashMap<>();

    public void initializeAdventureCards(List<AdventureCard> cards) {
        this.cards = cards;
    }

    public void initializeDeck() {
        cards = stacks.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public void addStack(int stack,List<AdventureCard> stackCards){
        stacks.put(stack,stackCards);
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
        drawnCards.push(card);
        return card;
    }

    public AdventureCard getLastDrawnCard() throws EmptyStackException {
        return drawnCards.peek();
    }

    public HashMap<Integer,List<AdventureCard>> getStacks(){
        return stacks;
    }




}
