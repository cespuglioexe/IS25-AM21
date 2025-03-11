package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Color;
import it.polimi.it.galaxytrucker.utility.Direction;
import it.polimi.it.galaxytrucker.utility.Projectile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PiratesTest {

    @Test
    void attack() {
        ShipManager shipManager = new ShipManager();
        Color color = Color.BLUE;
        List<Player> player = new ArrayList<>();
        player.add(1,"Sergio",10,color,shipManager);
        Map<Projectile, Direction> meteorite = new HashMap<>();
        meteorite.put(Projectile.BIG, Direction.UP);
        meteorite.put(Projectile.SMALL, Direction.DOWN);

        for(Map.Entry<Projectile, Direction> entry : meteorite.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
            if()

        }
    }
}