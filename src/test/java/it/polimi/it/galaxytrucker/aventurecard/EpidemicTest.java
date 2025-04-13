package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.model.aventurecard.cards.Epidemic;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EpidemicTest {

    @Test
    void humanRemove() {

        ShipManager manager = new ShipManager(1);
        manager.addComponentTile(6,7, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        manager.addComponentTile(8,6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        manager.addComponentTile(6,6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));
        manager.addComponentTile(7,6, new CabinModule(List.of(TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE,TileEdge.SINGLE)));

        manager.addCrewmate(6,7, new Human());
        manager.addCrewmate(6,7, new Human());
        manager.addCrewmate(8,6, new Human());
        manager.addCrewmate(8,6, new Human());
        manager.addCrewmate(6,6, new Human());
        manager.addCrewmate(6,6, new Human());
        manager.addCrewmate(7,6, new Human());
        manager.addCrewmate(7,6, new Human());
        manager.addCrewmate(7,7, new Human());
        manager.addCrewmate(7,7, new Human());

        Epidemic epidemic = new Epidemic(Optional.of(2),Optional.of(3),null,0,5);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",Color.RED,manager);
        epidemic.HumanRemove(player1);
        assertEquals(player1.getShipManager().countCrewmates(), 5);
    }
}