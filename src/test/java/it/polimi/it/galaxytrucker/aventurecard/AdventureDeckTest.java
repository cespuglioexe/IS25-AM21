import it.polimi.it.galaxytrucker.aventurecard.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdventureDeckTest {

    @Test
    void shuffle() {
    }

    @Test
    void draw() {
        AbandonedShip ship = new AbandonedShip(3,2,5,0,5);
        AbandonedStation station = new AbandonedStation(2,3,null,0,5,null);
        OpenSpace space = new OpenSpace(null,5,null,0,0);
        Planet planet = new Planet(null,3,null,null,0,0,null);
        Slavers slaver = new Slavers(3,2,5,4,5);
        Smugglers smuggler = new Smugglers(3,2,5,4,5);
        StarDust star = new StarDust(null,null,null,0,0);

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
        AbandonedShip ship = new AbandonedShip(3,2,5,0,5);
        AbandonedStation station = new AbandonedStation(2,3,null,0,5,null);
        OpenSpace space = new OpenSpace(null,5,null,0,0);
        Planet planet = new Planet(null,3,null,null,0,0,null);
        Slavers slaver = new Slavers(3,2,5,4,5);
        Smugglers smuggler = new Smugglers(3,2,5,4,5);
        StarDust star = new StarDust(null,null,null,0,0);

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
        assertEquals(deck.getStack(0).get(0),ship);
        assertEquals(deck.getStack(0).get(1),station);
        assertEquals(deck.getStack(1).get(0),space);
        assertEquals(deck.getStack(1).get(1),planet);
        assertEquals(deck.getStack(2).get(0),slaver);
        assertEquals(deck.getStack(2).get(1),star);
        assertEquals(deck.getStack(3).get(0),smuggler);
        assertEquals(deck.getStack(3).get(1),space);
    }
}