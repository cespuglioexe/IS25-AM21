package it.polimi.it.galaxytrucker.model.adventurecards.refactored;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.combatZone.StartState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.CrewmatePenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.FlightDayPenalty;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Attack;
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
        start(new StartState());
    }

    public Player findPlayerWithLeastCrewmates() {
        List<Player> players = flightRules.getPlayerOrder();

        int minCrewmates = getMinimumCrewmateCount(players);
        List<Player> playersWithLeastCrewmates = getPlayersWithCrewmatesNumber(players, minCrewmates);

        if (playersWithLeastCrewmates.size() == 1) {
            return playersWithLeastCrewmates.get(0);
        }
        return findLeadingPlayerAmong(players, playersWithLeastCrewmates);
    }
    private int getMinimumCrewmateCount(List<Player> players) {
        return players.stream()
            .mapToInt(p -> p.getShipManager().countCrewmates())
            .min()
            .orElseThrow(() -> new NotFoundException("No user found"));
    }
    private List<Player> getPlayersWithCrewmatesNumber(List<Player> players, int crewmatersNumber) {
        return players.stream()
            .filter(p -> p.getShipManager().countCrewmates() == crewmatersNumber)
            .collect(Collectors.toList());
    }
    private Player findLeadingPlayerAmong(List<Player> playersInFlightOrder, List<Player> players) {
        return playersInFlightOrder.stream()
            .filter(players::contains)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("No user found"));
    }

    public Player findPlayerWithLeastEnginePower() {
        int minEnginePower = getMinimumEnginePower();
        List<Player> playersWithLeastEnginePower = getPlayersWithEnginePower(minEnginePower);

        if (playersWithLeastEnginePower.size() == 1) {
            Player player = playersWithLeastEnginePower.get(0);
            playerWithLeastEnginePower = player;
            return player;
        }
        Player player = findLeadingPlayerAmong(flightRules.getPlayerOrder(), playersWithLeastEnginePower);
        playerWithLeastEnginePower = player;
        return player;
    }
    private int getMinimumEnginePower() {
        return playersAndEnginePower.keySet().stream()
            .mapToInt(p -> playersAndEnginePower.get(p))
            .min()
            .orElseThrow(() -> new NotFoundException("No user found"));
    }
    private List<Player> getPlayersWithEnginePower(int enginePower) {
        return playersAndEnginePower.keySet().stream()
            .filter(p -> playersAndEnginePower.get(p) == enginePower)
            .collect(Collectors.toList());
    }

    public Player findPlayerWithLeastFirePower() {
        double minFirePower = getMinimumFirePower();
        List<Player> playersWithLeastFirePower = getPlayersWithFirePower(minFirePower);

        if (playersWithLeastFirePower.size() == 0) {
            Player player = playersWithLeastFirePower.get(0);
            setPlayer(player);
            return player;
        }
        Player player = findLeadingPlayerAmong(flightRules.getPlayerOrder(), playersWithLeastFirePower);
        setPlayer(player);
        return player;
    }
    private double getMinimumFirePower() {
        return playersAndFirePower.keySet().stream()
            .mapToDouble(p -> playersAndFirePower.get(p))
            .min()
            .orElseThrow(() -> new NotFoundException("No user found"));
    }
    private List<Player> getPlayersWithFirePower(double firePower) {
        return playersAndFirePower.keySet().stream()
            .filter(p -> playersAndFirePower.get(p) == firePower)
            .collect(Collectors.toList());
    }

    public void selectEngines(Player player, HashMap<List<Integer>, List<Integer>> enginesAndBatteries) {
        ShipManager ship = player.getShipManager();
        int enginePower = (int) ship.activateComponent(enginesAndBatteries);

        int baseEnginePower = playersAndEnginePower.get(player);
        enginePower += baseEnginePower;

        playersAndEnginePower.put(player, enginePower);
        updateState();
    }

    public void selectNoEngines(Player player) {
        updateState();
    }

    public void selectCannons(Player player, HashMap<List<Integer>, List<Integer>> doubleCannonsAndBatteries) {
        ShipManager ship = player.getShipManager();
        double firePower = ship.activateComponent(doubleCannonsAndBatteries);

        double baseFirePower = playersAndFirePower.get(player);
        firePower += baseFirePower;

        playersAndFirePower.put(player, firePower);
        updateState();
    }

    public void selectNoCannons(Player player) {
        updateState();
    }
    
    @Override
    public void attack() {
        for (Projectile projectile : getProjectilesAndDirection().keySet()) {
            List<Integer> aimedCoords = aimAtCoordsWith(projectile);
            Direction direction = getProjectilesAndDirection().get(projectile);

            if (projectile == Projectile.SMALL) {
                if (isShieldActivated(direction)) {
                    continue;
                }
            }
            destroyComponent(aimedCoords.get(0), aimedCoords.get(1));
        }
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
    public int getCrewmatePenalty() {
        return crewmatePenalty;
    }

    @Override
    public void applyCrewmatePenalty(int shipRow, int shipColumn) {
        ShipManager ship = playerWithLeastEnginePower.getShipManager();

        ship.removeCrewmate(shipRow, shipColumn);
        updateState();
    }

    @Override
    public int getCrewmatePenalty() {
        return 0;
    }

    @Override
    public void applyFlightDayPenalty() {
        Player player = findPlayerWithLeastCrewmates();

        flightRules.movePlayerBackwards(flightDayPenalty, player);
    }

    public int getNumberOfBoardPlayers() {
        return flightRules.getPlayerOrder().size();
    }
}
