package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.model.aventurecard.*;
import it.polimi.it.galaxytrucker.model.aventurecard.cards.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AdventureDeckTest {

    @Test
    void shuffle() {
        AbandonedShip ship = new AbandonedShip(Optional.of(3),Optional.of(2),Optional.of(5),0,5);
        AbandonedStation station = new AbandonedStation(Optional.of(2), Optional.of(3),null,0,5,null);
        OpenSpace space = new OpenSpace(null,Optional.of(5),null,0,0);
        Planet planet = new Planet(null,Optional.of(3),null,null,0,0,null);
        Slavers slaver = new Slavers(Optional.of(3),Optional.of(2),Optional.of(5),4,5);
        Smugglers smuggler = new Smugglers(Optional.of(3),Optional.of(2),null,4,5, null);
        StarDust star = new StarDust(null,null,null,0, 0);

        List<AdventureCard> list = new ArrayList<>();
        list.add(ship);
        list.add(station);
        list.add(space);
        list.add(planet);
        list.add(slaver);
        list.add(star);
        list.add(smuggler);
        list.add(space);

        AdventureDeck deck = new AdventureDeck(list);
        deck.shuffle();

        for(AdventureCard card : deck.getCards()){
            System.out.println(card.toString());
        }
        assertEquals(deck.getCards().size(),8);
    }

    @Test
    void draw() {
        AbandonedShip ship = new AbandonedShip(Optional.of(3),Optional.of(2),Optional.of(5),0,5);
        AbandonedStation station = new AbandonedStation(Optional.of(2), Optional.of(3),null,0,5,null);
        OpenSpace space = new OpenSpace(null,Optional.of(5),null,0,0);
        Planet planet = new Planet(null,Optional.of(3),null,null,0,0,null);
        Slavers slaver = new Slavers(Optional.of(3),Optional.of(2),Optional.of(5),4,5);
        Smugglers smuggler = new Smugglers(Optional.of(3),Optional.of(2),null,4,5, null);
        StarDust star = new StarDust(null,null,null,0, 0);

        List<AdventureCard> list = new ArrayList<>();
        list.add(ship);
        list.add(station);
        list.add(space);
        list.add(planet);
        list.add(slaver);
        list.add(star);
        list.add(smuggler);


        AdventureDeck deck = new AdventureDeck(list);
        assertEquals(deck.draw(),ship);
    }

    @Test
    void getStack() {
        AbandonedShip ship = new AbandonedShip(Optional.of(3),Optional.of(2),Optional.of(5),0,5);
        AbandonedStation station = new AbandonedStation(Optional.of(2), Optional.of(3),null,0,5,null);
        OpenSpace space = new OpenSpace(null,Optional.of(5),null,0,0);
        Planet planet = new Planet(null,Optional.of(3),null,null,0,0,null);
        Slavers slaver = new Slavers(Optional.of(3),Optional.of(2),Optional.of(5),4,5);
        Smugglers smuggler = new Smugglers(Optional.of(3),Optional.of(2),null,4,5, null);
        StarDust star = new StarDust(null,null,null,0, 0);

        List<AdventureCard> list = new ArrayList<>();
        list.add(ship);
        list.add(station);
        list.add(space);
        list.add(planet);
        list.add(slaver);
        list.add(star);
        list.add(smuggler);
        list.add(space);


        AdventureDeck deck = new AdventureDeck(list);

        assertEquals(deck.getStack(1).get(0),ship);
        assertEquals(deck.getStack(1).get(1),station);
        assertEquals(deck.getStack(2).get(0),space);
        assertEquals(deck.getStack(2).get(1),planet);
        assertEquals(deck.getStack(3).get(0),slaver);
        assertEquals(deck.getStack(3).get(1),star);
    }
}