package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;

import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CrewmatePenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.design.strategyPattern.FlightRules;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.Projectile;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.exceptions.NotFoundException;

public class CombatZone extends Attack implements AdventureCard, FlightDayPenalty, CrewmatePenalty {
    private final int crewmatePenalty = 2;
    private final int flightDayPenalty = 3;
    private HashMap<Player, Integer> playersAndEnginePower;
    private HashMap<Player, Double> playersAndFirePower;

    private Player playerWithLeastEnginePower;

    private FlightRules flightRules;

    public CombatZone(int crewmatePenalty, int flightDayPenalty, FlightRules flightRules) {
        super(createProjectiles());
        this.flightRules = flightRules;
        setPlayers();
    }
    private void setPlayers() {
        List<Player> players = flightRules.getPlayerOrder();
        playersAndEnginePower = new HashMap<>();
        playersAndFirePower = new HashMap<>();

        for (Player player : players) {
            ShipManager ship = player.getShipManager();
            playersAndEnginePower.put(player, ship.calculateEnginePower());
            playersAndFirePower.put(player, ship.calculateFirePower());
        }
    }
    private static HashMap<Projectile, Direction> createProjectiles() {
        Projectile smallProjectile = Projectile.SMALL;
        Projectile bigProjectile = Projectile.BIG;

        HashMap<Projectile, Direction> projectilesAndDirections = new HashMap<>();
        projectilesAndDirections.put(smallProjectile, Direction.UP);
        projectilesAndDirections.put(bigProjectile, Direction.UP);

        return projectilesAndDirections;
    }

    @Override
    public void play() {
        
    }

    public Player findPlayerWithLeastCrewmates() {
        List<Player> players = flightRules.getPlayerOrder();

        return players.stream()
            .reduce((p1, p2) -> {
                ShipManager ship1 = p1.getShipManager();
                ShipManager ship2 = p2.getShipManager();

                return ship1.countCrewmates() <= ship2.countCrewmates() ? p1 : p2;
            })
            .orElseThrow(() -> new NotFoundException("No user found"));
    }

    public Player findPlayerWithLeastEnginePower() {
        return playerWithLeastEnginePower = playersAndEnginePower.keySet().stream()
            .reduce((p1, p2) -> {
                int enginePower1 = playersAndEnginePower.get(p1);
                int enginePower2 = playersAndEnginePower.get(p2);

                return enginePower1	<= enginePower2 ? p1 : p2;
            })
            .orElseThrow(() -> new NotFoundException("No user found"));
    }

    public Player findPlayerWithLeastFirePower() {
        Player playerWithLeastFirePower = playersAndFirePower.keySet().stream()
            .reduce((p1, p2) -> {
                double firePower1 = playersAndFirePower.get(p1);
                double firePower2 = playersAndFirePower.get(p2);

                return firePower1	<= firePower2 ? p1 : p2;
            })
            .orElseThrow(() -> new NotFoundException("No user found"));
        
        setPlayer(playerWithLeastFirePower);
        return playerWithLeastFirePower;
    }

    public void selectEngine(Player player, HashMap<List<Integer>, List<Integer>> enginesAndBatteries) {
        ShipManager ship = player.getShipManager();
        int enginePower = (int) ship.activateComponent(enginesAndBatteries);

        int baseEnginePower = playersAndEnginePower.get(player);
        enginePower += baseEnginePower;

        playersAndEnginePower.put(player, enginePower);
    }

    public void selectCannons(Player player, HashMap<List<Integer>, List<Integer>> doubleCannonsAndBatteries) {
        ShipManager ship = player.getShipManager();
        double firePower = ship.activateComponent(doubleCannonsAndBatteries);

        double baseFirePower = playersAndFirePower.get(player);
        firePower += baseFirePower;

        playersAndFirePower.put(player, firePower);
    }
    
    @Override
    public void attack() {
        for (Projectile projectile : getProjectilesAndDirection().keySet()) {
            List<Integer> aimedCoords = getAimedCoordsByProjectile(projectile);
            Direction direction = getProjectilesAndDirection().get(projectile);

            if (projectile == Projectile.SMALL) {
                if (isShieldActivated(direction)) {
                    continue;
                }
            }
            destroyComponent(aimedCoords.get(0), aimedCoords.get(1));
        }
    }
    private boolean isShieldActivated(Direction direction) {
        for (List<Integer> shieldCoord : getShieledsAndDirection().keySet()) {
            List<Direction> coveredDirections = getShieledsAndDirection().get(shieldCoord);
            if (coveredDirections.contains(direction)) {
                return true;
            }
        }
        return false;
    }
    private void destroyComponent(int row, int column) {
        ShipManager ship = getPlayer().getShipManager();
        try {
            ship.removeComponentTile(row, column);
        } catch (IllegalComponentPositionException emptyTile) {

        } catch (IndexOutOfBoundsException missedShot) {

        }
    }

    @Override
    public void applyCrewmatePenalty(int shipRow, int shipColumn) {
        ShipManager ship = playerWithLeastEnginePower.getShipManager();

        ship.removeCrewmate(shipRow, shipColumn);
    }

    @Override
    public void applyFlightDayPenalty() {
        Player player = findPlayerWithLeastCrewmates();

        flightRules.movePlayerBackwards(flightDayPenalty, player);
    }
}
